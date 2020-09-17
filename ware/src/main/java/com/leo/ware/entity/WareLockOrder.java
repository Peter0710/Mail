package com.leo.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Liu
 */
@Data
@TableName("ware_lockorder")
public class WareLockOrder {

    @TableId
    private Long id;

    private String orderId;

    private Integer pay;
}
