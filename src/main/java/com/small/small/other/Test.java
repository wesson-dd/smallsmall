package com.small.small.other;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p>
 * Create By wesson 2020-11-05 23:02
 **/

public class Test {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        System.out.println(list.get(5));
        List<Integer> list1 = list.subList(1, 2);
        for (Integer integer : list1) {
            System.out.println(integer);
        }
        System.out.println("----------");
        List<Integer> list3 = list.subList(2, 4);
        for (Integer integer : list3) {
            System.out.println(integer);
        }
        System.out.println("----------");
        List<Integer> list4 = list.subList(6, 6);
        for (Integer integer : list4) {
            System.out.println(integer);
        }

    }
}
