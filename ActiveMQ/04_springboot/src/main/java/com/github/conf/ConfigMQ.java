package com.github.conf;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.Queue;

/**
 * @author HAN
 * @version 1.0
 * @create 03-16-3:40
 */
@Configuration
@EnableJms // 开启jms适配注解
@EnableScheduling // 激活定时功能
public class ConfigMQ {

    @Value("${myQueue}")
    private String myQueue;

    @Bean
    public Queue queue() {
        return new ActiveMQQueue(myQueue);
    }


}
