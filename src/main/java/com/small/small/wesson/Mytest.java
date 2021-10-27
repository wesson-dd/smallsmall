package com.small.small.wesson;

import com.small.small.config.MyConfig;
import com.small.small.utils.SpringHelp;
import lombok.extern.slf4j.Slf4j;

/**
 * description:
 *
 * @author wesson
 * Create at 2021-10-27 22:25
 **/
@Slf4j
public class Mytest {
    private static final MyConfig myConfig = SpringHelp.getBean(MyConfig.class);

    private Mytest() {
        throw new IllegalArgumentException("error");
    }


    public static String method1() {

        String name = log.getName();
        String format = String.format("%s say wesson %s %s %s", name, myConfig.getTo(), myConfig.getBe(), myConfig.getTop());
        log.info(format);
        return format;

    }

}
