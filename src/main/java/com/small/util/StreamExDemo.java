package com.small.util;

import cn.hutool.core.lang.Console;
import com.google.common.collect.Lists;
import com.small.pojo.Test;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wesson
 * Create at 2023/3/5 17:54 周日
 */
public class StreamExDemo {
    public static void main(String[] args) {
        List<Test> one = StreamEx.of(new Test().setName("A-"),
                new Test().setName("B-")).toList();
        Console.log(one);

        List<Test> objects = Lists.newArrayList();
        objects.addAll(one);
        Console.log(objects);

        List<Test> collect = one.stream().map(w -> {

            final Test test = new Test();
            test.setName(w.getName().replace("-", ""));
            return test;
        }).collect(Collectors.toList());

        Console.log(one);
        Console.log(objects);
        Console.log(collect);
    }
}
