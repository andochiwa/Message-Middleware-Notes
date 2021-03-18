package com.github.conf;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HAN
 * @version 1.0
 * @create 03-19-7:40
 */
@Configuration
public class DirectConfiguration {

    // 声明fanout模式的交换机
    @Bean
    public DirectExchange fanoutExchange() {
        return new DirectExchange("direct_order_exchange", true, false);
    }

    // 声明队列sms, email, wechat
    @Bean
    public Queue smsDirectQueue() {
        return new Queue("sms.direct.queue", true);
    }

    @Bean
    public Queue emailDirectQueue() {
        return new Queue("email.direct.queue", true);
    }

    @Bean
    public Queue wechatDirectQueue() {
        return new Queue("wechat.direct.queue", true);
    }

    // 完成绑定关系，with为指定路由key
    @Bean
    public Binding smsBinding(Queue smsDirectQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(smsDirectQueue).to(directExchange).with("sms");
    }

    @Bean
    public Binding emailBinding(Queue emailDirectQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(emailDirectQueue).to(directExchange).with("email");
    }

    @Bean
    public Binding wechatBinding(Queue wechatDirectQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(wechatDirectQueue).to(directExchange).with("wechat");
    }

}
