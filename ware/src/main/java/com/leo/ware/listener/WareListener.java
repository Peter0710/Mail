package com.leo.ware.listener;

import com.leo.ware.entity.Order;
import com.leo.ware.entity.WareLockOrder;
import com.leo.ware.service.WareProductService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author Liu
 */
@RabbitListener(queues = "ware.release.ware.queue")
public class WareListener {


    @Autowired
    WareProductService wareProductService;

    @Autowired

    @RabbitHandler
    public void release(WareLockOrder wareLockOrder, Message message, Channel channel) throws IOException {
        String orderId = wareLockOrder.getOrderId();
        Boolean bool = wareProductService.releaseStock(orderId);
        if (bool){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        //拒绝后数据重新入队
        channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
    }

    @RabbitHandler
    public void release(Order Order, Message message, Channel channel) throws IOException {
        String orderId = Order.getOrderId();
        Boolean bool = wareProductService.releaseStock(orderId);
        if (bool){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            return;
        }
        //拒绝后数据重新入队
        channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
    }
}
