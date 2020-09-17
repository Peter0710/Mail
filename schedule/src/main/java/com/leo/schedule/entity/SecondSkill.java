package com.leo.schedule.entity;

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
public class SecondSkill {

    private Long id;

    private Date startTime;

    private Date endTime;

    private List<SecondProduct> products;

    private String name;
}
