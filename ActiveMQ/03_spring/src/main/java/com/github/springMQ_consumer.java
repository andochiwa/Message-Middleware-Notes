package com.github;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Message;

/**
 * @author HAN
 * @version 1.0
 * @create 03-16-2:33
 */
@Service
public class springMQ_consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static void main(String[] args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        springMQ_consumer consumer = context.getBean(springMQ_consumer.class);
        String convert = (String) consumer.jmsTemplate.receiveAndConvert();

        System.out.println("接收到消息:" + convert);
    }

}
