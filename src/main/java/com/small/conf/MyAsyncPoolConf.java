package com.small.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wesson
 * Create at 2022/3/30 10:05 周三
 */
@Configuration
public class MyAsyncPoolConf {
    @Bean
    public ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(5);
        // 最大线程数
        executor.setMaxPoolSize(10);
        // 任务队列大小
        executor.setQueueCapacity(50);
        // 线程前缀名
        executor.setThreadNamePrefix("async--");
        // 线程的空闲时间
        executor.setKeepAliveSeconds(500);
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程初始化
        executor.initialize();
        return executor;
    }


}
