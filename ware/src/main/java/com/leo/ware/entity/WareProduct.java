package com.leo.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Liu
 */
@Data
@TableName("ware")
public class WareProduct {

    @TableId
    private Long id;

    private Long productId;

    private Integer stock;

    private Integer lockStock;

}
