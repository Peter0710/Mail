package com.leo.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Liu
 */
@Data
@TableName("member_address")
public class Address {

    @TableId
    private Long id;

    private Long uid;

    private String name;

    private String address;

    private String phone;

    private Integer prefer;


}
