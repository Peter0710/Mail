package com.leo.cart.feign;

import com.leo.common.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Liu
 */
@FeignClient(name = "product")
public interface ProductFeignService {

    @GetMapping("/product/getProductDetail/{id}")
    CommonResult getProductDetail(@PathVariable String id);

    @GetMapping("/product/getProductPrice/{id}")
    CommonResult getProductPrice(@PathVariable String id);
}
