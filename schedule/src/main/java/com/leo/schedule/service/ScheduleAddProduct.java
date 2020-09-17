package com.leo.schedule.service;

import com.alibaba.fastjson.TypeReference;
import com.leo.common.common.CommonResult;
import com.leo.common.constant.SecondConstant;
import com.leo.schedule.config.SecondScheduleConstant;
import com.leo.schedule.entity.SecondProduct;
import com.leo.schedule.entity.SecondSkill;
import com.leo.schedule.feign.DiscountFeignService;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Liu
 */
@Service
public class ScheduleAddProduct {

    @Autowired
    DiscountFeignService feignService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;
    /**
     * 定时任务,查询秒杀信息，
     * 并远程调用查询商品详细信息，存入redis
     */

    @Async
    @Scheduled(cron = "0 0 3 * * ?")
    public void addProduct(){
        //通过加锁，保证秒杀信息上架的幂等性
        RLock lock = redissonClient.getLock(SecondScheduleConstant.SCHEDULE_SECOND_LOCK);
        lock.lock(10, TimeUnit.SECONDS);
        //查找discount模块的秒杀场次和面杀商品详情并加入到redis中
        try {
            uploadSecondInfo();
        }finally {
            lock.unlock();
        }
    }


    private void uploadSecondInfo() {
        CommonResult secondInfo = feignService.getSecondInfo();
        if (secondInfo.getCode() == CommonResult.SUCCESS_CODE){
            List<SecondSkill> data = secondInfo.getData("data", new TypeReference<List<SecondSkill>>() {
            });
            data.stream().forEach(item ->{
                long startTime = item.getStartTime().getTime();
                long endTime = item.getEndTime().getTime();
                //如果已经添加过了，就不用再重新加入redis
                if (!redisTemplate.hasKey(SecondConstant.SECOND_KILL + startTime + "_" + endTime)){
                    //list存储场次信息 key为 second:session:startTime + "_" + endTime
                    String sessionKey = SecondConstant.SECOND_KILL + startTime + "_" + endTime;
                    redisTemplate.opsForValue().set(sessionKey, item);
                    //hash存储场次对应的商品信息 +token
                    List<SecondProduct> products = item.getProducts();
                    products.stream().forEach(product -> {
                        Long productId = product.getProductId();
                        //远程调用商品模块查询商品详情
                        String token = UUID.randomUUID().toString().replaceAll("-", "");
                        product.setToken(token);
                        product.setStartTime(startTime);
                        product.setEndTime(endTime);
                        BoundHashOperations operations = redisTemplate.boundHashOps(SecondConstant.SECOND_KILL_PRODUCT);
                        operations.put(item.getId() + "_" + product.getProductId(), product);
                        //设置信号量，为秒杀商品量，
                        //信号量的名字为 second:stock:token, 防止安全问题
                        RSemaphore stock = redissonClient.getSemaphore(SecondConstant.SECOND_KILL_STOCK + token);
                        stock.trySetPermits(product.getAmount());
                    });
                }
            });
        }
    }
}
