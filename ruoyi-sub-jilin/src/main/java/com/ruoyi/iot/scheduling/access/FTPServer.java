package com.ruoyi.iot.scheduling.access;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

public class FTPServer {
    public static void main(String[] args) {
        // 初始化FTP服务器配置列表
//        List<FTPServerConfig> servers = Arrays.asList(
//                new FTPServerConfig("10.1.3.150", 21, "yuancheng", "123456", "/", 1),
//                new FTPServerConfig("10.1.3.151", 21, "yuancheng", "123456", "/", 1),
//                new FTPServerConfig("10.1.3.152", 21, "yuancheng", "123456", "/", 2),
//                new FTPServerConfig("10.1.3.153", 21, "yuancheng", "123456", "/", 2),
//                new FTPServerConfig("10.1.3.154", 21, "yuancheng", "123456", "/", 2));
//
//        // 下载和处理FTP服务器文件
//        for (FTPServerConfig server : servers) {
//            System.out.println(server.getServer());
//            String localFilePath = "/home/mashir0/project/Normal.mdb";
////            processFTPServer(server, localFilePath);
//        }
//        print("/Users/y/Desktop/项目/吉林/Normal.mdb");
    }

    /**
     * 处理单个FTP服务器中的文件
     */
    public void processFTPServer(FTPServerConfig config, String localFilePath) {
        FTPClient ftpClient = new FTPClient();
//        localFilePath = "/home/mashir0/project/Normal.mdb";  // 本地保存文件的路径

        try {
            // 连接到FTP服务器
            ftpClient.connect(config.getServer(), config.getPort());
//            System.out.println("Connected to " + config.getServer());

            // 登录到FTP服务器
            if (ftpClient.login(config.getUser(), config.getPass())) {
//                System.out.println("Logged in as " + config.getUser());

                // 下载文件
                if (downloadFile(ftpClient, config.getRemoteDir(), localFilePath)) {
                    System.out.println("File downloaded successfully.");

                    // 处理本地数据库文件
//                    System.out.println("开始处理本地文件");
//                    print(localFilePath);

                } else {
                    System.out.println("Failed to download file.");
                }

                // 退出登录
                ftpClient.logout();
            } else {
                System.out.println("Failed to login to FTP server: " + config.getServer());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                // 断开连接
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                    System.out.println("Disconnected from FTP server: " + config.getServer());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 从FTP服务器下载文件到本地
     */
    public boolean downloadFile(FTPClient ftpClient, String remoteFilePath, String localFilePath) {
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

        try (OutputStream outputStream = new FileOutputStream(localFilePath)) {
            // 设置为被动模式
            ftpClient.enterLocalPassiveMode();
            // 下载文件
            boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);
            return success;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("开始报错");
            return false;
        }
    }
}