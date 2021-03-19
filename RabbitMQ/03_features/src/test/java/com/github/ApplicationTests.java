package com.github;

import com.github.service.OrderService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    void ttlTest() {
        orderService.makeOrderTTL();
    }

    @SneakyThrows
    @Test
    void lengthTest() {
        for (int i = 0; i < 20; i++) {
            orderService.makeOrderTTL();
            Thread.sleep(2000);
        }
    }

}
