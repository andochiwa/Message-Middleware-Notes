package com.github;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author HAN
 * @version 1.0
 * @create 03-15-5:05
 */
public class JsmConsumerTopic {

    private static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "topic02";

    @SneakyThrows
    public static void main(String[] args){
        System.out.println("1号消费者");
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        // 表面订阅的用户是谁
        connection.setClientID("1号消费者");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        // 持久化订阅者设置
        TopicSubscriber subscriber = session.createDurableSubscriber(topic, "备注....");

        connection.start();

//        Message message = subscriber.receive();
//        if (null != message) {
//            TextMessage textMessage = (TextMessage) message;
//            System.out.println("收到的持久化topic:" + textMessage.getText());
//            subscriber.receive(5000L);
//        }
        subscriber.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("收到的持久化topic:" + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        System.in.read();

        session.close();
        connection.close();
    }

}
