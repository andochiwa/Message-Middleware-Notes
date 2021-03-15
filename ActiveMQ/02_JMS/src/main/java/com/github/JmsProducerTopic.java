package com.github;

import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author HAN
 * @version 1.0
 * @create 03-15-5:57
 */
public class JmsProducerTopic {

    private static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "topic02";

    @SneakyThrows
    public static void main(String[] args){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        MessageProducer producer = session.createProducer(topic);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        // 设置持久化主题，start要放在这
        connection.start();

        for (int i = 0; i < 3; i++) {
            TextMessage textMessage = session.createTextMessage("msg---" + (i + 1));
            producer.send(textMessage);
        }
        producer.close();
        session.close();
        connection.close();
        System.out.println("*******发布成功");
    }

}
