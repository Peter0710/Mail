package com.leo.discount.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Liu
 */
@Data
@TableName("secondkill")
public class SecondSkill {

    @TableId
    private Long id;

    private String name;

    private Date startTime;

    private Date endTime;

    @TableField(exist = false)
    private List<SecondProduct> products;
}
