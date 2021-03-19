package com.github;

import com.github.service.OrderService;
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

}
