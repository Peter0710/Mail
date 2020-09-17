package com.leo.order.feign;

import com.leo.common.common.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Liu
 */
@FeignClient("member")
public interface MemberFeignService {

    @GetMapping("/address/getPreferAddress")
    CommonResult getPreferAddress(@RequestParam("uid") String uid);

    @GetMapping("/address/getAddressByid")
    CommonResult getAddressById(@RequestParam("id") String id);

    @GetMapping("/member/getMemberInfo/{id}")
    CommonResult getMemberInfoById(@PathVariable String id);
}
