package com.small;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.small.mapstruct.Car;
import com.small.mapstruct.CarDto;
import com.small.mapstruct.CarMapper;
import com.small.pojo.PlanRequirePool;
import com.small.pojo.TestEntity;
import com.small.util.MyOpenCsv;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wesson
 * Create at 2022/3/22 19:21 周二
 */
public class MyTest {

    @Test
    public void testZip() throws Exception {
        File outFile = new File("/Users/wesson/Desktop/output/zip.zip");
        FileOutputStream outputStream = new FileOutputStream(outFile);
        File file = new File("/Users/wesson/Desktop/output/");
        File[] files = file.listFiles();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            byte[] buffer = new byte[4096];

            assert files != null;
            for (File oneFile : files) {
                int len;
                try (InputStream inputStream = new FileInputStream(oneFile)) {
                    String name = oneFile.getName();
                    zipOutputStream.putNextEntry(new ZipEntry(name));
                    while ((len = inputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    zipOutputStream.closeEntry();
                }
            }
        }
        Assertions.assertTrue(outFile.exists());
    }

    @Test
    public void testOpenCsv() {
        List<String[]> strings = MyOpenCsv.readCsv("/Users/wesson/Desktop/output/dmd.csv", 1);
        MyOpenCsv.writeCsv("/Users/wesson/Desktop/output/dmd2.csv", strings);

        Assertions.assertTrue(new File("/Users/wesson/Desktop/output/dmd2.csv").exists());
    }

    @Test
    public void newClassInfo() {
        // OFF  SZ                   TYPE DESCRIPTION               VALUE
        // 0   8                        (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
        // 8   4                        (object header: class)    0xf8029b96
        // 12   4       java.lang.String Test.name                 null
        // 16   4      java.lang.Integer Test.type                 null
        // 20   4      java.lang.Integer Test.id                   null
        // 24   4      java.lang.Boolean Test.aBoolean             true
        // 28   4         java.lang.Long Test.aLong                null
        // 32   4   java.math.BigDecimal Test.bigDecimal           null
        // 36   4                        (object alignment gap)
        // Instance size: 40 bytes

        TestEntity testEntity = new TestEntity();
        System.out.println(ClassLayout.parseInstance(testEntity).toPrintable());

        synchronized (testEntity) {
            System.out.println(ClassLayout.parseInstance(testEntity).toPrintable());
        }

        Object obj = new Object();// 16 byte
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }

    @Test
    public void writeCsvWithBean() {
        TestEntity testEntity = new TestEntity();
        testEntity.setId(2);
        TestEntity testEntity2 = new TestEntity();
        testEntity2.setId(3);
        List<TestEntity> testEntities = Lists.newArrayList(testEntity2, testEntity);

        CsvWriter writer = CsvUtil.getWriter("aa.csv", Charset.defaultCharset());
        writer.writeBeans(testEntities);
        writer.close();
    }

    @Test
    public void moveSheet() {
        //加载文档
        Workbook wb = new Workbook();
        wb.loadFromFile("/Users/wesson/Desktop/test.xlsx");
        //获取工作表
        Worksheet sheet = wb.getWorksheets().get(1);
        //移动工作表作为第三张工作表
        sheet.moveWorksheet(0);
        //保存文档
        wb.save();
        wb.dispose();
    }

    @Test
    public void sort() {
        final List<TestEntity> testEntities = Lists.newArrayList(new TestEntity().setTime(LocalDateTime.now()),
                new TestEntity().setTime(LocalDateTime.now().plusHours(-1)));
        System.out.println(testEntities);

        testEntities.sort(Comparator.comparing(TestEntity::getTime));

        System.out.println(testEntities);


    }

    @Test
    public void mapStruct() {
        final Car car = new Car();
        car.setId(null);
        car.setStr(null);
        car.setBigDecimal(new BigDecimal("1.23"));
        car.setTime(LocalDateTime.now());


        final CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);

        System.out.println(JSON.toJSONString(carDto));


    }

    @Test
    public void csv2() {
        final CsvWriter writer = CsvUtil.getWriter("./wesson.csv", Charset.defaultCharset(), true);

        for (int i = 0; i < 10000; i++) {
            String tet = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN) + ": " + IdUtil.fastSimpleUUID();
            writer.writeLine(tet);
        }

        writer.close();
    }

    @Test
    public void csv3() {
        final ArrayList<TestEntity> list = Lists.newArrayList(TestEntity.builder().id(1).aBoolean(false).build(),
                TestEntity.builder().id(3).aBoolean(false).build(),
                TestEntity.builder().id(2).aBoolean(false).build());

        final PageInfo<TestEntity> objectPageInfo = new PageInfo<TestEntity>(list);

        System.out.println(JSONUtil.toJsonPrettyStr(objectPageInfo));

        final List<TestEntity> list1 = objectPageInfo.getList();
        list1.forEach(r -> r.setId(r.getId() + 100));

        System.out.println();
        System.out.println();
        System.out.println(JSONUtil.toJsonPrettyStr(objectPageInfo));
    }

    @Test
    public void getFile() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("config/wesson.txt");

        InputStream inputStream = classPathResource.getInputStream();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouBanTest() {
        PlanRequirePool pool1 = PlanRequirePool.builder().kao("05:00:00").fa("06:40:00").dao("07:25:00").jie("07:55:00").id(1L).build();
        PlanRequirePool pool2 = PlanRequirePool.builder().kao("10:45:00").fa("11:05:00").dao("11:50:00").jie("12:00:00").id(2L).build();
        PlanRequirePool pool3 = PlanRequirePool.builder().kao("16:00:00").fa("16:38:00").dao("17:59:00").jie("18:29:00").id(3L).build();
        PlanRequirePool pool4 = PlanRequirePool.builder().kao("19:00:00").fa("19:50:00").dao("20:50:00").jie("21:00:00").id(4L).build();
        PlanRequirePool pool5 = PlanRequirePool.builder().kao("22:35:00").fa("23:25:00").dao("00:15:00").jie("00:25:00").id(5L).build();
        PlanRequirePool pool6 = PlanRequirePool.builder().kao("12:30:00").fa("12:58:00").dao("14:14:00").jie("14:44:00").id(6L).build();
        PlanRequirePool pool7 = PlanRequirePool.builder().kao("13:40:00").fa("14:00:00").dao("15:15:00").jie("15:25:00").id(7L).build();
        PlanRequirePool pool8 = PlanRequirePool.builder().kao("04:30:00").fa("06:40:00").dao("07:25:00").jie("07:55:00").id(8L).build();
        PlanRequirePool pool9 = PlanRequirePool.builder().kao("06:44:00").fa("07:04:00").dao("07:58:00").jie("08:28:00").id(9L).build();

        List<PlanRequirePool> pools = Lists.newArrayList(pool1, pool2, pool3, pool4, pool5, pool6, pool7, pool8, pool9)
                .stream()
                .sorted(Comparator.comparing((PlanRequirePool r) ->
                                DateUtil.parse(r.getFa(), DatePattern.NORM_TIME_PATTERN).getTime())
                        .thenComparing(r ->
                                DateUtil.parse(r.getKao(), DatePattern.NORM_TIME_PATTERN).getTime())
                        .thenComparing(r ->
                                DateUtil.parse(r.getDao(), DatePattern.NORM_TIME_PATTERN).getTime())
                        .thenComparing(r ->
                                DateUtil.parse(r.getJie(), DatePattern.NORM_TIME_PATTERN).getTime()))
                .collect(Collectors.toList());


        Console.log("first sort :{}", JSONUtil.toJsonPrettyStr(pools));

        // 分组
        List<List<PlanRequirePool>> groupedPools = new ArrayList<>();

        List<PlanRequirePool> currentGroup = new ArrayList<>();
        currentGroup.add(pools.get(0)); // 将第一个元素加入当前分组

        for (int i = 1; i < pools.size(); i++) {
            PlanRequirePool current = pools.get(i);
            PlanRequirePool previous = pools.get(i - 1);

            if (DateUtil.parse(previous.getDao(), DatePattern.NORM_TIME_PATTERN).getTime() >
                    DateUtil.parse(current.getFa(), DatePattern.NORM_TIME_PATTERN).getTime()) {
                // 如果上一条的 dao 时间大于下一条的 fa 时间，则将当前元素添加到当前分组
                currentGroup.add(current);
            } else {
                // 否则，将当前分组添加到分组列表中，并开始一个新的分组
                groupedPools.add(currentGroup);
                currentGroup = new ArrayList<>();
                currentGroup.add(current);
            }
        }

        // 添加最后一个分组
        if (!currentGroup.isEmpty()) {
            groupedPools.add(currentGroup);
        }

        Console.log("group  :{}", JSONUtil.toJsonPrettyStr(groupedPools));


        Map<String, List<PlanRequirePool>> mappedGroups = new LinkedHashMap<>();
        Map<Integer, String> groupCountKeyMap = new LinkedHashMap<>();
        int groupCount = 0;
        for (List<PlanRequirePool> group : groupedPools) {
            String minKao = group.stream()
                    .min(Comparator.comparing(PlanRequirePool::getKao))
                    .map(PlanRequirePool::getKao)
                    .orElse("");

            String maxDao = group.stream()
                    .max(Comparator.comparing(PlanRequirePool::getDao))
                    .map(PlanRequirePool::getDao)
                    .orElse("");

            String maxJie = group.stream()
                    .max(Comparator.comparing(PlanRequirePool::getJie))
                    .map(PlanRequirePool::getJie)
                    .orElse("");

            String key = minKao + "-" + maxDao + "-" + maxJie; // 拼接成一个字符串作为key
            mappedGroups.put(key, group);
            groupCountKeyMap.put(groupCount, key);
            groupCount++;
        }


        // 组之间计算差值
        if (groupedPools.size() == 1) {
            Console.log("poolsFinal sort  :{}", JSONUtil.toJsonPrettyStr(groupedPools.get(0)));
        } else {
            String maxKey = groupCountKeyMap.get(0);
            String finalKey = groupCountKeyMap.get(groupedPools.size() - 1);

            long abs = Math.abs(DateUtil.parse(maxKey.split("-")[0], DatePattern.NORM_TIME_PATTERN).getTime() -
                    DateUtil.parse(finalKey.split("-")[1], DatePattern.NORM_TIME_PATTERN).getTime());

            for (int i = 1; i < groupedPools.size(); i++) {
                String preKey = groupCountKeyMap.get(i - 1);
                String currentKey = groupCountKeyMap.get(i);

                long absCurrent = Math.abs(DateUtil.parse(currentKey.split("-")[0], DatePattern.NORM_TIME_PATTERN).getTime() -
                        DateUtil.parse(preKey.split("-")[1], DatePattern.NORM_TIME_PATTERN).getTime());

                if (absCurrent > abs) {
                    abs = absCurrent;
                    maxKey = currentKey;
                }
            }

            // 最终排序
            List<PlanRequirePool> poolsFinal = new ArrayList<>();
            List<PlanRequirePool> poolsNotFirst = new ArrayList<>();

            for (int i = 0; i < groupedPools.size(); i++) {
                String currentKey = groupCountKeyMap.get(i);
                if (currentKey.equals(maxKey)) {
                    poolsFinal.addAll(groupedPools.get(i));
                } else {
                    poolsNotFirst.addAll(groupedPools.get(i));
                }
            }

            poolsFinal.addAll(poolsNotFirst);

            Console.log("poolsFinal sort  :{}", JSONUtil.toJsonPrettyStr(poolsFinal));
        }
    }

}
