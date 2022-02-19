package com.small.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author wesson
 * Create at 2020-12-01 03:10
 **/
@RestController
@RequestMapping(value = "/small")
public class SmallController {

    @GetMapping("/get")
    public String get() {
        return LocalDateTimeUtil.now().toString();
    }

    @PostMapping("/post")
    public String post() {
        return LocalDateTimeUtil.now().toString();
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
}
