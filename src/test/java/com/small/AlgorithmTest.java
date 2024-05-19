package com.small;

import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;

/**
 * TODO
 *
 * @author wesson
 * Created on 2024/5/19 下午4:30
 **/
public class AlgorithmTest {

    /**
     * 判断是否为子序列
     */
    @Test
    public void isSubsequence() {
        String str1 = "abcz";
        String str2 = "afdbfdfcfdf";
        int index = -1;
        for (char c : str1.toCharArray()) {
            index = str2.indexOf(c, index + 1);
            if (index == -1) {
                Console.log("不是子序列");
                return;
            }
        }

        Console.log("是子序列");
    }


}
