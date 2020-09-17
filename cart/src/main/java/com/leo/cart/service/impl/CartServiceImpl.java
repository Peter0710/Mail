package com.leo.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.leo.cart.entity.Cart;
import com.leo.cart.entity.CartProduct;
import com.leo.cart.entity.Product;
import com.leo.cart.entity.UserInfo;
import com.leo.cart.feign.ProductFeignService;
import com.leo.cart.interceptor.CartInterceptor;
import com.leo.cart.service.CartService;
import com.leo.common.common.CommonResult;
import com.leo.common.constant.CartConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Liu
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public Boolean addProductToCart(String id, Integer amount, String uid) {
        //判断用户是登录用户还是临时用户 使用拦截器和threadLocal
        String key = null;
//        UserInfo userInfo = CartInterceptor.THREAD_LOCAL.get();
//        if (userInfo.getId() != null){
//            key = CartConstant.USER_CART + userInfo.getId();
//        }else {
//            key = CartConstant.USER_CART + userInfo.getTemp();
//        }

        key = CartConstant.USER_CART + uid;
        //判断购物车是否有这个商品，如果没有进行查询，
        //如果已经有了，则增加数量就可以了
        BoundHashOperations cartOperation = redisTemplate.boundHashOps(key);
        String str = (String)cartOperation.get(id);
        CartProduct cartProduct1 = JSON.parseObject(str, CartProduct.class);
        if (cartProduct1 != null){
            Integer count = cartProduct1.getCount();
            cartProduct1.setCount(count + amount);
            cartOperation.put(cartProduct1.getId(), JSON.toJSONString(cartProduct1));
            return true;
        }
        //远程调用 根据id查找商品的详细信息，
        CommonResult result = productFeignService.getProductDetail(id);
        Product product1 = result.getData("data", new TypeReference<Product>(){});

        //将信息封装到cartProduct中
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCount(amount);
        cartProduct.setChecked(true);
        cartProduct.setId(product1.getId());
        cartProduct.setName(product1.getName());
        cartProduct.setPrice(product1.getPrice());

        cartOperation.put(cartProduct.getId(), JSON.toJSONString(cartProduct));
        return true;

    }

    /**
     * 获取购物车所有的产品
     * @return
     */
    @Override
    public Cart getCartItem(String uid) {
        //查询购物车时，应该当考虑用户登录后，临时用户数据和登录用户数据的合并
        //TODO 当存在临时购物车时，查看购物车，需要合并临时购物车和用户购物车
        //当用户登录过
        List<CartProduct> products = getProducts(uid);
        Cart cart = new Cart();
        cart.setProducts(products);
        return cart;
    }

    private List<CartProduct> getProducts(String uid) {
        String key = CartConstant.USER_CART + uid;
        BoundHashOperations cartOperation = redisTemplate.boundHashOps(key);
        List<Object> values = cartOperation.values();
        List<CartProduct> products = null;
        if (values != null && values.size() > 0){
            products = values.stream().map(object -> {
                String str = (String) object;
                CartProduct cartProduct = JSON.parseObject(str, CartProduct.class);
                return cartProduct;
            }).collect(Collectors.toList());
        }
        return products;
    }

    /**
     * 修改商品数量
     * @param id
     * @return
     */
    @Override
    public Boolean amountChange(String id, String uid, Integer number) {
        String key = CartConstant.USER_CART + uid;
        BoundHashOperations cartOperation = redisTemplate.boundHashOps(key);
        String str = (String)cartOperation.get(id);
        CartProduct cartProduct = JSON.parseObject(str, CartProduct.class);
        Integer count = cartProduct.getCount() + number;
        if (count <= 0) {
            return false;
        }
        cartProduct.setCount(count);
        cartOperation.put(cartProduct.getId(), JSON.toJSONString(cartProduct));
        return null;
    }

    /**
     * 清空购物车
     * @param key
     */
    @Override
    public void ClearCart(String key){
        redisTemplate.delete(key);
    }

    /**
     * 修改商品是否选中
     * @param id
     * @param uid
     */
    @Override
    public void checkedProduct(String id, String uid, Integer checked) {
        String key = CartConstant.USER_CART + uid;
        BoundHashOperations cartOperation = redisTemplate.boundHashOps(key);
        String str = (String)cartOperation.get(id);
        CartProduct cartProduct = JSON.parseObject(str, CartProduct.class);
        Boolean check = false;
        if (checked == 1){
            check = true;
        }
        cartProduct.setChecked(check);
        String productString = JSON.toJSONString(cartProduct);
        cartOperation.put(id, productString);
    }

    /**
     * 生成订单时，不能查的时redis购物车的价格，应该查询数据库中的最新价格
     * @param uid
     * @return
     */
    @Override
    public List<CartProduct> getCheckedCartItem(String uid) {
        List<CartProduct> products = getProducts(uid);
        List<CartProduct> checkedProductList = products.stream().filter(item -> item.getChecked())
                .map(item -> {
                    CommonResult productPrice = productFeignService.getProductPrice(item.getId().toString());
                    BigDecimal price = productPrice.getData("data", new TypeReference<BigDecimal>() {
                    });
                    item.setPrice(price);
                    return item;
                }).collect(Collectors.toList());
        return checkedProductList;
    }
}
