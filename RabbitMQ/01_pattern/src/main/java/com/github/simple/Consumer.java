package com.github.simple;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * @author HAN
 * @version 1.0
 * @create 03-19-3:16
 */
public class Consumer {
    private static final String QUEUE_NAME = "queue01";

    @SneakyThrows
    public static void main(String[] args){
        // 1.创建连接工程，设置ip和port还有账号密码
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        // 2.创建连接Connection
        Connection connection = connectionFactory.newConnection("producer");
        // 3.通过连接获取通道Channel
        Channel channel = connection.createChannel();
        // 4.接受消息
        channel.basicConsume(QUEUE_NAME, true, new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                System.out.println("收到消息：" + new String(delivery.getBody()));
            }
        }, new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {
                System.out.println("接收失败" + s);
            }
        });
        System.in.read();
        // 5.关闭所有资源
        channel.close();
        connection.close();
    }
}
