package com.leo.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leo.common.common.CommonResult;
import com.leo.order.constant.OrderConstant;
import com.leo.order.dao.OrderDao;
import com.leo.order.dao.OrderProductDao;
import com.leo.order.entity.*;
import com.leo.order.feign.CartFeignService;
import com.leo.order.feign.MemberFeignService;
import com.leo.order.feign.ProductFeignService;
import com.leo.order.feign.WareFeignService;
import com.leo.order.service.OrderConfirmService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author Liu
 */
@Service
public class OrderConfirmServiceImpl implements OrderConfirmService {

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    WareFeignService wareFeignService;

    @Qualifier("mainThreadPool")
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderProductDao orderProductDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 查询订单相关的内容
     * @param uid
     * @return
     */
    @Override
    public OrderVo getOrder(String uid) {
        OrderVo orderVo = new OrderVo();
        //查询用户默认的地址
        CompletableFuture<Void> preferAddress1 = CompletableFuture.runAsync(() -> {
            CommonResult preferAddress = memberFeignService.getPreferAddress(uid);
            Address preferAddressData = preferAddress.getData("preferAddress", new TypeReference<Address>() {
            });
            orderVo.setAddress(preferAddressData);
        }, threadPoolExecutor);

        //查询选中的购物项
        //计算商品总额
        //TODO 查询远程库存信息,检测是否有库存
        CompletableFuture<Void> cartProductFuture = CompletableFuture.runAsync(() -> {
            List<CartProduct> checkedProduct = cartFeignService.getCheckedProduct(uid);
            orderVo.setCartProductList(checkedProduct);
        }, threadPoolExecutor);

        //TODO 查询一些用户的优惠券和积分信息

        //幂等性操作，给订单生成一个token
        String token = UUID.randomUUID().toString().replace("-", "");
        //存入redis中方便校验
        redisTemplate.opsForValue().set(OrderConstant.ORDER_TOKEN + uid, token, 30, TimeUnit.MINUTES);
        System.out.println(token);
        orderVo.setOrderToken(token);

        //所有都完成才可以
        CompletableFuture.allOf(preferAddress1, cartProductFuture);

        return orderVo;
    }

    /**
     * 提交订单
     * @param orderSubmitVo
     * @return
     */
    @Override
    public OrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo) {
        OrderResponseVo orderResponseVo = new OrderResponseVo();
        String token = orderSubmitVo.getToken();
        Long uid = orderSubmitVo.getUid();
        //验证令牌,并删除令牌，应该保证原子性
        //TODO 使用lua脚本完成原子性的操作
        String redisToken = (String) redisTemplate.opsForValue().get(OrderConstant.ORDER_TOKEN + uid);
        if (redisToken != null && token.equals(redisToken)){
            redisTemplate.delete(OrderConstant.ORDER_TOKEN + uid);
            String orderId = IdWorker.getTimeId();
            //生成购物项的实体类，并保存到数据库
            List<CartProduct> checkedProduct = cartFeignService.getCheckedProduct(uid.toString());
            //验证成功，创建订单
            Order order = createOrder(orderSubmitVo, checkedProduct, orderId);
            //遍历每个数据
            List<OrderProduct> orderProducts = getOrderItem(checkedProduct, orderId);
            //将数据信息插入数据库
            orderDao.insert(order);

            for (OrderProduct product : orderProducts){
                orderProductDao.insert(product);
            }

            //锁定库存，远程调用仓库模块锁库存
            CommonResult result = wareFeignService.lockProduct(order);
            order.setProducts(orderProducts);
            //使用基于消息的最终一致性，
            if (result.getCode() == CommonResult.SUCCESS_CODE){
                //锁定成功
                rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order",order);
                orderResponseVo.setCode(200);
                orderResponseVo.setOrder(order);
            }else {
                //锁定失败
                orderResponseVo.setCode(500);
            }
            //将订单信息发给rabbitmq的交换机，
            return orderResponseVo;
        }else {
            orderResponseVo.setCode(500);
            return orderResponseVo;
        }
    }

    /**
     * 查询订单状态，0为未支付， 1 为支付，2 为取消，3，为完成
     * @param orderId
     * @return
     */
    @Override
    public Integer getOrderSttues(String orderId) {
        Order order = orderDao.selectOne(new QueryWrapper<Order>().eq("order_id", orderId));
        Integer pay = order.getPay();
        return pay;
    }

    @Override
    public void closeOrder(Order order) {
        Order orderOne = orderDao.selectOne(new QueryWrapper<Order>().eq("order_id", order.getOrderId()));
        Integer pay = orderOne.getPay();
        if (pay == 0){
            //未支付, 修改订单状态为2 ，为取消订单状态
            orderDao.update(orderOne, new QueryWrapper<Order>().eq("pay", 2));
            rabbitTemplate.convertAndSend("order-event-exchange", "order.release.stock", orderOne);
        }
    }

    /**
     * 生成订单内容
     * @param orderSubmitVo
     * @return
     */
    private Order createOrder(OrderSubmitVo orderSubmitVo, List<CartProduct> checkedProduct, String orderId) {
        Order order = new Order();
        Long uid = orderSubmitVo.getUid();

        CommonResult preferAddress = memberFeignService.getAddressById(orderSubmitVo.getAddressId().toString());
        Address address = preferAddress.getData("preferAddress", new TypeReference<Address>() {});
        CommonResult memberInfoById = memberFeignService.getMemberInfoById(uid.toString());
        Member data = memberInfoById.getData("data", new TypeReference<Member>() {});
        //设置未支付
        order.setPay(0);
        //设置订单创建时间
        order.setTime(new Date());
        //生成订单号
        order.setOrderId(orderId);
        //设置收货地址
        order.setAddress(address.getAddress());
        //设置手机号
        order.setPhone(address.getPhone());
        //设置收货人的名字
        order.setName(data.getName());
        //设置用户id
        order.setUid(uid);
        //设置账单金额
        //获取订单项数据，并将数据存储到数据库中
        BigDecimal totalPrice = new BigDecimal("0");
        for (CartProduct product : checkedProduct){
            totalPrice = totalPrice.add(product.getTotalPrice());
        }
        order.setPrice(totalPrice);
        return order;
    }

    /**
     * 获取订单中的商品，并包装成可以放入数据库的实体类
     * @param checkedProduct
     * @param orderId
     */
    private List<OrderProduct> getOrderItem(List<CartProduct> checkedProduct, String orderId) {
        List<OrderProduct> orderProductList = checkedProduct.stream().map(item -> {
            OrderProduct product = new OrderProduct();
            //设置商品数量
            product.setAmount(item.getCount());
            //设置订单号
            product.setOrderId(orderId);
            //设置商品单价
            product.setPrice(item.getPrice());
            //设置商品名称
            product.setProductName(item.getName());
            //设置商品id
            product.setProductId(item.getId());
            //设置订单中商品信息
            CommonResult productDetail = productFeignService.getProductDetail(item.getId().toString());
            Product detailData = productDetail.getData("data", new TypeReference<Product>() {
            });
            product.setProductInfo(detailData.getIntro());
            //设置商品总价
            product.setTotalPrice(item.getTotalPrice());
            return product;
        }).collect(Collectors.toList());

        return orderProductList;
    }
}
