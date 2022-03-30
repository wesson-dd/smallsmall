package com.small.conf;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @author wesson
 * Create at 2022/3/30 10:36 周三
 */
@Configuration
public class MyScheduleConf implements SchedulingConfigurer {


    public static final String THREAD_NAME_WITH_SCHEDULE = "schedule--%d";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(THREAD_NAME_WITH_SCHEDULE).build();

        /*
         1. CallerRunsPolicy ：    这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功。
         2. AbortPolicy ：         对拒绝任务抛弃处理，并且抛出异常。
         3. DiscardPolicy ：       对拒绝任务直接无声抛弃，没有异常信息。
         4. DiscardOldestPolicy ： 对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列。
         不写则为默认的AbortPolicy策略。
         */
        ScheduledExecutorService threadPool = new ScheduledThreadPoolExecutor(
                5,
                threadFactory);

        scheduledTaskRegistrar.setScheduler(threadPool);
    }

}

