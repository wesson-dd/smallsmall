package com.small.small;

import java.io.File;
import java.io.IOException;

/**
 * description:
 * <p>
 * Create By wesson 2020-09-17 22:28
 **/

public class Test {
    public static void main(String[] args) throws IOException {


        File file = new File("./doc/a.csv");

        if (!file.exists()) {
            file.createNewFile();
        }


    }
}
