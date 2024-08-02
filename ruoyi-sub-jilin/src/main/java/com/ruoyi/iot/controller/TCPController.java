package com.ruoyi.iot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@RestController
@RequestMapping("/TCP")
public class TCPController {
    /**
     * server
     */
    @GetMapping("/server")
    public void server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(4321);
        System.out.println("服务器启动，等待连接：");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("客户端已连接");

            // 发送数据到客户端
            OutputStream outputStream = socket.getOutputStream();
            byte[] initialData = hexStringToByteArray("25 03 00 00 00 02 C2 EF");
            outputStream.write(initialData);
            outputStream.flush();
            System.out.println("发送数据：" + bytesToHex(initialData, initialData.length));

            // 接收客户端反馈的数据
            InputStream inputStream = socket.getInputStream();
            byte[] receivedBytes = new byte[1024];
            int read = inputStream.read(receivedBytes);
            String receivedMessage = bytesToHex(receivedBytes, read);
            System.out.println("收到客户端反馈数据：" + receivedMessage);

            // 解析客户端反馈数据
            if (read >= 7) {
                System.out.println(receivedBytes[5] & 0xFF);
                int high = receivedBytes[4] & 0xFF;
                int low = receivedBytes[5] & 0xFF;
                int rainData = (high << 8) + low;
                double accumulatedRainfall = rainData / 10.0;
                System.out.println("累计雨量: " + accumulatedRainfall + "mm");
            } else {
                System.out.println("收到的数据长度不足，无法解析累计雨量。");
            }

            // 关闭流和套接字
            inputStream.close();
            outputStream.close();
            socket.close();
        }
    }

    // 将字节数组转换为十六进制字符串
    public static String bytesToHex(byte[] bytes, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString().trim();
    }

    // 将十六进制字符串转换为字节数组
    public static byte[] hexStringToByteArray(String s) {
        s = s.replace(" ", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) {
        String response = "25 03 04 00 04 00 02 5E 31";
        int result = (0x04 * 256) + 0x02;
        System.out.println("计算结果: " + result);
    }



}