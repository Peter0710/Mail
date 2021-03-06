package com.leo.ware.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 存储订单数据
 * @author Liu
 */
@Data
public class Order {

    private Long id;

    private BigDecimal price;

    private Integer pay;

    private Date time;

    private Long uid;

    private String phone;

    private String address;

    private String name;

    private String orderId;

    private List<OrderProduct> products;
}
