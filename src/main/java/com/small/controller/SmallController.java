package com.small.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.small.mapper.TestMapper;
import com.small.pojo.Test;
import com.small.pojo.TestPojo;
import com.small.service.SmallService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

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
        final List<@Nullable TestPojo> list = Lists.newArrayList();
        final List<@Nullable CompletableFuture> list2 = Lists.newArrayList();
        for (int i = 7; i < 10000; i++) {
            TestPojo testPojo = new TestPojo().setName(i % 123 == 0 ? "明" : "龙" + "," + IdUtil.nanoId(5));
            list.add(testPojo);
        }
        final List<List<@Nullable TestPojo>> partition = Lists.partition(list, 1000);
        for (List<TestPojo> testPojos : partition) {
            final CompletableFuture<Void> uCompletableFuture = CompletableFuture.runAsync(() -> {
                for (TestPojo testPojo : testPojos) {
                    testMapper.insert(testPojo);
                }
            });
            list2.add(uCompletableFuture);
        }

        final CompletableFuture[] array = list2.toArray(new CompletableFuture[0]);
        CompletableFuture.allOf(array);
        log.info("完成");
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

    @GetMapping("/getByForeach")
    public Object getByForeach() {
        final List<@Nullable String> list = Lists.newArrayList("明", "韦");
        final List<@Nullable String> list2 = Lists.newArrayList("aaa", "ss");
        final PageInfo<TestPojo> objectPageInfo = PageHelper.startPage(1, 100, "id desc").doSelectPageInfo(() -> testMapper.getByForeach(list, list2));
        return objectPageInfo;
    }


}
