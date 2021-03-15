package com.github.consumer;

import lombok.SneakyThrows;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

/**
 * @author HAN
 * @version 1.0
 * @create 03-16-4:15
 */
@Component
public class QueueConsumer {

    @JmsListener(destination = "${myQueue}")
    @SneakyThrows
    public void receive(TextMessage textMessage) {
        System.out.println("*******消费者收到消息:" + textMessage.getText());
    }

}
