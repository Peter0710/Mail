package com.leo.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leo.order.entity.*;

import java.util.Map;

/**
 * @author Liu
 */
public interface OrderConfirmService  {


    OrderVo getOrder(String uid);

    OrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo);

    Integer getOrderSttues(String orderId);

    void closeOrder(Order order);
}
