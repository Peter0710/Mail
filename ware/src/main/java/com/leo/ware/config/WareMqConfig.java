package com.leo.ware.config;

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
public class WareMqConfig {

    /**
     * 一开始库存生成存放的队列, 死信队列（DLX）
     * @return
     */
    @Bean
    public Queue wareDeadQueue(){
        //队列的名字，队列是否持久化，是否自动删除，是否自动删除，参数
        Map<String, Object> param = new HashMap<>();
        param.put("x-dead-letter-exchange", "ware-event-exchange");
        param.put("x-dead-letter-routing-key","ware.release.ware");
        param.put("x-message-ttl", 80000);
        return new Queue("ware.delay.queue",true,false,false, param);
    }

    /**
     * 一开始的队列信息30分钟后转发到的队列
     * @return
     */
    @Bean
    public Queue wareQueue(){
        return new Queue("ware.release.ware.queue",true,false,false, null);
    }

    /**
     * 交换机，负责俩次消息的发送
     * @return
     */
    @Bean
    public Exchange wareExchange(){
        //交换机名字，是否持久化，是否自动删除，参数
        return new TopicExchange("ware-event-exchange", true, false);
    }

    /**
     * 绑定开始存放消息的死信队列和交换机
     * @return
     */
    @Bean
    public Binding wareDeadBinding(){
        //目的地，目的地类型 交换机， 路由键, 参数
        return new Binding("ware.delay.queue", Binding.DestinationType.QUEUE,
                "ware-event-exchange", "ware.create.ware", null);
    }

    /**
     * 绑定第二个队列和交换机
     * @return
     */
    @Bean
    public Binding wareBinding(){
        return new Binding("ware.release.ware.queue", Binding.DestinationType.QUEUE,
                "ware-event-exchange", "ware.release.ware", null);
    }
}
