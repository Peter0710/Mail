package com.leo.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liu
 */
@Data
@TableName("product")
public class Product {

    @TableId
    private Long id;

    private String name;

    private BigDecimal price;

    private String intro;

}
