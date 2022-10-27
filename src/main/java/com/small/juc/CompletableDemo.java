package com.small.juc;

import cn.hutool.core.thread.ThreadUtil;

import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

/**
 * @author wesson
 * Create at 2022/10/27 09:30 周四
 */
public class CompletableDemo {
    public static void main(String[] args) {
        final String join = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(300);
            print("任务一");
            return "返回任务一";
        }).thenCombineAsync(CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(100);
            print("任务2");
            return "返回任务2";
        }), (s1, s2) -> "合并").thenCombineAsync(CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleep(100);
            print("任务3");
            return "返回任务3";
        }), (s1, s2) -> "合并2").thenCompose((pre)->CompletableFuture.supplyAsync(()->{print(pre+"thenCompose");return "ok";})).join();

        System.out.println(join);
    }
    private static void print(String msg){
        System.out.println(new StringJoiner("\t|\t").add(Thread.currentThread().getId() + "")
                .add(msg));
    }
}
