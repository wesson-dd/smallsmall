package com.small.util;

import cn.hutool.core.lang.Console;
import com.google.common.collect.Lists;
import com.small.pojo.TestEntity;
import one.util.streamex.StreamEx;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wesson
 * Create at 2023/3/5 17:54 周日
 */
public class StreamExDemo {
    public static void main(String[] args) {
        List<TestEntity> one = StreamEx.of(new TestEntity().setName("A-"),
                new TestEntity().setName("B-")).toList();
        Console.log(one);

        List<TestEntity> objects = Lists.newArrayList();
        objects.addAll(one);
        Console.log(objects);

        List<TestEntity> collect = one.stream().map(w -> {

            final TestEntity testEntity = new TestEntity();
            testEntity.setName(w.getName().replace("-", ""));
            return testEntity;
        }).collect(Collectors.toList());

        Console.log(one);
        Console.log(objects);
        Console.log(collect);
    }
}
