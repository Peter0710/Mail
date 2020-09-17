package com.leo.order.listen;

import com.leo.order.entity.Order;
import com.leo.order.service.OrderConfirmService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author Liu
 */
@RabbitListener(queues = "order.release.order.queue")
public class OrderListener {

    @Autowired
    OrderConfirmService orderConfirmService;

    /**
     * 监听队列，如果订单未支付，则关闭订单
     * @param order
     */
    @RabbitHandler
    public void listener(Order order, Message message, Channel channel) throws IOException {
        try {
            orderConfirmService.closeOrder(order);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
