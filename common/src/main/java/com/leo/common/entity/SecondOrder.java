package com.leo.common.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Liu
 */
@Data
public class SecondOrder {

    private String orderId;

    private BigDecimal price;

    private Integer amount;

    private Long productId;

    private Long sessionId;

    private Long uid;

}
