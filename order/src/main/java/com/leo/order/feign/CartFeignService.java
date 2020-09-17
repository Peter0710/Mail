package com.leo.order.feign;

import com.leo.order.entity.CartProduct;
import com.leo.order.entity.OrderProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Liu
 */
@FeignClient("cart")
public interface CartFeignService {

    /**
     * 获取购物车中选中的商品的信息，商品价格应该从数据库中重新查询
     * @param uid
     * @return
     */
    @GetMapping("/cart/getChecked")
    List<CartProduct>  getCheckedProduct(@RequestParam("uid") String uid);
}
