package com.github.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

/**
 * @author HAN
 * @version 1.0
 * @create 03-19-3:16
 */
public class Producer {
    private static final String QUEUE_NAME = "queue01";

    @SneakyThrows
    public static void main(String[] args){
        // 1.创建连接工程，设置ip和port还有账号密码
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        // 2.创建连接Connection
        Connection connection = connectionFactory.newConnection("producer");
        // 3.通过连接获取通道Channel
        Channel channel = connection.createChannel();
        // 4.准备消息内容
        String message = "hello world";
        // 5准备交换机，路由key，类型
        String routing = "topic-exchange";
        String routeKey = "com.order.test1.user.test2.test3";
        String type = "topic";
        // 6.发送消息给队列queue
        // 参数：1.交换机 2.队列名/路由key 3.消息的状态控制 4.消息主题
        channel.basicPublish(routing, routeKey, null, message.getBytes());
        System.out.println("发送成功");
        // 7.关闭所有资源
        channel.close();
        connection.close();
    }
}
