package com.leo.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leo.order.entity.OrderProduct;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Liu
 */
@Mapper
public interface OrderProductDao extends BaseMapper<OrderProduct> {
}
