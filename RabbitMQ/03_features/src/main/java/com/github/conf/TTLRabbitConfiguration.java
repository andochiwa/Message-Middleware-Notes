package com.github.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HAN
 * @version 1.0
 * @create 03-20-0:08
 */
@Configuration
public class TTLRabbitConfiguration {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("ttl_direct_exchange", true, false);
    }

    @Bean
    public Queue ttlDirectQueue(DirectExchange directExchange) {
        // 设置过期时间
        Map<String, Object> map = new HashMap<>();
        map.put("x-message-ttl", 5000);
        return new Queue("ttl.direct.queue", true, false, false, map);
    }

    @Bean
    public Binding ttlQueueBinding(Queue ttlDirectQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(ttlDirectQueue).to(directExchange).with("ttl");
    }

}
