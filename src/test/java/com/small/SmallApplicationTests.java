package com.small;

import com.small.service.SmallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmallApplicationTests {
    @Autowired
    SmallService smallService;

    @Test
    void contextLoads() {
        System.out.println(smallService.getList());
    }

}
