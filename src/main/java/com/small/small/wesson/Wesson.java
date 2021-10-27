package com.small.small.wesson;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * description:
 *
 * @author wesson
 * Create at 2021-08-04 21:32
 **/

public class Wesson {
    public void allToZip(List<File> fileList, HttpServletResponse response, String fileName) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            byte[] buffer = new byte[4096];
            for (File file : fileList) {
                int len;
                try (InputStream inputStream = new FileInputStream(file)) {
                    String name = file.getName();
                    zipOutputStream.putNextEntry(new ZipEntry(name));
                    while ((len = inputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    zipOutputStream.closeEntry();
                }
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
