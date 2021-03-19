package com.github.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 死信队列
 * @author HAN
 * @version 1.0
 * @create 03-20-0:08
 */
@Configuration
public class DeadRabbitConfiguration {

    @Bean
    public DirectExchange deadDirectExchange() {
        return new DirectExchange("dead_direct_exchange", true, false);
    }

    @Bean
    public Queue deadDirectQueue() {
        return new Queue("dead.direct.queue", true, false, false);
    }

    @Bean
    public Binding deadQueueBinding(Queue deadDirectQueue, DirectExchange deadDirectExchange) {
        return BindingBuilder.bind(deadDirectQueue).to(deadDirectExchange).with("dead");
    }

}
