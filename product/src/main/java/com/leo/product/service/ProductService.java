package com.leo.product.service;

import com.leo.product.entity.Product;

import java.math.BigDecimal;

/**
 * @author Liu
 */
public interface ProductService {
    Product getProductDetails(String id);

    BigDecimal getProductPrice(String id);

    Integer addProduct(Product product);
}
