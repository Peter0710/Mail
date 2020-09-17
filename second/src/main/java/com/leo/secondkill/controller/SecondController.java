package com.leo.secondkill.controller;

import com.leo.common.common.CommonResult;
import com.leo.secondkill.entity.SecondKill;
import com.leo.secondkill.entity.SecondProduct;
import com.leo.secondkill.service.SecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Liu
 */
@RestController
@RequestMapping("/second")
public class SecondController {

    @Autowired
    SecondService secondService;

    @GetMapping("/getSecondInfo")
    public CommonResult getSecondInfo(){
        List<SecondProduct> secondInfo = secondService.getSecondInfo();
        return CommonResult.ok().set("data", secondInfo);
    }

    @GetMapping("/second")
    public CommonResult secondKill(@RequestParam("uid") String uid,
                                   @RequestParam("token") String token,
                                   @RequestParam("num") Integer num,
                                   @RequestParam("Kill_id") String kill_id){
        String orderId = secondService.secondKill(uid, token, num, kill_id);
        return CommonResult.ok().set("data", orderId);
    }

}
