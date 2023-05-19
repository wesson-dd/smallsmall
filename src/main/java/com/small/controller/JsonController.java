package com.small.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.small.mapper.TestJsonMapper;
import com.small.pojo.JsonEntry;
import com.small.pojo.TestJson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试json类型字段
 *
 * @author wesson
 * Create at 2023年05月19日19:39:05
 **/
@Slf4j
@RestController
@RequestMapping(value = "/json")
@RequiredArgsConstructor
public class JsonController {

    private final TestJsonMapper testMapper;


    @GetMapping("/select")
    public String select() {
        final List<TestJson> testJsons = testMapper.selectList(null);
        log.info("共{}条", testJsons.size());
        final List<TestJson> testJsons1 = testMapper.selectListXml();
        log.info("查一月哈 {}条: {}", testJsons1.size(), testJsons1);

        final List<TestJson> testJsons2 = testMapper.selectListXml2();
        log.info("查非空 {}条", testJsons2.size());


        return JSON.toJSONString(testJsons2);
    }

    @GetMapping("/remove")
    public void remove() {
        testMapper.remove("apr", 6);
    }

    @GetMapping("/replace")
    public void replace() {
        testMapper.set("设置新的值", 2);
    }

    @PostMapping("/add")
    public void add() {
        final TestJson testJson = new TestJson();
        testJson.setName(IdUtil.fastSimpleUUID());
        testJson.setMyJson(new JsonEntry().setJan("一月")
                .setFeb("二月")
                .setMar("三月")
                .setApr("四月")
                .setMay("五月"));

        testMapper.insert(testJson);
    }


}
