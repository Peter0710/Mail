package com.leo.ware.service;

import com.leo.ware.entity.Order;
import com.leo.ware.entity.OrderProduct;
import com.leo.ware.entity.WareProduct;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Liu
 */
public interface WareProductService {

    Boolean lockProduct(Order order) throws Exception;

    Integer addStock(WareProduct wareProduct);

    Integer updateStock(WareProduct wareProduct);

    Boolean releaseStock(String orderId);
}
