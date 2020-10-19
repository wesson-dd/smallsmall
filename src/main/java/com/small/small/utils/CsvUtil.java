package com.small.small.utils;


import com.csvreader.CsvReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * <p>
 * Create By wesson 2020-10-19 22:44
 **/

public class CsvUtil {
    public static void main(String args[]) {
        CsvUtil csv1 = new CsvUtil();
        csv1.read("./doc/com1.csv");
    }

    /**
     * 读取文件信息,存入ArrayList集合
     */
    public void read(String filePath) {
        List<Object> list;
        try {
            list =new ArrayList<>();
            // 创建CSV读对象,设置字符集为GBK
            CsvReader csvReader = new CsvReader(filePath, ',', Charset.forName("GBK"));
            // 读表头,返回的是布尔值
            csvReader.readHeaders();
            //这是循环读取
            while (csvReader.readRecord()) {
                // 读一整行,默认是以逗号分离的
                String rawRecord = csvReader.getRawRecord();
                //转换读取的行分成数组,split方法是把字符串根据符号","拆分成一个String数组
                String[] split = rawRecord.split(",");
                //把每一行的分出来的数组添加到集合list里
                list.add(split);
            }

            OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream("./doc/com2.csv"), StandardCharsets.UTF_8);
            CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withRecordSeparator("\n"));
            List<String> columnsName = new ArrayList<>();
            columnsName.add("阿里");
            columnsName.add("腾讯");
            columnsName.add("百度");
            printer.printRecord(columnsName);

            printer.printRecords(list);

            printer.close();
            fileWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}



