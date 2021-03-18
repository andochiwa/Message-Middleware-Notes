package com.github.work.fail;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * @author HAN
 * @version 1.0
 * @create 03-19-3:16
 */
public class work1 {
    private static final String QUEUE_NAME = "queue10";

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
        // 公平分发需要设置同一时刻服务器只会发送一条消息给消费者
        channel.basicQos(1);
        // 4.接受消息  公平分发把第二个参数改成false手动应答
        channel.basicConsume(QUEUE_NAME, false, new DeliverCallback() {
            @SneakyThrows
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                System.out.println("收到消息：" + new String(delivery.getBody()));
                Thread.sleep(200);
                // 公平模式还需要手动接收
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
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
