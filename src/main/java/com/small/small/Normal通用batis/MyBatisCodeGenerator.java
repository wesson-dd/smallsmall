package com.small.small.Normal通用batis;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 本类不用修改，运行就行   这个生成的xml 包含sql
 * 具体配置，请修改 MBG_configuration.xml
 */
public class MyBatisCodeGenerator {
    public static void main(String[] args) throws Exception {
        System.out.println("+++++++++generate begin++++++++++");
        List<String> warnings = new ArrayList<String>();
        // TODO 路径
        String url = MyBatisCodeGenerator.class.getResource("/MBG_configuration.xml").getFile();
        Configuration config = new ConfigurationParser(warnings).parseConfiguration(new File(url));
        DefaultShellCallback callback = new DefaultShellCallback(true);
        new MyBatisGenerator(config, callback, warnings).generate(null);
        System.out.println("+++++++++generate writer end+++++++++++");
    }
}
