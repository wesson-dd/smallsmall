package com.small.util;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 同步锁工具类 注入使用
 *
 * @author wesson
 * Create at 2022/1/26 00:54 周三
 */
public class SynchronizedUtil {
    public SynchronizedUtil() {
    }

    private static final Map<String, Object> mutexCache = new ConcurrentHashMap<>();

    public static void exec(String key, Runnable statement) {
        Object mutexKey = mutexCache.computeIfAbsent(key, v -> new Object());
        Console.log("mutexKey= {}",mutexKey);
        synchronized (mutexKey) {
            try {
                statement.run();
            } finally {
                mutexCache.remove(key);
            }
        }

    }

    public static void main(String[] args) {
        final CompletableFuture<String> async = CompletableFuture.supplyAsync(() -> {

            SynchronizedUtil.exec("we", () -> {
                ThreadUtil.sleep(10000);
                Console.log("hahahaha");
            });
            return "";
        });

        final CompletableFuture<String> async2 = CompletableFuture.supplyAsync(() -> {

            SynchronizedUtil.exec("we", () -> {
                ThreadUtil.sleep(10000);
                Console.log("hahaha770ha");
            });
            return "";
        });

        CompletableFuture.allOf(async2, async);
        ThreadUtil.sleep(20200);
    }
}
