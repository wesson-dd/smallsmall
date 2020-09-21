package com.small.small.service;

import com.small.small.dao.DynamicMapper;
import com.small.small.pojo.DynamicMapperParams;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.csv.CSVFormat;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * description: 从数据库读取数据生成csv
 * <p>
 * Create By wesson 2020-09-20 20:34
 **/
@Service
public class DbToCsvService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    DynamicMapper dynamicMapper;

    /**
     * 读取动态表配置，生成CSV文件
     * @param result      文件放置路径
     * @param tableName   数据库表名
     * @param columnsName 字段名
     * @throws IOException 异常
     */
    public void autoReadDbToScv(File result, String tableName, List<String> columnsName,String schemeId) throws IOException {
        OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(result), StandardCharsets.UTF_8);
        CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withRecordSeparator("\n"));
        // 写表头
        printer.printRecord(columnsName);
        // 写数据
        DynamicMapperParams dynamicMapperParams = new DynamicMapperParams();
        dynamicMapperParams.setTableName(tableName);
        dynamicMapperParams.setColumns(columnsName);
        dynamicMapperParams.setSqlCondition("");
        // 读数据库，转储到csv
        int total = dynamicMapper.getTotalCountByParam(dynamicMapperParams);
        List<LinkedHashMap<String, Object>> tmpList;
        for (int i = 0; i < total; i += 3000) {
            dynamicMapperParams.setSqlCondition("where id = "+schemeId+" limit " + i + ",3000");
            tmpList = dynamicMapper.getResultListByParams(dynamicMapperParams);
            List<Collection<Object>> rows = tmpList.stream()
                    .map(LinkedHashMap::values)
                    .collect(Collectors.toList());
            printer.printRecords(rows);
            logger.info("读取完成！");
        }
        printer.close();
        fileWriter.close();
    }
}
