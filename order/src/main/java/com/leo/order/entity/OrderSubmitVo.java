package com.leo.order.entity;

import lombok.Data;

/**
 * 前端提交的订单实体类
 * @author Liu
 */
@Data
public class OrderSubmitVo {

    private Long addressId;

    private String token;

    private Long uid;

}
