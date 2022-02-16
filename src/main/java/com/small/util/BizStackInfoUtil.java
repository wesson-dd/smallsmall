package com.small.util;

/**
 * 屏蔽无用框架堆栈日志,减少磁盘压力
 *
 * @author wesson
 * Create at 2022/2/16 20:20 周三
 */
public final class BizStackInfoUtil {

    /**
     * 获取以指定包名为前缀的堆栈信息
     *
     * @param e             异常
     * @param packagePrefix 包前缀
     * @return 堆栈信息
     */
    public static String getStackTraceByPrefix(Throwable e, String packagePrefix) {
        StringBuilder s = new StringBuilder("\n").append(e);
        for (StackTraceElement traceElement : e.getStackTrace()) {
            if (!traceElement.getClassName().startsWith(packagePrefix)) {
                continue;
            }
            s.append("\n\tat ").append(traceElement);
        }
        return s.toString();
    }

    /**
     * com.small日志
     *
     * @param e 异常
     * @return 堆栈信息
     */
    public static String getBizStackTrace(Throwable e) {
        return getStackTraceByPrefix(e, "com.small");
    }

}
