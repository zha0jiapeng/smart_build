package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.DustMonitoringDevice;
import com.ruoyi.iot.service.IDustMonitoringDeviceService;
import com.ruoyi.iot.utils.HdyHttpUtils;
import com.ruoyi.system.domain.basic.IotTsp;
import com.ruoyi.system.domain.basic.Rain;
import com.ruoyi.system.mapper.IotTspMapper;
import com.ruoyi.system.service.IRainService;
import com.ruoyi.system.service.IotTspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static Map<String, Object> sendMap = new HashMap<>();

    @Resource
    HdyHttpUtils hdyHttpUtils;

    @Resource
    private IotTspService iotTspService;

    @Autowired
    private IRainService rainService;

    private static ServerSocket serverSocket4322;
    private static ServerSocket serverSocket4321;

    @PostConstruct
    public void init() {
        try {
            serverSocket4322 = new ServerSocket(4322);
            serverSocket4322.setReuseAddress(true);

            serverSocket4321 = new ServerSocket(4321);
            serverSocket4321.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/server4322")
    public void server4322() throws IOException {
        new Thread(this::handleConnection4322).start();
    }

    private void handleConnection4322() {
        try {
            Socket socket = serverSocket4322.accept();
            handleClient(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try {
            // 处理客户端连接
            for (Map.Entry<String, BiConsumer<byte[], Integer>> entry : commandHandlers.entrySet()) {
                String command = entry.getKey();
                BiConsumer<byte[], Integer> handler = entry.getValue();

                // 发送数据到客户端
                OutputStream outputStream = socket.getOutputStream();
                byte[] initialData = hexStringToByteArray(command);
                outputStream.write(initialData);
                outputStream.flush();

                // 接收客户端反馈的数据
                InputStream inputStream = socket.getInputStream();
                byte[] receivedBytes = new byte[1024];
                int read = inputStream.read(receivedBytes);
                String receivedMessage = bytesToHex(receivedBytes, read);

                // 调用对应的处理方法
                handler.accept(receivedBytes, read);
            }

            IotTsp iotTsp = new IotTsp();
            setIotTsp(iotTsp);

            // 插入数据库
            iotTsp.setCreatedDate(DateUtils.getNowDate());
            iotTspService.save(iotTsp);

            // 创建values的List并添加valueMap
            List<Map<String, Object>> valuesList = new ArrayList<>();
            valuesList.add(sendMap);

            // 创建根Map
            Map<String, Object> rootMap = new HashMap<>();
            rootMap.put("values", valuesList);
            hdyHttpUtils.pushIOT(rootMap,"f69f70f2-9fe6-49e6-bfcf-a062421cb1d2");

            // 关闭流和套接字
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/server4321")
    public void server4321() throws IOException {
        new Thread(this::handleConnection4321).start();
    }

    private void handleConnection4321() {
        try {
            Socket socket = serverSocket4321.accept();
            handleClient4321(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient4321(Socket socket) {
        try {
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

            // 提取并计算特定字节的数据
            if (read >= 8) {
                int value1 = receivedBytes[4] & 0xFF;
                int value2 = receivedBytes[6] & 0xFF;
                int result = (value1 * 256) + value2;
                double resultDouble = result / 10.0;
                if (sendMap == null) {
                    sendMap = new HashMap<>();
                }
                sendMap.put("rainfall", resultDouble);

                Rain rain = new Rain();
                rain.setRainfall(new BigDecimal(sendMap.get("rainfall").toString()));
                rain.setDeviceCode("2407052002LXY-02");
                rainService.insertRain(rain);
            }
            // 关闭流和套接字
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setIotTsp(IotTsp iotTsp) {
        String now = DateUtil.now();
        sendMap.put("device_code", "2407052002LXY-02");
        iotTsp.setPmTwoFive(sendMap.get("pm25").toString());
        iotTsp.setDevId("54");
        iotTsp.setPmTen(sendMap.get("pm10").toString());
        iotTsp.setNoise(sendMap.get("noise").toString());
        sendMap.put("wind_speed", "0");
        iotTsp.setWindSpeed("0");
        sendMap.put("wind_direction", "");
        iotTsp.setWindDirection("");
        iotTsp.setTemperature(sendMap.get("temperature").toString());
        iotTsp.setHumidity(sendMap.get("humidity").toString());
        iotTsp.setPressure(sendMap.get("pressure").toString());
        Rain lastMonitor = rainService.getOne(new LambdaQueryWrapper<Rain>()
                        .eq(Rain::getDeviceCode, "2407052002LXY-02")
                        .orderByDesc(Rain::getCreateTime).last("limit 1")
                , false);
        if(lastMonitor!=null){
            sendMap.put("rainfall", new BigDecimal(sendMap.get("rainfall").toString()).subtract(lastMonitor.getRainfall()).setScale(2, RoundingMode.HALF_UP).toString());
        }else {
            sendMap.put("rainfall", new BigDecimal(sendMap.get("rainfall").toString()));
        }
        sendMap.put("status", "在线");
        sendMap.put("date_time", now);
        sendMap.put("push_time", now);
        sendMap.put("portal_id", "1751847977770553345");
        sendMap.put("other", "");
    }

    // 处理温度响应的方法
    private static void handleTemperatureResponse(byte[] receivedBytes, int read) {
        if (read >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int temperature = (highByte << 8) | lowByte;
            double actualTemperature = temperature / 100.0;
            sendMap.put("temperature", actualTemperature);
        }
    }

    // 处理湿度响应的方法
    private static void handleHumidityResponse(byte[] receivedBytes, int read) {
        if (read >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int humidity = (highByte << 8) | lowByte;
            double actualHumidity = humidity / 100.0;
            sendMap.put("humidity", actualHumidity);
        }
    }

    // 处理气压响应的方法
    private static void handlePressureResponse(byte[] receivedBytes, int read) {
        if (read >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int pressure = (highByte << 8) | lowByte;
            double actualPressure = pressure / 100.0;
            sendMap.put("pressure", actualPressure );
        }
    }

    // 处理噪声响应的方法
    private static void handleNoiseResponse(byte[] receivedBytes, int read) {
        if (read >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int noise = (highByte << 8) | lowByte;
            double actualNoise = noise / 10.0;
            sendMap.put("noise", actualNoise );
        }
    }

    // 处理PM2.5响应的方法
    private static void handlePM2_5Response(byte[] receivedBytes, int read) {
        if (read >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int PM2_5 = (highByte << 8) | lowByte;
            sendMap.put("pm25", PM2_5);
        }
    }

    // 处理PM10响应的方法
    private static void handlePM10Response(byte[] receivedBytes, int read) {
        if (read >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int PM10 = (highByte << 8) | lowByte;
            sendMap.put("pm10", PM10 );
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
}