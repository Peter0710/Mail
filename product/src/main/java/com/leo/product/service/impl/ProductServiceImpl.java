package com.leo.product.service.impl;

import com.leo.product.dao.ProductDao;
import com.leo.product.entity.Product;
import com.leo.product.service.ProductService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Liu
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;


    @Override
    public Product getProductDetails(String id) {
        Product product = productDao.selectById(id);
        return product;
    }

    @Override
    public BigDecimal getProductPrice(String id) {
        return getProductDetails(id).getPrice();
    }

    @Override
    public Integer addProduct(Product product) {
        return productDao.insert(product);
    }
}
