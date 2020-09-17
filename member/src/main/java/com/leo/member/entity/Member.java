package com.leo.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Liu
 */
@Data
@TableName("member")
public class Member {

    @TableId
    private Long id;

    private String name;

    private String password;

    private String third;
}
