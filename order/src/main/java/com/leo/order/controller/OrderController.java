package com.leo.order.controller;

import com.leo.common.common.CommonResult;
import com.leo.order.entity.OrderResponseVo;
import com.leo.order.entity.OrderSubmitVo;
import com.leo.order.entity.OrderVo;
import com.leo.order.service.OrderConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author Liu
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderConfirmService orderService;

    /**
     * 确认订单，点击购买后显示的页面
     * @param uid
     * @return
     */
    @GetMapping("/getOder")
    public CommonResult getOrder(@RequestParam("uid") String uid){
        OrderVo orderVo = orderService.getOrder(uid);
        return CommonResult.ok().set("orderVo", orderVo);
    }

    /**
     * 提交订单 然后跳转到付款页
     * @param OrderSubmitVo
     * @return
     */
    @PostMapping("/submitOrder")
    public CommonResult submitOrder(@RequestBody OrderSubmitVo OrderSubmitVo){
        OrderResponseVo orderResponse = orderService.submitOrder(OrderSubmitVo);
        if (orderResponse.getCode() != 200) {
            return CommonResult.error();
        }
        return CommonResult.ok().set("data", orderResponse);
    }

    @GetMapping("/getOrderStatue/{orderId}")
    public CommonResult getOrderStatue(@PathVariable String orderId){
        Integer result = orderService.getOrderSttues(orderId);
        return CommonResult.ok().set("data", result);
    }

}
