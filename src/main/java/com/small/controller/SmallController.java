package com.small.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.small.mapper.TestMapper;
import com.small.pojo.Test;
import com.small.pojo.TestPojo;
import com.small.service.SmallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author wesson
 * Create at 2020-12-01 03:10
 **/
@Slf4j
@RestController
@RequestMapping(value = "/small")
public class SmallController {
    @Resource
    private SmallService smallService;
    @Resource
    private TestMapper testMapper;

    @GetMapping("/get")
    public List<TestPojo> get(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringJoiner stringJoiner = new StringJoiner(",");
        while (headerNames.hasMoreElements()) {
            stringJoiner.add(headerNames.nextElement());
        }
        log.info("----header:[{}]", stringJoiner);
        return testMapper.getTestInfo();
    }


    @GetMapping("/get2")
    public void TestPojo() {
        for (int i = 7; i < 10000; i++) {
            testMapper.insert(new TestPojo().setName("中文"+ IdUtil.nanoId(5)).setId(i)
            );
        }
    }

    @GetMapping("/get3")
    public List<TestPojo> get3() {
        return testMapper.findLike("防守打");
    }

    @GetMapping("/get4")
    public List<TestPojo> get4() {
        return testMapper.findLike2("防守打");
    }

    @PostMapping("/post")
    public String post(@RequestBody Test test) {
        return LocalDateTimeUtil.now().toString() + test;
    }

    @DeleteMapping("/delete")
    public String delete() {
        return LocalDateTimeUtil.now().toString();
    }

    @PatchMapping("/patch")
    public String patch() {
        return LocalDateTimeUtil.now().toString();
    }

    @PutMapping("/put")
    public String put() {
        return LocalDateTimeUtil.now().toString();
    }

    @PutMapping("/learnMore")
    public String learnMore() {
        return LocalDateTimeUtil.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
