package com.small;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Indexed;

/**
 * @author wesson
 * @date 2022-01-26 03:05:57
 */
@Indexed
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
public class SmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmallApplication.class, args);
    }

}
