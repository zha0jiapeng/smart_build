package com.ruoyi.iot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.iot.domain.DustMonitoringDevice;
import com.ruoyi.iot.service.IDustMonitoringDeviceService;
import com.ruoyi.iot.utils.HdyHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("/TCP")
public class TCPController {
    // 初始化指令和处理方法的映射
    private static final Map<String, BiConsumer<byte[], Integer>> commandHandlers = new HashMap<>();

    static {
        // 初始化指令和对应的处理方法
        commandHandlers.put("66 03 00 00 00 01 8C 1D", TCPController::handleTemperatureResponse);
//        commandHandlers.put("8A 03 00 00 00 01 9A B1", TCPController::handleCO2Response);
        commandHandlers.put("66 03 00 01 00 01 DD DD", TCPController::handleHumidityResponse);
        commandHandlers.put("66 03 00 02 00 01 2D DD", TCPController::handlePressureResponse);
        commandHandlers.put("66 03 00 03 00 01 7C 1D", TCPController::handleNoiseResponse);
        commandHandlers.put("8A 03 00 01 00 01 CB 71", TCPController::handlePM2_5Response);
        commandHandlers.put("8A 03 00 02 00 01 3B 71", TCPController::handlePM10Response);
//        commandHandlers.put("01 03 00 00 00 01 84 0A", TCPController::handleWindDirectionResponse);
//        commandHandlers.put("02 03 00 01 00 01 D5 F9", TCPController::handleWinSpeedResponse);
    }

    private static Map<String, String> sendMap = new HashMap<>();

    @Resource
    HdyHttpUtils hdyHttpUtils;

    @Autowired
    private IDustMonitoringDeviceService dustMonitoringDeviceService;

    @PostConstruct
    public void init() {
        // 启动服务器
        new Thread(() -> {
            try {
                server4321();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                server4322();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    @GetMapping("/server4322")
    public void server4322() throws IOException {
        ServerSocket serverSocket = new ServerSocket(4322);
        System.out.println("服务器启动，等待连接：");
        while (true) {
            Socket socket = serverSocket.accept();
//            System.out.println("客户端已连接");
//            sendMap = new HashMap<>();
            for (Map.Entry<String, BiConsumer<byte[], Integer>> entry : commandHandlers.entrySet()) {
                String command = entry.getKey();
                BiConsumer<byte[], Integer> handler = entry.getValue();

                // 发送数据到客户端
                OutputStream outputStream = socket.getOutputStream();
                byte[] initialData = hexStringToByteArray(command);
                outputStream.write(initialData);
                outputStream.flush();
//                System.out.println("发送数据：" + bytesToHex(initialData, initialData.length));

                // 接收客户端反馈的数据
                InputStream inputStream = socket.getInputStream();
                byte[] receivedBytes = new byte[1024];
                int read = inputStream.read(receivedBytes);
                String receivedMessage = bytesToHex(receivedBytes, read);
//                System.out.println("收到客户端反馈数据：" + receivedMessage);

                // 调用对应的处理方法
                handler.accept(receivedBytes, read);
            }
            DustMonitoringDevice dustMonitoringDevice = new DustMonitoringDevice();
            sendMap.put("deviceCode", "2407052002LXY-02");
            dustMonitoringDevice.setDeviceCode("2407052002LXY-02");
            sendMap.put("workStatus", "在线");
            dustMonitoringDevice.setWorkStatus("在线");
            dustMonitoringDevice.setPm25(sendMap.get("pm25"));
            dustMonitoringDevice.setPm10(sendMap.get("pm10"));
            dustMonitoringDevice.setNoise(sendMap.get("noise"));
            dustMonitoringDevice.setWindSpeed(sendMap.get(""));
            sendMap.put("windSpeed", "");
            dustMonitoringDevice.setWindDirection(sendMap.get(""));
            sendMap.put("windDirection", "");
            dustMonitoringDevice.setTemperature(sendMap.get("temperature"));
            dustMonitoringDevice.setHumidity(sendMap.get("humidity"));
            dustMonitoringDevice.setPressure(sendMap.get("pressure"));
            dustMonitoringDevice.setRainfall(sendMap.get("rainfall"));
            dustMonitoringDevice.setStatus(sendMap.get("在线"));
            sendMap.put("status", "在线");
            // 获取当前时间
            LocalDateTime now = LocalDateTime.now();

            // 定义时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 格式化当前时间
            String formattedNow = now.format(formatter);
            sendMap.put("pushTime",formattedNow);
            dustMonitoringDevice.setPushTime(sendMap.get("pushTime"));
            sendMap.put("other","");
            dustMonitoringDevice.setOther(sendMap.get("other"));
            dustMonitoringDevice.setProtalId("1751847977770553345");
            dustMonitoringDevice.setSubProjectId("1763492186013306882");
            hdyHttpUtils.pushIOT(sendMap);
            dustMonitoringDeviceService.insertDustMonitoringDevice(dustMonitoringDevice);
            // 关闭流和套接字
            socket.close();
        }
    }

    // 处理温度响应的方法
    private static void handleTemperatureResponse(byte[] receivedBytes, int read) {
        if (read >= 5) { // 确保接收到的字节数足够
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int temperature = (highByte << 8) | lowByte;
            double actualTemperature = temperature / 100.0;
            sendMap.put("temperature", actualTemperature + "°C");
        }
    }

//    // 处理CO2响应的方法
//    private static void handleCO2Response(byte[] receivedBytes, int read) {
//        if (read >= 5) { // 确保接收到的字节数足够
//            int highByte = receivedBytes[3] & 0xFF;
//            int lowByte = receivedBytes[4] & 0xFF;
//            int CO2 = (highByte << 8) | lowByte;
//            System.out.println("CO2: " + CO2 + " ppm");
//        }
//    }

    // 处理湿度响应的方法
    private static void handleHumidityResponse(byte[] receivedBytes, int read) {
        if (read >= 5) { // 确保接收到的字节数足够
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int humidity = (highByte << 8) | lowByte;
            double actualHumidity = humidity / 100.0;
            sendMap.put("humidity", actualHumidity + "%RH");
        }
    }

    // 处理气压响应的方法
    private static void handlePressureResponse(byte[] receivedBytes, int read) {
        if (read >= 5) { // 确保接收到的字节数足够
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int pressure = (highByte << 8) | lowByte;
            double actualPressure = pressure / 100.0;
            sendMap.put("pressure", actualPressure + "Kpa");
        }
    }

    // 处理噪声响应的方法
    private static void handleNoiseResponse(byte[] receivedBytes, int read) {
        if (read >= 5) { // 确保接收到的字节数足够
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int noise = (highByte << 8) | lowByte;
            double actualNiose = noise / 10.0;
        }
    }

    // 处理PM2.5响应的方法
    private static void handlePM2_5Response(byte[] receivedBytes, int read) {
        if (read >= 5) { // 确保接收到的字节数足够
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int PM2_5 = (highByte << 8) | lowByte;
            sendMap.put("PM25", PM2_5 + "ug/m³");
        }
    }

    // 处理PM10响应的方法
    private static void handlePM10Response(byte[] receivedBytes, int read) {
        if (read >= 5) { // 确保接收到的字节数足够
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int PM10 = (highByte << 8) | lowByte;
            sendMap.put("PM10", PM10 + "ug/m³");
        }
    }

    // 处理风向响应的方法
    private static void handleWindDirectionResponse(byte[] receivedBytes, int read) {
        if (read >= 5) { // 确保接收到的字节数足够
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int windDirection = (highByte << 8) | lowByte;
            sendMap.put("windDirection", String.valueOf(windDirection));
        }
    }

    // 处理风速响应的方法
    private static void handleWinSpeedResponse(byte[] receivedBytes, int read) {
        if (read >= 5) { // 确保接收到的字节数足够
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int windSpeed = (highByte << 8) | lowByte;
            double actualWindSpeed = windSpeed / 10.0;
            sendMap.put("windSpeed", actualWindSpeed + "m/s");
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


    /**
     * server
     */
    @GetMapping("/server4321")
    public void server4321() throws IOException {
        ServerSocket serverSocket = new ServerSocket(4321);
        System.out.println("服务器4321启动，等待连接：");

        while (true) {
            Socket socket = serverSocket.accept();
//            System.out.println("客户端已连接4321");

            // 发送数据到客户端
            OutputStream outputStream = socket.getOutputStream();
            byte[] initialData = hexStringToByteArray("25 03 00 00 00 02 C2 EF");
            outputStream.write(initialData);
            outputStream.flush();

            // 接收客户端反馈的数据
            InputStream inputStream = socket.getInputStream();
            byte[] receivedBytes = new byte[1024];
            int read = inputStream.read(receivedBytes);
            String receivedMessage = bytesToHex(receivedBytes, read);
//            System.out.println("收到客户端反馈数据：" + receivedMessage);

            // 提取并计算特定字节的数据
            if (read >= 8) { // 确保接收到的字节数足够
                int value1 = receivedBytes[4] & 0xFF; // 取第五个字节
                int value2 = receivedBytes[6] & 0xFF; // 取第七个字节
                int result = (value1 * 256) + value2;
                double resultDouble = result / 10.0;
                if (sendMap == null){
                    System.out.println("sendMap为null");
                    sendMap = new HashMap<>();
                }
                sendMap.put("rainfall", resultDouble + "mm");
            }
            // 关闭流和套接字
            inputStream.close();
            outputStream.close();
            socket.close();
        }
    }
}