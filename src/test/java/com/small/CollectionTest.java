package com.small;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.lang.Console;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.small.pojo.TestPojo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * @author wesson
 * Create at 2023/2/25 14:01 周六
 */
public class CollectionTest {

    @Test
    public void MapTest() {
        Map<String, Set<String>> map = Maps.newHashMap();
        Map<String, Set<String>> map2 = Maps.newHashMap();
        Map<String, String> mapOrigin = Maps.newHashMap();
        mapOrigin.put("A", "A");
        mapOrigin.put("A2", "B");
        mapOrigin.put("A3", "B");
        mapOrigin.put("A4", "C");
        mapOrigin.put("A5", "C");
        mapOrigin.put("A6", "C");
        mapOrigin.put("A7", "D");
        mapOrigin.put("A8", "D");

        mapOrigin.forEach((k, v) -> map.computeIfAbsent(v, w -> Sets.newHashSet()).add(k));

        Console.log(map);

        map2.put("A", Sets.newHashSet("==", "--"));
        mapOrigin.forEach((k, v) -> map2.computeIfPresent(v, (v1, v2) -> {
            v2.add(k);
            return v2;
        }));

        Console.log(map2);


    }

    @Test
    public void MapTest2() {
        final TestPojo testPojo = new TestPojo();
        testPojo.setId(1);
        testPojo.setName("test1");
        final TestPojo testPojo2 = new TestPojo();
        testPojo2.setId(2);
        testPojo2.setName("test2");
        final TestPojo testPojo3 = new TestPojo();
        testPojo3.setId(2);
        testPojo3.setName("test3");
        final TestPojo testPojo4 = new TestPojo();
        testPojo4.setId(4);
        testPojo4.setName("test4");

        final ArrayList<TestPojo> testPojos = Lists.newArrayList(testPojo, testPojo2, testPojo3, testPojo4);

        final Map<Integer, TestPojo> identityMap = CollStreamUtil.toIdentityMap(testPojos, TestPojo::getId);
        System.out.println(identityMap);


    }
}
