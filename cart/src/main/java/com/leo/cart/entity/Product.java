package com.leo.cart.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liu
 */
@Data
public class Product {

    private Long id;

    private String name;

    private BigDecimal price;

    private String intro;

}
