package com.leo.ware.feign;

import com.leo.common.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Liu
 */
@FeignClient("order")
public interface OrderFeignService {

    @GetMapping("/order/getOrderStatue/{orderId}")
    CommonResult getOrderStatue(@PathVariable String orderId);
}
