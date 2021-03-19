package com.github.service;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author HAN
 * @version 1.0
 * @create 03-20-0:14
 */
@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void makeOrderTTL() {
        String orderId = UUID.randomUUID().toString();
        System.out.println("订单生产成功" + orderId);

        // 给消息设置过期时间
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setExpiration("50000");
            return message;
        };
        rabbitTemplate.convertAndSend("ttl_direct_exchange", "ttl", orderId, messagePostProcessor);
    }

}
