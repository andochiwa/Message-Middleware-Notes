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
    public DirectExchange ttlDirectExchange() {
        return new DirectExchange("ttl_direct_exchange", true, false);
    }

    @Bean
    public Queue ttlDirectQueue() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-message-ttl", 10000); // 设置过期时间
        map.put("x-max-length", 5); // 指定队列长度
        // 配置死信队列信息
        map.put("x-dead-letter-exchange", "dead_direct_exchange");
        map.put("x-dead-letter-routing-key", "dead"); // 如果是fanout模式则不需要路由key
        return new Queue("ttl.direct.queue", true, false, false, map);
    }

    @Bean
    public Binding ttlQueueBinding(Queue ttlDirectQueue, DirectExchange ttlDirectExchange) {
        return BindingBuilder.bind(ttlDirectQueue).to(ttlDirectExchange).with("ttl");
    }

}
