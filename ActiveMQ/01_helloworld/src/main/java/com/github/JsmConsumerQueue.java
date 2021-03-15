package com.github;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author HAN
 * @version 1.0
 * @create 03-15-5:05
 */
public class JsmConsumerQueue {

    private static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "queue01";

    @SneakyThrows
    public static void main(String[] args){
        System.out.println("2号消费者");
        // 1.创建连接工厂，用户名和密码为默认不用配置
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 2.通过连接工厂获得connection并启动访问
        Connection connection = factory.createConnection();
        connection.start();
        // 3.创建会话，两个参数，事务、签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 4.创建目的地
        Queue queue = session.createQueue(QUEUE_NAME);
        // 5.创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        // 6.接收消息

        // 第一种方法， receive()
//        while (true) {
//            TextMessage message = (TextMessage) consumer.receive(4000L);
//            if (null != message) {
//                System.out.println("接收到消息" + message.getText());
//            } else {
//                break;
//            }
//        }

        // 第二种方法，设置监听器
        consumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("接收到Text消息" + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read();

        consumer.close();
        session.close();
        connection.close();
    }

}
