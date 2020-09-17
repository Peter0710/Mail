package com.leo.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leo.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Liu
 */
@Mapper
public interface OrderDao extends BaseMapper<Order> {
}
