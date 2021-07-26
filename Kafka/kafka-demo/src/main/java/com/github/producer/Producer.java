package com.github.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @author HAN
 * @version 1.0
 * @since 07-26-17:34
 */
public class Producer {

    public static void main(String[] args){
        // 创建kafka生产者的配置信息
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("acks", "1");
        properties.put("retries", 3);
        properties.put("batch.size", 8192);
        properties.put("linger.ms", 1);
        properties.put("key.serializer", StringSerializer.class);
        properties.put("value.serializer", StringSerializer.class);

        // 创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        System.out.println("开始发送数据");
        // 发送数据
//        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<>("test", "andochiwa--" ));
            System.out.println("发送消息 andochiwa--" + " 成功");
//        }

        // 关闭资源
        System.out.println("关");
        producer.close();
    }
}
