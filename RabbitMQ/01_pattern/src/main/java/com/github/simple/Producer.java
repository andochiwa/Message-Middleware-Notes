package com.github.simple;

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
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        // 2.创建连接Connection
        Connection connection = connectionFactory.newConnection("producer");
        // 3.通过连接获取通道Channel
        Channel channel = connection.createChannel();
        // 4.通过通道创建交换机，声明队列，绑定关系，路由key，发送消息和接受消息
        // 参数：1.名字 2.是否持久化 3.排他性(是否独占) 4.是否自动删除 5.携带一些附属参数
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 5.准备消息内容
        String message = "hello world";
        // 6.发送消息给队列queue
        // 参数：1.交换机 2.队列名 3.消息的状态控制 4.消息主题
        channel.basicPublish("", QUEUE_NAME,null, message.getBytes());
        // 7.关闭所有资源
        channel.close();
        connection.close();
    }
}
