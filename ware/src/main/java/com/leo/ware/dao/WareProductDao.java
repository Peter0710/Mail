package com.leo.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leo.ware.entity.WareProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Liu
 */
@Mapper
public interface WareProductDao extends BaseMapper<WareProduct> {
    /**
     * 锁定库存，确定仓库中货物是否足够
     * @param productId
     * @param amount
     * @return
     */
    Integer lockProduct(@Param("productId") Long productId, @Param("amount") String amount);

    /**
     * 解锁库存
     * @param productId
     * @param amount
     */
    void releaseStock(@Param("productId") Long productId, @Param("amount") Integer amount);
}
