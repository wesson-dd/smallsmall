package com.small;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
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
import com.small.pojo.TestEntity;
import com.small.util.MyOpenCsv;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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


}
