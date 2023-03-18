package com.small.util;

import com.google.common.collect.HashBasedTable;

/**
 * @author wesson
 * Create at 2023/3/12 00:05 周日
 */
public class GauvaDemo {
    public static void main(String[] args) {
        final HashBasedTable<Object, Object, Object> table = HashBasedTable.create();
        System.out.println(table.contains(1, 2));
        table.put(1,2,"hahaha");
        System.out.println(table.contains(1, 2));
        System.out.println(table.get(1, 2));

    }
}
