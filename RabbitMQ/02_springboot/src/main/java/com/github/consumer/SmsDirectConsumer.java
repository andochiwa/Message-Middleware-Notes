package com.github.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author HAN
 * @version 1.0
 * @create 03-19-7:28
 */
@Service
@RabbitListener(queues = "sms.direct.queue") // 队列名字
public class SmsDirectConsumer {

    // 告知接受的落脚点
    @RabbitHandler
    public void receiveMessage(String message) {
        System.out.println("sms接收到订单消息=>" + message);
    }

}
