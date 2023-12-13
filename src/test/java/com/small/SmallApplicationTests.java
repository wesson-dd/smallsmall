package com.small;

import com.small.interfaces.RetrofitClient1;
import com.small.pojo.TestEntity;
import com.small.service.SmallService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SmallApplicationTests {
    @Autowired
    private SmallService smallService;
    @Autowired
    private RetrofitClient1 retrofitClient1;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(smallService.getList());

    }

    @Test
    void retrofitTest1() {

        List<TestEntity> retrofitGet = retrofitClient1.retrofitGet();
        Assertions.assertNotNull(retrofitGet);
    }
}
