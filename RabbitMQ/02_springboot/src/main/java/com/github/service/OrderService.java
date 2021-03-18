package com.github.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author HAN
 * @version 1.0
 * @create 03-19-7:09
 */
@Service
public class OrderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 模拟用户下单
    public void makeFanoutOrder(String userId, String productId, int num) {
        // 1.根据商品id查询库存是否充足
        // 2.保存订单
        String orderId = UUID.randomUUID().toString();
        System.out.println("订单生成成功：" + orderId);
        // 通过MQ来完成消息的分发
        String exchangeName = "fanout_order_exchange";
        String routingKey = "";
        rabbitTemplate.convertAndSend(exchangeName, routingKey, orderId);
    }

    public void makeDirectOrder(String userId, String productId, int num) {
        // 1.根据商品id查询库存是否充足
        // 2.保存订单
        String orderId = UUID.randomUUID().toString();
        System.out.println("订单生成成功：" + orderId);
        // 通过MQ来完成消息的分发
        String exchangeName = "direct_order_exchange";
        rabbitTemplate.convertAndSend(exchangeName, "email", orderId);
        rabbitTemplate.convertAndSend(exchangeName, "sms", orderId);
    }

}
