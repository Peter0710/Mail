package com.leo.ware.controller;

import com.leo.common.common.CommonResult;
import com.leo.ware.entity.Order;
import com.leo.ware.entity.OrderProduct;
import com.leo.ware.entity.WareProduct;
import com.leo.ware.service.WareProductService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Liu
 */
@RestController
@RequestMapping("/ware")
public class WareProductController {

    @Autowired
    WareProductService wareProductService;

    @GetMapping("/lock")
    public CommonResult lockProduct(@RequestBody Order order) throws Exception {
        Boolean result = wareProductService.lockProduct(order);
        if (!result){
            return CommonResult.error();
        }
        return CommonResult.ok();
    }

    @PostMapping("/addStock")
    public CommonResult addStock(@RequestBody WareProduct wareProduct){
        Integer result = wareProductService.addStock(wareProduct);
        return CommonResult.ok();
    }

    @PostMapping("/updateStock")
    public CommonResult updateStock(@RequestBody WareProduct wareProduct){
        Integer result = wareProductService.updateStock(wareProduct);
        return CommonResult.ok();
    }

}
