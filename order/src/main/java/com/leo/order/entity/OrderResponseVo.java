package com.leo.order.entity;

import lombok.Data;

/**
 * 订单生成后返回给前端的实体类
 * @author Liu
 */
@Data
public class OrderResponseVo {

    private Order order;

    private Integer code;
}
