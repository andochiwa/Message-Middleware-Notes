package com.github;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author HAN
 * @version 1.0
 * @create 03-15-4:39
 */
public class JmsProducerQueue {

    private static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "queue01";

    @SneakyThrows
    public static void main(String[] args){
        // 1.创建连接工厂，用户名和密码为默认不用配置
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 2.通过连接工厂获得connection并启动访问
        Connection connection = factory.createConnection();
        connection.start();
        // 3.创建会话，两个参数，事务、签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4.创建目的地
        Queue queue = session.createQueue(QUEUE_NAME);
        // 5.创建消息生产者
        MessageProducer producer = session.createProducer(queue);
        // 6.发送消息
        for (int i = 0; i < 3; i++) {
            // 创建消息
            TextMessage textMessage = session.createTextMessage("msg---" + i);
            // 发布
            producer.send(textMessage);
        }
        // 7.关闭资源
        producer.close();
        session.close();
        connection.close();
        System.out.println("*******发布成功");
    }

}
