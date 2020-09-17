package com.leo.order.service.impl;

import com.leo.common.entity.SecondOrder;
import com.leo.order.service.SecondOrderService;
import org.springframework.stereotype.Service;

/**
 * @author Liu
 */
@Service
public class SecondOrderImpl implements SecondOrderService {
    /**
     * 创建秒杀订单
     * @param secondOrder
     * @return
     */
    @Override
    public Boolean creatSecondOrder(SecondOrder secondOrder) {
        //创建秒杀订单 --- 库存模块，锁定库存
        return null;
    }
}
