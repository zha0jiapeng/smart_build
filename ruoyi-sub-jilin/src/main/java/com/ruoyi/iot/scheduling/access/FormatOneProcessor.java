package com.ruoyi.iot.scheduling.access;



import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

/**
 * 处理第一种目录格式的类。
 */
public class FormatOneProcessor {
    public void processFiles(FTPClient ftpClient, String remoteDir) throws IOException {
        FTPFile[] files = ftpClient.listFiles(remoteDir); // 列出目录下的文件和子目录
        for (FTPFile file : files) {
            if (file.isDirectory()) {
                System.out.println("[Format 1 Directory] " + file.getName()); // 输出目录信息
            } else {
                System.out.println("[Format 1 File] " + file.getName() + " (Size: " + file.getSize() + " bytes)"); // 输出文件信息
            }
        }
    }
}