package com.leo.order.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单确认页返回的实体类
 * @author Liu
 */

public class OrderVo {

    @Getter
    @Setter
    private Address address;

    @Getter
    @Setter
    private List<CartProduct>  cartProductList;

    //TODO 用户模块可以添加优惠券和淘宝币之类的内容，可以抵消金额的

    private BigDecimal totalPrice;

    private BigDecimal payPrice;

    @Getter
    @Setter
    private String orderToken;

    public BigDecimal getTotalPrice(){
        BigDecimal totalPrice = new BigDecimal("0");
        if (cartProductList != null && cartProductList.size() > 0){
            for (CartProduct product : cartProductList){
                BigDecimal productPrice = product.getPrice().multiply(new BigDecimal("" + product.getCount()));
                totalPrice = totalPrice.add(productPrice);
            }
        }
        return totalPrice;
    }


    public BigDecimal getPayPrice(){
        //TODO 扣减掉一些优惠券和积分
        BigDecimal payPrice = new BigDecimal("0");
        if (cartProductList != null && cartProductList.size() > 0){
            for (CartProduct product : cartProductList){
                BigDecimal productPrice = product.getPrice().multiply(new BigDecimal("" + product.getCount()));
                payPrice = payPrice.add(productPrice);
            }
        }
        return payPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }
}
