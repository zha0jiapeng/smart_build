package com.ruoyi.iot.scheduling.access;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class ComprehensiveApp {
    public static void main(String[] args) {
        // 初始化FTP服务器配置列表
        List<FTPServerConfig> servers = Arrays.asList(
                new FTPServerConfig("10.1.3.150", 21, "yuancheng", "123456", "/", 1),
                new FTPServerConfig("10.1.3.151", 21, "yuancheng", "123456", "/", 1),
                new FTPServerConfig("10.1.3.152", 21, "yuancheng", "123456", "/", 2),
                new FTPServerConfig("10.1.3.153", 21, "yuancheng", "123456", "/", 2),
                new FTPServerConfig("10.1.3.154", 21, "yuancheng", "123456", "/", 2));

        // 下载和处理FTP服务器文件
        for (FTPServerConfig server : servers) {
            System.out.println(server.getServer());
            processFTPServer(server);
        }
    }

    /**
     * 处理单个FTP服务器中的文件
     */
    public static void processFTPServer(FTPServerConfig config) {
        FTPClient ftpClient = new FTPClient();
        String localFilePath = "/home/mashir0/project/Normal.mdb";  // 本地保存文件的路径

        try {
            // 连接到FTP服务器
            ftpClient.connect(config.getServer(), config.getPort());
            System.out.println("Connected to " + config.getServer());

            // 登录到FTP服务器
            if (ftpClient.login(config.getUser(), config.getPass())) {
                System.out.println("Logged in as " + config.getUser());

                // 下载文件
                if (downloadFile(ftpClient, config.getRemoteDir() + "/Normal.mdb", localFilePath)) {
                    System.out.println("File downloaded successfully.");

                    // 处理本地数据库文件
                    System.out.println("开始处理本地文件");
                    print(localFilePath);
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
    public static boolean downloadFile(FTPClient ftpClient, String remoteFilePath, String localFilePath) {
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

    /**
     * 读取本地Access数据库并打印内容
     */
    public static void print(String databasePath) {
        System.out.println("开始");
        System.out.println();
        // 构建 UCanAccess 连接 URL，明确设置密码为空
        // 如果数据库文件有密码保护，包含密码
        String url = "jdbc:ucanaccess://" + databasePath + ";password=oke";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            System.out.println("开始加载");
            // 加载 UCanAccess 驱动程序
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            System.out.println("开始建立数据库连接");
            // 建立数据库连接
            connection = DriverManager.getConnection(url);
            System.out.println("建立成功");
            // 创建 Statement 对象并执行查询
            statement = connection.createStatement();
            System.out.println("开始查询");
            // 查询 test_result 表中的所有数据
            String query = "SELECT * FROM test_result";
            resultSet = statement.executeQuery(query);
            System.out.println("查询成功" + resultSet.toString());
            printResultSet(resultSet);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 确保关闭资源
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打印ResultSet内容
     */
    public static void printResultSet(ResultSet resultSet) {
        try {
            // 获取 ResultSet 的元数据
            ResultSetMetaData metaData = resultSet.getMetaData();

            // 获取列数
            int columnCount = metaData.getColumnCount();

            // 打印列名
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            // 遍历 ResultSet 并打印每一行的每一列
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}