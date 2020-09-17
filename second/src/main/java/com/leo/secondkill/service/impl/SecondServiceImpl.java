package com.leo.secondkill.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leo.common.constant.SecondConstant;
import com.leo.common.entity.SecondOrder;
import com.leo.secondkill.entity.SecondKill;
import com.leo.secondkill.entity.SecondProduct;
import com.leo.secondkill.service.SecondService;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Liu
 */
@Service
public class SecondServiceImpl implements SecondService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 获取当前时间的秒杀商品
     * @return
     */
    @Override
    public List<SecondProduct> getSecondInfo() {

        Date date = new Date();
        long currentTime = date.getTime();

        //获取满足时间的场次信息
        Set<String> sessionKeys = redisTemplate.keys(SecondConstant.SECOND_KILL + "*");
        for (String key : sessionKeys){
            String[] s = key.replace(SecondConstant.SECOND_KILL, "").split("_");
            Long startTime = Long.parseLong(s[0]);
            Long endTime = Long.parseLong(s[1]);
            if (currentTime >= startTime && currentTime <= endTime ){
                SecondKill secondKill = (SecondKill)redisTemplate.opsForValue().get(key);
                List<String> collect = secondKill.getProducts().stream()
                        .map(product -> secondKill.getId() + "_" + product.getProductId()).collect(Collectors.toList());
                //获取满足时间的商品信息
                List<Object> list = redisTemplate.opsForHash().multiGet(SecondConstant.SECOND_KILL_PRODUCT, collect);
                if (list != null && list.size() > 0){
                    List<SecondProduct> products = list.stream().map(item -> {
                        SecondProduct product = JSON.parseObject(item.toString(), SecondProduct.class);
                        return product;
                    }).collect(Collectors.toList());
                    return products;
                }
                break;
            }
        }
        return null;
    }

    @Override
    public String secondKill(String uid, String token, Integer num, String kill_id) {
        //先判断是否登录
        //TODO 拦截器校验
        BoundHashOperations operations = redisTemplate.boundHashOps(SecondConstant.SECOND_KILL_PRODUCT);
        String result = (String)operations.get(kill_id);
        //判断是否有这个商品
        if (result != null){
            SecondProduct secondProduct = JSON.parseObject(result, SecondProduct.class);
            Date date = new Date();
            long currentTime = date.getTime();
            //判断是否还在秒杀时间
            if (currentTime >= secondProduct.getStartTime() && currentTime <= secondProduct.getEndTime()){

                //判断随机码是否相等
                if(secondProduct.getToken().equals(token)){
                    //秒杀的数量是否符合要求
                    if (num <= secondProduct.getPermitNumber()){
                        //判断用户是否已经秒杀过了
                        Long ttl = secondProduct.getEndTime() - currentTime;
                        Integer lastTime = (Integer)redisTemplate.opsForValue()
                                .get(SecondConstant.SECOND_KILL_CONSUMER + uid + "_" + kill_id);
                        if (lastTime == null){
                            //没有买过，尝试获取信号量
                            redisTemplate.opsForValue().set(SecondConstant.SECOND_KILL_CONSUMER + uid + "_" + kill_id, num, ttl, TimeUnit.MICROSECONDS);
                            getStock(token, num);
                            //快速下单，发送消息到mq
                            String timeId = IdWorker.getTimeId();
                            SecondOrder secondOrder = new SecondOrder();
                            secondOrder.setAmount(num);
                            secondOrder.setUid(Long.parseLong(uid));
                            secondOrder.setPrice(secondProduct.getPrice());
                            secondOrder.setSessionId(secondProduct.getKillId());
                            secondOrder.setProductId(secondProduct.getProductId());
                            secondOrder.setOrderId(timeId);
                            rabbitTemplate.convertAndSend("order-event-exchange", "order.second.order", secondOrder);
                            return timeId;
                        }else {
                            //判断是否此次购买是否会超过允许购买书
                            if(lastTime + num < secondProduct.getPermitNumber()){
                                //可以购买 ,获取信号量
                                redisTemplate.opsForValue().set(SecondConstant.SECOND_KILL_CONSUMER + uid + "_" + kill_id, num + lastTime, ttl, TimeUnit.MICROSECONDS);
                                getStock(token, num);
                                //快速下单，发送消息到mq
                                String timeId = IdWorker.getTimeId();
                                return timeId;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void getStock(String token, Integer num) {
        RSemaphore semaphore = redissonClient.getSemaphore(SecondConstant.SECOND_KILL_STOCK + token);
        try {
            semaphore.tryAcquire(num, 10, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
