package com.leo.discount.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leo.discount.dao.SecondProductDao;
import com.leo.discount.dao.SecondSkillDao;
import com.leo.discount.entity.SecondProduct;
import com.leo.discount.entity.SecondSkill;
import com.leo.discount.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liu
 */
@Service
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    SecondProductDao secondProductDao;

    @Autowired
    SecondSkillDao secondSkillDao;

    @Override
    public Integer addSecondKill(SecondSkill secondSkill) {
        int insert = secondSkillDao.insert(secondSkill);
        return insert;
    }

    @Override
    public Integer addSecondKillProduct(SecondProduct secondProduct) {
        int insert = secondProductDao.insert(secondProduct);
        return insert;
    }

    /**|
     *        //查询未来三天的秒杀场次
     *         //查询秒杀场次对应的秒杀商品
     * @return
     */
    @Override
    public List<SecondSkill> getSecondInfo() {
        //格式是年月日 如 2019-09-09
        LocalDate now = LocalDate.now();
        //格式是年月日 如 2019-09-10
        LocalDate localDate = now.plusDays(1);
        //格式是年月日 如 2019-09-11
        LocalDate localDate1 = now.plusDays(2);

        //00:00:00
        LocalTime min = LocalTime.MIN;
        //23:59:59
        LocalTime max = LocalTime.MAX;

        //拼接显示为年月日时分秒
        LocalDateTime startTime = LocalDateTime.of(now, min);
        LocalDateTime endTime = LocalDateTime.of(localDate1, max);
        String formatStartTime = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formatEndTime = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<SecondSkill> secondSkillList = secondSkillDao.selectList(new QueryWrapper<SecondSkill>()
                .between("start_name", formatStartTime, formatEndTime));

        if (secondSkillList != null && secondSkillList.size() > 0){
            List<SecondSkill> killList = secondSkillList.stream().map(item -> {
                List<SecondProduct> secondProducts = secondProductDao.selectList(new QueryWrapper<SecondProduct>().eq("kill_id", item.getId()));
                item.setProducts(secondProducts);
                return item;
            }).collect(Collectors.toList());
            return killList;
        }
        return null;
    }
}
