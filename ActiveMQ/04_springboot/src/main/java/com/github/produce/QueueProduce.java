package com.github.produce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.UUID;

/**
 * @author HAN
 * @version 1.0
 * @create 03-16-3:50
 */
@Component
public class QueueProduce {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    public void produceMessage() {
        jmsMessagingTemplate.convertAndSend(queue, "****:" +
                UUID.randomUUID().toString().substring(0, 6));
    }

    @Scheduled(fixedDelay = 3000)
    public void produceMegScheduled() {
        jmsMessagingTemplate.convertAndSend(queue, "****scheduled:" +
                UUID.randomUUID().toString().substring(0, 6));
        System.out.println("======send access==========");
    }

}
