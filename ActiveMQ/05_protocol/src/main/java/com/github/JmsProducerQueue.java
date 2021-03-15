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

    private static final String ACTIVEMQ_URL = "nio://localhost:61608";
    private static final String QUEUE_NAME = "queue01";

    @SneakyThrows
    public static void main(String[] args){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageProducer producer = session.createProducer(queue);
        for (int i = 0; i < 3; i++) {
            TextMessage textMessage = session.createTextMessage("msg---" + i);
            producer.send(textMessage);
        }
        producer.close();
        session.close();
        connection.close();
        System.out.println("*******发布成功");
    }

}
