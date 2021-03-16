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

    private static final String ACTIVEMQ_URL = "nio://localhost:61608";
    private static final String QUEUE_NAME = "jdbc";

    @SneakyThrows
    public static void main(String[] args){
        System.out.println("2号消费者");
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageConsumer consumer = session.createConsumer(queue);
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
