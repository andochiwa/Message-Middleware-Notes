package com.github.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author HAN
 * @version 1.0
 * @create 03-19-7:28
 */
//@Service
@RabbitListener(queues = "email.fanout.queue") // 队列名字
public class EmailFanoutConsumer {

    // 告知接受的落脚点
    @RabbitHandler
    public void receiveMessage(String message) {
        System.out.println("email接收到订单消息=>" + message);
    }

}
