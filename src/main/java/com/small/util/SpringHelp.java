package com.small.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * description: 用于多线程获取bean
 *
 * @author wesson
 * Create at 2021-01-03 23:19
 **/
@Configuration
public class SpringHelp implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public SpringHelp() {
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (applicationContext == null) {
            applicationContext = context;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取当前运行环境
     *
     * @return prod test uat
     */
    public static List<String> getProfile() {
        String[] profiles = applicationContext.getEnvironment().getActiveProfiles();
        if (profiles.length > 0) {
            return Arrays.asList(profiles);
        }
        return new ArrayList<>();
    }

    /**
     * 通过name获取 Bean
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 获取配置信息
     */
    public static <T> T getProperty(String name, Class<T> clazz, T defaultValue) {
        return getApplicationContext().getEnvironment().getProperty(name, clazz, defaultValue);
    }

    public static String getProperty(String name, String defaultValue) {
        return getApplicationContext().getEnvironment().getProperty(name, defaultValue);
    }

    public static <T> T getProperty(String name, Class<T> clazz) {
        return getApplicationContext().getEnvironment().getProperty(name, clazz);
    }
}
