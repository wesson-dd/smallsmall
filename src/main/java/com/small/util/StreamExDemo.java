package com.small.util;

import one.util.streamex.StreamEx;

import java.util.List;

/**
 * @author wesson
 * Create at 2023/3/5 17:54 周日
 */
public class StreamExDemo {
    public static void main(String[] args) {
        final List<Integer> integers = StreamEx.of(1, 2, 3, 4, 5).toList();
        System.out.println(integers);

        System.out.println(StreamEx.of(1, 2, 3, 45).without(3).toList());
    }
}
