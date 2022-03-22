package com.small;

import com.small.util.MyOpenCsv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wesson
 * Create at 2022/3/22 19:21 周二
 */
class MyTest {

    @Test
    void testZip() throws Exception {
        File outFile = new File("/Users/wesson/Desktop/output/zip.zip");
        FileOutputStream outputStream = new FileOutputStream(outFile);
        File file = new File("/Users/wesson/Desktop/output/");
        File[] files = file.listFiles();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            byte[] buffer = new byte[4096];

            assert files != null;
            for (File oneFile : files) {
                int len;
                try (InputStream inputStream = new FileInputStream(oneFile)) {
                    String name = oneFile.getName();
                    zipOutputStream.putNextEntry(new ZipEntry(name));
                    while ((len = inputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    zipOutputStream.closeEntry();
                }
            }
        }
        Assertions.assertTrue(outFile.exists());
    }

    @Test
    void testOpenCsv() {
        List<String[]> strings = MyOpenCsv.readCsv("/Users/wesson/Desktop/output/dmd.csv", 1);
        MyOpenCsv.writeCsv("/Users/wesson/Desktop/output/dmd2.csv", strings);
    }
}
