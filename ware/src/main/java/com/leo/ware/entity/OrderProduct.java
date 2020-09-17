package com.leo.ware.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单中包含的商品数据信息
 * @author Liu
 */
@Data
public class OrderProduct {

    private String productName;

    private BigDecimal price;

    private String orderId;

    private Integer amount;

    private BigDecimal totalPrice;

    private Long productId;

    private String productInfo;
}
