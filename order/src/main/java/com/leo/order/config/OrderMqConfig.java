package com.leo.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liu
 */
@Configuration
public class OrderMqConfig {

    /**
     * 一开始订单生成存放的队列, 死信队列（DLX）
     * @return
     */
    @Bean
    public Queue orderDeadQueue(){
        //队列的名字，队列是否持久化，是否自动删除，是否自动删除，参数
        Map<String, Object> param = new HashMap<>();
        param.put("x-dead-letter-exchange", "order-event-exchange");
        param.put("x-dead-letter-routing-key","order.release.order");
        param.put("x-message-ttl", 60000);
        return new Queue("order.delay.queue",true,false,false, param);
    }

    /**
     * 一开始的队列信息30分钟后转发到的队列
     * 监听这个队列，获取出的订单消息，检查订单是否支付，如果未支付，说明订单超时未支付，应当关闭订单。
     * 发送给库存模块的解锁库存的队列
     * @return
     */
    @Bean
    public Queue orderQueue(){
        return new Queue("order.release.order.queue",true,false,false, null);
    }

    /**
     * 交换机，负责俩次消息的发送
     * @return
     */
    @Bean
    public Exchange orderExchange(){
        //交换机名字，是否持久化，是否自动删除，参数
        return new TopicExchange("order-event-exchange", true, false);
    }

    /**
     * 绑定开始存放消息的死信队列和交换机
     * @return
     */
    @Bean
    public Binding orderDeadBinding(){
        //目的地，目的地类型 交换机， 路由键, 参数
        return new Binding("order.delay.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.create.order", null);
    }

    /**
     * 绑定第二个队列和交换机
     * @return
     */
    @Bean
    public Binding orderBinding(){
        return new Binding("order.release.order.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.release.order", null);
    }

    /**
     * 秒杀队列，监听这个队列，做到削峰处理，后台订单模块，监听队列，创建订单（库存锁定），
     * 然后再走上面的发送到死信队列---关单队列---监听（是否付款，false关单，解锁库存）
     * true,订单完成
     * @return
     */
    @Bean
    public Queue secondQueue(){
        return new Queue("order.second.order.queue", true, false,false, null);
    }

    /**
     * 秒杀订单队列绑定
     * @return
     */
    @Bean
    public Binding secondBinding(){
        return new Binding("order.second.order.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.second.order", null);
    }

    /**
     * 订单超过30分钟没支付后，发送给库存模块，要求解库存
     * @return
     */
    public Binding releaseStock(){
        return new Binding("ware.release.ware.queue", Binding.DestinationType.QUEUE,
                "order-event-exchange", "order.release.stock", null);
    }
}
