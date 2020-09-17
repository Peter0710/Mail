package com.leo.cart.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Liu
 */
public class Cart {

    private List<CartProduct> products;

    private Integer totalCount;

    private Integer typeCount;

    private BigDecimal totalPrice;

    public List<CartProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CartProduct> products) {
        this.products = products;
    }

    public Integer getTotalCount() {
        int totalCount = 0;
        if (products != null && products.size() > 0){
            for (CartProduct cartProduct : products){
                totalCount += cartProduct.getCount();
            }
        }
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTypeCount() {
        int typeCount = 0;
        if (products != null && products.size() > 0){
            for (CartProduct cartProduct : products){
                typeCount++;
            }
        }
        return typeCount;
    }

    public void setTypeCount(Integer typeCount) {
        this.typeCount = typeCount;
    }

    /**
     * 遍历计算购物项的总价
     * @return
     */
    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal("0");
        if (products != null && products.size() > 0){
            for (CartProduct cartProduct : products){
                if (cartProduct.getChecked()){
                    BigDecimal price = cartProduct.getTotalPrice();
                    totalPrice = totalPrice.add(price);
                }
            }
        }
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
