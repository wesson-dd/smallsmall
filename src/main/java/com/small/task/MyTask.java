package com.small.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wesson
 * Create at 2022/3/30 10:06 周三
 */
@Slf4j
@Component
public class MyTask {

    //@Scheduled(initialDelay = 1000, fixedRate = 20000)
    public void task01() {
        log.info("---i am task01---");
    }

    @Async //优先级高
    @Scheduled(initialDelay = 1500, fixedRate = 30000)
    public void task02() {
        log.info("---i am task02---");
    }
}
