package com.small.interfaces;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.Intercept;
import com.github.lianjiatech.retrofit.spring.boot.retry.Retry;
import com.github.lianjiatech.retrofit.spring.boot.retry.RetryRule;
import com.small.interceptor.MyRetrofitInterceptor;
import com.small.pojo.TestEntity;
import org.springframework.stereotype.Component;
import retrofit2.http.GET;

import java.util.List;

/**
 * @author wesson
 * Create at 2022/3/29 11:43 周二
 */
@Component
@RetrofitClient(baseUrl = "${remote.base-url}")
@Intercept(handler = MyRetrofitInterceptor.class, include = "/**")
public interface RetrofitClient1 {

    @GET("small/get")
    @Retry(maxRetries = 3, retryRules = {RetryRule.RESPONSE_STATUS_NOT_2XX, RetryRule.OCCUR_EXCEPTION})
    List<TestEntity> retrofitGet();
}
