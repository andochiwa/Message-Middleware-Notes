package com.github.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * @author HAN
 * @version 1.0
 * @create 03-19-7:13
 */
//@Configuration
public class FanoutConfiguration {

    // 声明fanout模式的交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout_order_exchange", true, false);
    }

    // 声明队列sms, email, wechat
    @Bean
    public Queue smsFanoutQueue() {
        return new Queue("sms.fanout.queue", true);
    }

    @Bean
    public Queue emailFanoutQueue() {
        return new Queue("email.fanout.queue", true);
    }

    @Bean
    public Queue wechatFanoutQueue() {
        return new Queue("wechat.fanout.queue", true);
    }

    // 完成绑定关系
    @Bean
    public Binding smsBinding(Queue smsFanoutQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(smsFanoutQueue).to(fanoutExchange);
    }

    @Bean
    public Binding emailBinding(Queue emailFanoutQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(emailFanoutQueue).to(fanoutExchange);
    }

    @Bean
    public Binding wechatBinding(Queue wechatFanoutQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(wechatFanoutQueue).to(fanoutExchange);
    }

}
