package com.leo.discount.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liu
 */
@Data
@TableName("second_product")
public class SecondProduct {

    @TableId
    private Long id;

    private BigDecimal price;

    private Long productId;

    private Long killId;

    private Integer amount;

    private Integer permitNumber;
}
