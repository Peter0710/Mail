package com.leo.discount.service;

import com.leo.discount.entity.SecondProduct;
import com.leo.discount.entity.SecondSkill;

import java.util.List;

/**
 * @author Liu
 */
public interface DiscountService {
    Integer addSecondKill(SecondSkill secondSkill);

    Integer addSecondKillProduct(SecondProduct secondProduct);

    List<SecondSkill> getSecondInfo();
}
