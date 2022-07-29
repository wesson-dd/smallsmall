package com.small.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.small.mapper.TestMapper;
import com.small.pojo.Test;
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
    public List<Test> get(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringJoiner stringJoiner = new StringJoiner(",");
        while (headerNames.hasMoreElements()) {
            stringJoiner.add(headerNames.nextElement());
        }
        log.info("----header:[{}]", stringJoiner);
        return testMapper.getTestInfo();
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
