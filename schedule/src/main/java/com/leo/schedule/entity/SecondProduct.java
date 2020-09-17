package com.leo.schedule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Liu
 */
@Data
public class SecondProduct {

    private Long id;

    private Long productId;

    private String name;

    private String intro;

    private Integer amount;

    private BigDecimal price;

    private Long killId;

    private String token;

    private Integer permitNumber;

    private Long startTime;

    private Long endTime;

}
