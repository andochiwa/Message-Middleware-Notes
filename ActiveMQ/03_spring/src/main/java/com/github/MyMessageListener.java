package com.github;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author HAN
 * @version 1.0
 * @create 03-16-2:59
 */
@Component
public class MyMessageListener implements MessageListener {
    @SneakyThrows
    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("接收到消息" + textMessage.getText());
        }
    }
}
