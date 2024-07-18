package com.small.conf;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis 拦截器配置
 *
 * @author wesson
 * Created on 2024/7/15 上午12:07
 **/
@Configuration
public class MybatisConfig {

    @Bean
    public MybatisPlusInterceptor mybatisInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new TestMybatisPlusInterceptor());
        return mybatisPlusInterceptor;
    }
}
