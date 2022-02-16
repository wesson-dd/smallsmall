package com.small.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author wesson
 * Create at 2022/2/16 20:56 周三
 */
public class BizAssert {
    private BizAssert() {
    }

    public static void notEmpty(String string, String message) {
        if (StrUtil.isEmpty(string)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(String string, Supplier<String> messageSupplier) {
        notEmpty(string, messageSupplier.get());
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, Supplier<String> messageSupplier) {
        notNull(object, messageSupplier.get());
    }

    public static <T extends RuntimeException> void notNull2(Object object, Supplier<T> exceptionSupplier) {
        if (object == null) {
            throw exceptionSupplier.get();
        }
    }

    public static <T> void notEmpty(T[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void notEmpty(T[] array, Supplier<String> messageSupplier) {
        notEmpty(array, messageSupplier.get());
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollUtil.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertState(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言一个boolean表达式为true，用于需要大量拼接字符串以及一些其他操作等
     *
     * @param expression boolean表达式
     * @param supplier   msg生产者
     */
    public static void isTrue(boolean expression, Supplier<String> supplier) {
        if (!expression) {
            throw new IllegalArgumentException(supplier.get());
        }
    }
}