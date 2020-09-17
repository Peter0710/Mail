package com.leo.secondkill.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Liu
 */
@Data
public class SecondKill {

    private Long id;

    private Date startTime;

    private Date endTime;

    private List<SecondProduct> products;

    private String name;
}
