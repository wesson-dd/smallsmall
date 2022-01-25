package com.small.small.utils;

import java.util.Map;
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

    Map<String, Object> mutexCache = new ConcurrentHashMap<>();

    public void exec(String key, Runnable statement) {
        Object mutexKey = mutexCache.computeIfAbsent(key, v -> new Object());
        synchronized (mutexKey) {
            try {
                statement.run();
            } finally {
                mutexCache.remove(key);
            }
        }

    }

}
