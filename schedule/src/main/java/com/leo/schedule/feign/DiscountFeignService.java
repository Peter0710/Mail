package com.leo.schedule.feign;

import com.leo.common.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Liu
 */
@FeignClient("discount")
public interface DiscountFeignService {

    /**
     * 查询未来三天的秒杀场次和商品信息
     * @return
     */
    @GetMapping("/dicount/getSecondInfo")
    CommonResult getSecondInfo();
}
