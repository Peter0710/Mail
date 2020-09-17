package com.leo.cart.service;

import com.leo.cart.entity.Cart;
import com.leo.cart.entity.CartProduct;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liu
 */

public interface CartService {

    Boolean addProductToCart(String id, Integer amount, String uid);

    Cart getCartItem(String uid);

    Boolean amountChange(String id, String uid, Integer number);

    void ClearCart(String id);

    void checkedProduct(String id, String uid, Integer checked);

    List<CartProduct> getCheckedCartItem(String uid);
}
