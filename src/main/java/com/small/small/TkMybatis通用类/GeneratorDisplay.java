package com.small.small.TkMybatis通用类;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成pojo和mapper
 *
 * @author wesson
 * by 2020/9/7
 */

public class GeneratorDisplay {
    /**
     * 运行主程序，生成pojo和mapper
     TODO 开始生成；
     */
    public static void main(String[] args) throws Exception {
        try {
            GeneratorDisplay generatorSqlmap = new GeneratorDisplay();
            generatorSqlmap.generator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generator() throws Exception {
        List<String> warnings = new ArrayList<String>();
        //指定 逆向工程配置文件   TODO：修改路径
        File configFile = new File("generatorConfig.xml");
        Configuration config = new ConfigurationParser(warnings)
                .parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(true);
        new MyBatisGenerator(config, callback, warnings).generate(null);
    }
}
