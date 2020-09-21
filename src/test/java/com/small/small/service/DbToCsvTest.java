package com.small.small.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * description:
 * <p>
 * Create By wesson 2020-09-20 20:52
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class DbToCsvTest {
    @Autowired
    DbToCsvService dbToCsvService;
    @Test
    public void csvTest() throws IOException {
        File file = new File("./doc/csv2.csv");
        if(!file.exists()){
            file.getParentFile().mkdirs();
        }
        ArrayList<String> list = new ArrayList<>();
        list.add("id");
        list.add("name");
        list.add("addr");
        list.add("age");
        list.add("json");
        list.add("decimal");

        dbToCsvService.autoReadDbToScv(file,"person",list,"2");
    }
}
