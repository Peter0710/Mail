package com.leo.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Liu
 */
@Data
public class Member {

    private Long id;

    private String name;

    private String password;

    private String third;
}
