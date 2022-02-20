package com.small;

import com.small.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmallApplicationTests {
    @Autowired
    TestService testService;

    @Test
    void contextLoads() {
        System.out.println(testService.getList());
    }

}
