package com.leo.cart.controller;

import com.leo.cart.entity.Cart;
import com.leo.cart.entity.CartProduct;
import com.leo.cart.service.CartService;
import com.leo.common.common.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Liu
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    /**
     * 查看购物车所有
     * @return
     */
    @GetMapping("/cartItem")
    public CommonResult cartItem(@RequestParam("uid") String uid){
        Cart cart = cartService.getCartItem(uid);
        return CommonResult.ok().set("data", cart);
    }

    /**
     * 添加商品进入购物车
     *
     * @param id 商品对应的id
     * @param amount 商品数量
     * @return
     */
    @GetMapping("/addProduct")
    public CommonResult addProduct(@RequestParam("id") String id,
                                   @RequestParam("amount") Integer amount, @RequestParam("uid") String uid){
        Boolean b = cartService.addProductToCart(id, amount, uid);
        if (!b){
            return CommonResult.error();
        }
        return CommonResult.ok();
    }

    /**
     * 增加商品数量
     * @return
     */
    @GetMapping("/addAmount")
    public CommonResult addAmount(@RequestParam("id") String id, @RequestParam("uid") String uid){
        Boolean bool = cartService.amountChange(id, uid, 1);
        return CommonResult.ok();
    }

    /**
     * 减少商品数量
     * @return
     */
    @GetMapping("/reduceAmount")
    public CommonResult reduceAmount(@RequestParam("id") String id, @RequestParam("uid") String uid){
        Boolean bool = cartService.amountChange(id, uid, -1);
        return CommonResult.ok();
    }

    /**
     * 删除商品
     * @return
     */
    @GetMapping("/deleteProduct")
    public CommonResult deleteProduct(){
        return CommonResult.ok();
    }

    /**
     * 批量删除商品
     * @return
     */
    @GetMapping("/delete")
    public CommonResult deleteMore(){
        return CommonResult.ok();
    }


    @GetMapping("/checked")
    public CommonResult checkedProduct(@RequestParam("id") String id, @RequestParam("uid") String uid, @RequestParam("checked") Integer checked){
        cartService.checkedProduct(id, uid, checked);
        return CommonResult.ok();
    }

    @GetMapping("/getChecked")
    public List<CartProduct> getCheckedProduct(@RequestParam("uid") String uid){
        List<CartProduct> checkedCartItem = cartService.getCheckedCartItem(uid);
        return checkedCartItem;
    }
}
