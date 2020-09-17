package com.leo.discount.controller;

import com.leo.common.common.CommonResult;
import com.leo.discount.entity.SecondProduct;
import com.leo.discount.entity.SecondSkill;
import com.leo.discount.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Liu
 */
@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    DiscountService discountService;

    /**
     * 添加秒杀场次信息，添加秒杀开始时间和结束时间按
     * @param secondSkill
     * @return
     */
    @PostMapping("/addSecond")
    public CommonResult addSecondKill(@RequestBody SecondSkill secondSkill){
        Integer result = discountService.addSecondKill(secondSkill);
        return CommonResult.ok();
    }

    @PostMapping("/addSecondProduct")
    public CommonResult addSecondProduct(@RequestBody SecondProduct secondProduct){
        Integer result = discountService.addSecondKillProduct(secondProduct);
        return CommonResult.ok();
    }

    /**
     * 获取未来三天的秒杀场次和对应的商品
     * @return
     */
    @GetMapping("/getSecondInfo")
    public CommonResult getSecondInfo(){
        List<SecondSkill> secondList = discountService.getSecondInfo();
        return CommonResult.ok().set("data", secondList);
    }

}
