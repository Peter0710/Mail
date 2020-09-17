package com.leo.order.feign;

import com.leo.common.common.CommonResult;
import com.leo.order.entity.Order;
import com.leo.order.entity.OrderProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Liu
 */
@FeignClient("ware")
public interface WareFeignService {

    @PostMapping("/ware/lock")
    CommonResult lockProduct(@RequestBody Order order);
}
