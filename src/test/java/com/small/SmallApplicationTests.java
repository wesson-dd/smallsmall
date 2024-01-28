package com.small;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.small.interfaces.RetrofitClient1;
import com.small.mapper.TestMapper;
import com.small.pojo.TestEntity;
import com.small.pojo.TestPojo;
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
    @Autowired
    private TestMapper testMapper;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(smallService.getList());

    }

    @Test
    void retrofitTest1() {

        List<TestEntity> retrofitGet = retrofitClient1.retrofitGet();
        Assertions.assertNotNull(retrofitGet);
    }

    @Test
    void retrofitTest2() {
        LambdaUpdateWrapper<TestPojo> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(TestPojo::getId, 67)
                .set(TestPojo::getName, "会不会跟新呢2");
        testMapper.update(null, wrapper);
    }
}
