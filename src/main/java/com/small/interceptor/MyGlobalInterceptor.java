package com.small.interceptor;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.GlobalInterceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 全局拦截
 *
 * @author wesson
 * Create at 2022/3/29 11:35 周二
 */
@Component
public class MyGlobalInterceptor implements GlobalInterceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("myHeader", "retrofit")
                .build();

        return chain.proceed(request);
    }
}
