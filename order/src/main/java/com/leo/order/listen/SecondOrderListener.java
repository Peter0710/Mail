package com.leo.order.listen;

import com.leo.common.entity.SecondOrder;
import com.leo.order.entity.Order;
import com.leo.order.service.SecondOrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author Liu
 */
@RabbitListener(queues = "order.second.order.queue")
public class SecondOrderListener {

    @Autowired
    SecondOrderService secondOrderService;

    @RabbitHandler
    public void listener(SecondOrder secondOrder, Message message, Channel channel) throws IOException {
        try {
            Boolean bool = secondOrderService.creatSecondOrder(secondOrder);
            if (bool){
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
