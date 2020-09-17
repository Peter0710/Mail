package com.leo.order.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 远程调用购物车模块返回的实体类
 * @author Liu
 */
public class CartProduct {

    private Long id;
    private Boolean checked;
    private String name;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 计算当前购物项的总价，使用bigdecimal计算价格，bigdecimal中应该使用string类型（effective java 中介绍）
     * @return
     */
    public BigDecimal getTotalPrice() {
        return this.price.multiply(new BigDecimal(""+this.count));
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
