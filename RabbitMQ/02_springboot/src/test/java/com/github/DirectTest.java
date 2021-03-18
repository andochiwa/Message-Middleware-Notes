package com.github;

import com.github.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HAN
 * @version 1.0
 * @create 03-19-7:46
 */
@SpringBootTest
public class DirectTest {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
        orderService.makeDirectOrder("1", "1", 12);
    }

}
