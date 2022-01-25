package com.small.util;


import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.*;


/**
 * 文件压缩工具
 *
 * @author wesson
 */
@Slf4j
public class ZipUtil {
    /**
     * 将文件打包成 zip 压缩包文件
     *
     * @param sourceFiles        待压缩的多个文件列表。只支持文件，不能有目录,否则抛异常。
     * @param zipFile            压缩文件。文件可以不存在，但是目录必须存在，否则抛异常。如 C:\Users\Think\Desktop\aa.zip
     * @param isDeleteSourceFile 是否删除源文件(sourceFiles)
     * @return 是否压缩成功
     */
    public static boolean archiveFiles2Zip(File[] sourceFiles, File zipFile, boolean isDeleteSourceFile) {
        if (ObjectUtil.isEmpty(sourceFiles)) {
            return false;
        }
        try (ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(zipFile)) {
            // Zip64Mode 枚举有 3 个值：Always：对所有条目使用 Zip64 扩展、
            // Never：不对任何条目使用Zip64扩展、AsNeeded：对需要的所有条目使用Zip64扩展
            zipArchiveOutputStream.setUseZip64(Zip64Mode.AsNeeded);
            for (File file : sourceFiles) {
                //将每个源文件用 ZipArchiveEntry 实体封装，然后添加到压缩文件中. 这样将来解压后里面的文件名称还是保持一致.
                ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file.getName());
                zipArchiveOutputStream.putArchiveEntry(zipArchiveEntry);
                InputStream inputStream = new FileInputStream(file);
                byte[] buffer = new byte[1024 * 5];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    zipArchiveOutputStream.write(buffer, 0, length);
                }
            }
            zipArchiveOutputStream.closeArchiveEntry();
            zipArchiveOutputStream.finish();
            if (isDeleteSourceFile) {
                for (File file : sourceFiles) {
                    file.deleteOnExit();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 将 zip 压缩包解压成文件到指定文件夹下
     *
     * @param zipFile   待解压的压缩文件。亲测  .zip 文件有效；.7z 压缩解压时抛异常。
     * @param targetDir 解压后文件存放的目的地. 此目录必须存在，否则异常。
     * @return 是否成功
     */
    public static boolean decompressZip2Files(File zipFile, File targetDir) {
        try (InputStream inputStream = new FileInputStream(zipFile);
             ZipArchiveInputStream zipArchiveInputStream = new ZipArchiveInputStream(inputStream, "UTF-8")) {
            //遍历解压每一个文件.
            ArchiveEntry archiveEntry;
            while (null != (archiveEntry = zipArchiveInputStream.getNextEntry())) {
                //获取文件名
                String archiveEntryFileName = archiveEntry.getName();
                //把解压出来的文件写到指定路径
                File entryFile = new File(targetDir, archiveEntryFileName);
                byte[] buffer = new byte[1024 * 5];
                OutputStream outputStream = new FileOutputStream(entryFile);
                int length;
                while ((length = zipArchiveInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
