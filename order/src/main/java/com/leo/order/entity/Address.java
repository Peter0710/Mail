package com.leo.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Liu
 */
@Data
public class Address {

    private Long id;

    private Long uid;

    private String name;

    private String address;

    private String phone;

    private Integer prefer;

}
