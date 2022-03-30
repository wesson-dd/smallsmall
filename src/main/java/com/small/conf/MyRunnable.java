package com.small.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 获取程序的启动参数
 *
 * @author wesson
 * Create at 2022/3/29 20:23 周二
 */
@Slf4j
@Component
public class MyRunnable implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set<String> optionNames = args.getOptionNames();
        log.info("参数名:{}", optionNames);
    }
}
