package com.github;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * @author HAN
 * @version 1.0
 * @create 03-16-2:34
 */
@Service
public class springMQ_producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        springMQ_producer producer = context.getBean(springMQ_producer.class);
        producer.jmsTemplate.send(session -> {
            return session.createTextMessage("spring+ActiveMQ");
        });
        System.out.println("发送完成");
    }
}
