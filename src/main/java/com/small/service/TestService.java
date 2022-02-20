package com.small.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wesson
 * Create at 2022/2/20 19:55 周日
 */
@Service
public class TestService {
    @Value("${wesson.to}")
    private String to;
    @Value("${wesson.be}")
    private String be;
    @Value("${wesson.top}")
    private String top;

    private final List<String> list = new ArrayList<>();


    @PostConstruct
    public void init() {
        if (top.equals("top")) {
            list.add("wesson");
        }
    }


    public String getList() {
        if (list.isEmpty()) {
            return "null";
        }
        return list.get(0);
    }

}
