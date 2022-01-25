package com.small.service;

import com.google.common.collect.Lists;
import com.opencsv.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Set;

/**
 * @author wesson
 * Create at 2021/11/11 22:57 周四
 */
@Slf4j
public class MyOpenCsv {
    public static void main(String[] args) {
        List<String[]> strings = MyOpenCsv.readCsv("/Users/wesson/Desktop/output/dmd.csv", 1);
        MyOpenCsv.writeCsv("/Users/wesson/Desktop/output/dmd2.csv", strings);
    }

    /**
     * @param filePath 文件路径
     * @return csv头list 速度快
     */
    @SneakyThrows
    public static List<String> readCsvHead(String filePath) {
        Set<String> set = new CSVReaderHeaderAware(new FileReader(filePath)).readMap().keySet();
        List<String> list = Lists.newArrayList(set);
        log.info("[{}] 文件头={}", filePath, list);
        return list;
    }

    /**
     * 一次性读取
     *
     * @param filePath  文件路径
     * @param skipLines 跳过行数
     * @return List<String [ ]>
     */
    @SneakyThrows
    public static List<String[]> readCsv(String filePath, int skipLines) {
        RFC4180Parser rfc4180Parser = new RFC4180ParserBuilder().build();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(rfc4180Parser)
                .withSkipLines(skipLines)
                .build()) {
            //String[] nextLine;
            //List<String[]> list = Lists.newArrayList();
            //while ((nextLine = csvReader.readNext()) != null) {
            //    List<String> arrayList = Lists.newArrayList(nextLine);
            //    for (int i : numList) {
            //        arrayList.remove(i);
            //    }
            //    nextLine = arrayList.toArray(new String[0]);
            //    list.add(nextLine);
            //}
            return csvReader.readAll();
        }
    }

    /**
     * 一次性写入
     *
     * @param filePath 文件路径
     * @param list     list
     */
    @SneakyThrows
    public static void writeCsv(String filePath, List<String[]> list) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(list);
        }
    }

}
