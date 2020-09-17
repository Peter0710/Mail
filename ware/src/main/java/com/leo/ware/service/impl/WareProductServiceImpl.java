package com.leo.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leo.common.common.CommonResult;
import com.leo.ware.dao.WareLockOrderDao;
import com.leo.ware.dao.WareLockProductDao;
import com.leo.ware.dao.WareProductDao;
import com.leo.ware.entity.*;
import com.leo.ware.feign.OrderFeignService;
import com.leo.ware.service.WareProductService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Liu
 */
@Service
public class WareProductServiceImpl implements WareProductService {

    @Autowired
    WareProductDao productDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    WareLockOrderDao wareLockOrderDao;

    @Autowired
    WareLockProductDao wareLockProductDao;

    @Autowired
    OrderFeignService orderFeignService;

    /**
     *订单取消，解锁库存
     * 下订单成功，但是部分业务回滚，需要解锁之前锁定的库存
     * @param order
     * @return
     */
    @Transactional
    @Override
    public Boolean lockProduct(Order order) throws Exception {
        //先记录订单信息
        WareLockOrder wareLockOrder = new WareLockOrder();
        String orderId = order.getOrderId();
        wareLockOrder.setId(null);
        wareLockOrder.setOrderId(orderId);
        wareLockOrder.setPay(0);
        wareLockOrderDao.insert(wareLockOrder);

        Boolean flag = false;
        for (OrderProduct product : order.getProducts()){
            Integer result = productDao.lockProduct(product.getProductId(), product.getAmount().toString());
            if (result == 1){
                //记录锁定成功的商品
                WareLockProduct wareLockProduct = new WareLockProduct();
                wareLockProduct.setOrderId(orderId);
                wareLockProduct.setProductId(product.getProductId());
                wareLockProduct.setAmount(product.getAmount());
                wareLockProductDao.insert(wareLockProduct);
                flag = true;
            }else {
                flag = false;
            }
        }
        if (!flag){
            throw new RuntimeException();
        }
        //发送消息给队列
        rabbitTemplate.convertAndSend("ware-event-exchange", "ware.create.ware",wareLockOrder);
        return flag;
    }

    /**
     * 添加库存信息
     * @param wareProduct
     * @return
     */
    @Override
    public Integer addStock(WareProduct wareProduct) {
        return productDao.insert(wareProduct);
    }

    /**
     * 更新库存信息
     * @param wareProduct
     * @return
     */
    @Override
    public Integer updateStock(WareProduct wareProduct) {
        int result = productDao.update(wareProduct,
                new QueryWrapper<WareProduct>().eq("product_id", wareProduct.getProductId()));
        return result;
    }

    /**
     * 自动解锁库存，先远程查看订单状态，是否支付，取消，待支付，完成的状态
     * 解锁库存，
     * @param orderId
     * @return
     */
    @Transactional
    @Override
    public Boolean releaseStock(String orderId) {
        CommonResult orderStatue = orderFeignService.getOrderStatue(orderId);
        WareLockOrder wareLockOrder = wareLockOrderDao.selectOne(new QueryWrapper<WareLockOrder>().eq("order_id", orderId));
        Integer pay1 = wareLockOrder.getPay();
        if (orderStatue.getCode() == CommonResult.SUCCESS_CODE){
            Integer pay = orderStatue.getData("data", new TypeReference<Integer>(){});
            if (pay != null){
                if (pay == 2){
                    //已取消， 解锁库存
                    if (pay1 == 0){
                        //说明库存还未解锁，需要解锁
                        List<WareLockProduct> list = wareLockProductDao.selectList(new QueryWrapper<WareLockProduct>().eq("order_id", orderId));
                        list.stream().forEach(product ->{
                            //还原库存锁定的信息
                            productDao.releaseStock(product.getProductId(), product.getAmount());
                        });
                        //更新锁定信息为2，已解锁
                        wareLockOrderDao.update(wareLockOrder, new QueryWrapper<WareLockOrder>().eq("pay", 2));
                    }
                }
                if (pay == 1){
                    //支付完成，更改库存信息为1， 表示已支付。
                    wareLockOrderDao.update(wareLockOrder, new QueryWrapper<WareLockOrder>().eq("pay", 1));
                }
                return true;
            }
        }
        return false;
    }
}
