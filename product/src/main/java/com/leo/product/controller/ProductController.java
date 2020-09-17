package com.leo.product.controller;

import com.leo.common.common.CommonResult;
import com.leo.product.entity.Product;
import com.leo.product.service.ProductService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author Liu
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/getProductDetail/{id}")
    public CommonResult getProductDetail(@PathVariable String id){
        Product product = productService.getProductDetails(id);
        return CommonResult.ok().set("data", product);
    }

    @GetMapping("/getProductPrice/{id}")
    public CommonResult getProductPrice(@PathVariable String id){
        BigDecimal price = productService.getProductPrice(id);
        return CommonResult.ok().set("data", price);
    }

    @PostMapping("/add")
    public CommonResult addProduct(@RequestBody Product product){
        Integer i = productService.addProduct(product);
        return CommonResult.ok();
    }
}
