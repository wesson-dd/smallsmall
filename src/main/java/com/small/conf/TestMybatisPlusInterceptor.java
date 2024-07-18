package com.small.conf;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;

/**
 * TODO
 *
 * @author wesson
 * Created on 2024/7/15 上午12:10
 **/
@Slf4j
@Intercepts({
        @Signature(type = StatementHandler.class, method = "parameterize", args = {java.sql.Statement.class}),
        @Signature(type = Executor.class, method = "commit", args = {boolean.class}),
        @Signature(type = Executor.class, method = "flushStatements", args = {})}
)
public class TestMybatisPlusInterceptor implements InnerInterceptor {

    @Override
    public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {


        log.info("执行了 TestMybatisPlusInterceptor");
    }
}
