package com.leo.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leo.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Liu
 */
@Mapper
public interface ProductDao extends BaseMapper<Product> {
}
