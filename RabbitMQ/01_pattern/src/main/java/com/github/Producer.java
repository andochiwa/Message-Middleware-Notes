package com.github;

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

        // 5.准备交换机，路由key，类型
        String exchangeName = "direct_message_exchange";
        String routeKey = "order";
        // 交换机类型 direct/topic/fanout/headers
        String exchangeType = "direct";

        // 6.声明交换机，第三个参数为是否持久化，即交换机会不会随服务器重启而造成丢失
        channel.exchangeDeclare(exchangeName, exchangeType, true);

        // 7. 声明队列
        channel.queueDeclare("queue5", true, false, false, null);
        channel.queueDeclare("queue6", true, false, false, null);
        channel.queueDeclare("queue7", true, false, false, null);
        channel.queueDeclare("queue8", true, false, false, null);
        // 8.绑定队列
        channel.queueBind("queue5", exchangeName, "order");
        channel.queueBind("queue6", exchangeName, "course");
        channel.queueBind("queue7", exchangeName, "order");
        channel.queueBind("queue8", exchangeName, "course");

        // 9.发送消息
        // 参数：1.交换机 2.队列名/路由key 3.消息的状态控制 4.消息主题
        channel.basicPublish(exchangeName, routeKey, null, message.getBytes());
        System.out.println("发送成功");
        // 10.关闭所有资源
        channel.close();
        connection.close();
    }
}
