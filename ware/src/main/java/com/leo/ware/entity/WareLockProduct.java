package com.leo.ware.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Liu
 */
@Data
@TableName("ware_lockproduct")
public class WareLockProduct {

    private String orderId;

    private Long productId;

    private Integer amount;
}
