package com.small.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 *
 * @author wesson
 * Create at 2020-12-01 03:10
 **/
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @GetMapping("/a")
    public String test() {
        return LocalDateTimeUtil.now().toString();
    }
}
