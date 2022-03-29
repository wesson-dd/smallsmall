package com.small.interceptor;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 可选择性拦截
 *
 * @author wesson
 * Create at 2022/3/29 11:39 周二
 */
@Component
public class MyRetrofitInterceptor extends BasePathMatchInterceptor {

    @Override
    protected Response doIntercept(Chain chain) throws IOException {

        Request request = chain.request()
                .newBuilder()
                .addHeader("myToken", "token")
                .build();

        return chain.proceed(request);
    }
}
