package com.github.produce;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HAN
 * @version 1.0
 * @create 03-16-3:55
 */
@SpringBootTest
class QueueProduceTest {

    @Autowired
    private QueueProduce queueProduce;

    @Test
    void produceMessage() {
        queueProduce.produceMessage();
    }
}