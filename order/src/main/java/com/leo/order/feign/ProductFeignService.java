package com.leo.order.feign;

import com.leo.common.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Liu
 */
@FeignClient("product")
public interface ProductFeignService {

    @GetMapping("/getProductDetail/{id}")
    CommonResult getProductDetail(@PathVariable String id);
}
