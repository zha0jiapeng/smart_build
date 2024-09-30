package com.ruoyi.iot.scheduling.access;

/**
 * 存储每个FTP服务器的配置信息。
 */
public class FTPServerConfig {
    private String server;     // 服务器IP地址
    private int port;          // 端口号
    private String user;       // 登录用户名
    private String pass;       // 登录密码
    private String remoteDir;  // 远程目录
    private int formatType;    // 文件格式类型

    // 构造函数
    public FTPServerConfig(String server, int port, String user, String pass, String remoteDir, int formatType) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.remoteDir = remoteDir;
        this.formatType = formatType;
    }

    // Getter方法
    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getRemoteDir() {
        return remoteDir;
    }

    public int getFormatType() {
        return formatType;
    }
}