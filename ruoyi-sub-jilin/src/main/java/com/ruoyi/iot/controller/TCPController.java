package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.DustMonitoringDevice;
import com.ruoyi.iot.enums.Direction;
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
import java.io.*;
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
import java.util.function.Consumer;

@RestController
@RequestMapping("/TCP")
public class TCPController {
    // 初始化指令和处理方法的映射
    private static final Map<String, BiConsumer<byte[], Map<String, Integer>>> commandHandlers = new HashMap<>();

    static {
        // 初始化指令和对应的处理方法
        commandHandlers.put("66 03 00 00 00 01 8C 1D", TCPController::handleTemperatureResponse);
        commandHandlers.put("66 03 00 01 00 01 DD DD", TCPController::handleHumidityResponse);
        commandHandlers.put("66 03 00 02 00 01 2D DD", TCPController::handlePressureResponse);
        commandHandlers.put("66 03 00 03 00 01 7C 1D", TCPController::handleNoiseResponse);
        commandHandlers.put("8A 03 00 01 00 01 CB 71", TCPController::handlePM2_5Response);
        commandHandlers.put("8A 03 00 02 00 01 3B 71", TCPController::handlePM10Response);
        commandHandlers.put("02 03 00 01 00 01 D5 F9", TCPController::handleWindDirectionResponse);
        commandHandlers.put("01 03 00 00 00 01 84 0A", TCPController::handleWindSpeedResponse);
    }

    private static Map<String, Object> sendMap4322_4321 = new HashMap<>();
    private static Map<String, Object> sendMap4323_4324 = new HashMap<>();

    @Resource
    HdyHttpUtils hdyHttpUtils;

    @Resource
    private IotTspService iotTspService;

    @Autowired
    private IRainService rainService;

    private static final Map<Integer, Consumer<Socket>> portHandlers = new HashMap<>();
    private static ServerSocket serverSocket4322;
    private static ServerSocket serverSocket4321;
    private static ServerSocket serverSocket4323;
    private static ServerSocket serverSocket4324;

    @PostConstruct
    public void init() {
        try {
            serverSocket4322 = new ServerSocket(4322);
            serverSocket4321 = new ServerSocket(4321);
            serverSocket4323 = new ServerSocket(4323);
            serverSocket4324 = new ServerSocket(4324);

            sendMap4322_4321.put("deviceArea", "14#支洞");
            sendMap4323_4324.put("deviceArea", "15#支洞");

            //14#扬尘
            portHandlers.put(4322, socket -> handleClient(socket, sendMap4322_4321, 14));
            //14#雨量
            portHandlers.put(4321, socket -> handleClient4321(socket, sendMap4322_4321));
            //15#支洞扬尘
            portHandlers.put(4323, socket -> handleClient(socket, sendMap4323_4324, 15));
            //15#支洞雨量
            portHandlers.put(4324, socket -> handleClient4321(socket, sendMap4323_4324));

            new Thread(() -> handleConnection(serverSocket4322, portHandlers.get(4322))).start();
            new Thread(() -> handleConnection(serverSocket4321, portHandlers.get(4321))).start();
            new Thread(() -> handleConnection(serverSocket4323, portHandlers.get(4323))).start();
            new Thread(() -> handleConnection(serverSocket4324, portHandlers.get(4324))).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/server4322")
    public void server4322() throws IOException {
        new Thread(() -> handleConnection(serverSocket4322, portHandlers.get(4322))).start();
    }

    @GetMapping("/server4321")
    public void server4321() throws IOException {
        new Thread(() -> handleConnection(serverSocket4321, portHandlers.get(4321))).start();
    }

    @GetMapping("/server4323")
    public void server4323() throws IOException {
        new Thread(() -> handleConnection(serverSocket4323, portHandlers.get(4323))).start();
    }

    @GetMapping("/server4324")
    public void server4324() throws IOException {
        new Thread(() -> handleConnection(serverSocket4324, portHandlers.get(4324))).start();
    }

    private void handleConnection(ServerSocket serverSocket, Consumer<Socket> handler) {
        try {
            Socket socket = serverSocket.accept();
            handler.accept(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket, Map<String, Object> sendMap, int area) {
        try {
            String originalValue = "";
            for (Map.Entry<String, BiConsumer<byte[], Map<String, Integer>>> entry : commandHandlers.entrySet()) {

                String command = entry.getKey();
                BiConsumer<byte[], Map<String, Integer>> handler = entry.getValue();

                OutputStream outputStream = socket.getOutputStream();
                byte[] initialData = hexStringToByteArray(command);
                outputStream.write(initialData);
                outputStream.flush();

                InputStream inputStream = socket.getInputStream();
                byte[] receivedBytes = new byte[1024];
                int read = inputStream.read(receivedBytes);
                originalValue = originalValue + command + "=" + bytesToHex(receivedBytes, read) + ";";
                Map<String, Integer> map = new HashMap<>();
                map.put("area", area);
                map.put("read", read);
                handler.accept(receivedBytes, map);
            }
            IotTsp iotTsp = new IotTsp();
            setIotTsp(iotTsp, sendMap);

            iotTsp.setCreatedDate(DateUtils.getNowDate());
            iotTsp.setThree(originalValue);
            iotTspService.save(iotTsp);

            List<Map<String, Object>> valuesList = new ArrayList<>();
            valuesList.add(sendMap);

            Map<String, Object> rootMap = new HashMap<>();
            rootMap.put("values", valuesList);
            hdyHttpUtils.pushIOT(rootMap, "f69f70f2-9fe6-49e6-bfcf-a062421cb1d2");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double lastCumulativeRainfall01 = 0.0;
    private static double lastCumulativeRainfall02 = 0.0;


    private void handleClient4321(Socket socket, Map<String, Object> sendMap) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            byte[] initialData = hexStringToByteArray("25 03 00 00 00 02 C2 EF");
            outputStream.write(initialData);
            outputStream.flush();

            InputStream inputStream = socket.getInputStream();
            byte[] receivedBytes = new byte[1024];
            int read = inputStream.read(receivedBytes);

            if (read >= 8) {
                int value1 = receivedBytes[4] & 0xFF;
                int value2 = receivedBytes[6] & 0xFF;
                int result = (value1 * 256) + value2;
                double currentCumulativeRainfall = result / 10.0;

                Rain rain = new Rain();


                if (sendMap.get("deviceArea").toString().contains("14#支洞")) {
                    rain.setDeviceCode("2407052002LXY-02");
                    double rainfallDifference = currentCumulativeRainfall - lastCumulativeRainfall02;
                    if (rainfallDifference < 0 || lastCumulativeRainfall02 == 0) {
                        rainfallDifference = 0;
                    }

                    lastCumulativeRainfall02 = currentCumulativeRainfall;

                    currentCumulativeRainfall = rainfallDifference;

                } else {
                    rain.setDeviceCode("2407052002LXY-01");
                    double rainfallDifference = currentCumulativeRainfall - lastCumulativeRainfall01;
                    if (rainfallDifference < 0 || lastCumulativeRainfall01 == 0) {
                        rainfallDifference = 0;
                    }
                    lastCumulativeRainfall01 = currentCumulativeRainfall;

                    currentCumulativeRainfall = rainfallDifference;


                    System.out.println("2407052002LXY-01");
                }

                sendMap.put("rainfall", currentCumulativeRainfall);

                System.out.println("雨量:currentCumulativeRainfall:"+currentCumulativeRainfall+";lastCumulativeRainfall01:"+lastCumulativeRainfall01+";lastCumulativeRainfall02:"+lastCumulativeRainfall01);

                rain.setRainfall(new BigDecimal(sendMap.get("rainfall").toString()));
                rain.setRemark(bytesToHex(receivedBytes, read));
                System.out.println("插入内容反馈："+rainService.insertRain(rain));

            }
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setIotTsp(IotTsp iotTsp, Map<String, Object> sendMap) {
        String now = DateUtil.now();
        iotTsp.setPmTwoFive(sendMap.get("pm25").toString());
        iotTsp.setDevId("54");
        iotTsp.setPmTen(sendMap.get("pm10").toString());
        iotTsp.setNoise(sendMap.get("noise").toString());
        iotTsp.setWindSpeed(sendMap.get("wind_speed").toString());
        iotTsp.setWindDirection(sendMap.get("wind_direction").toString());
        iotTsp.setTemperature(sendMap.get("temperature").toString());
        iotTsp.setHumidity(sendMap.get("humidity").toString());
        iotTsp.setPressure(sendMap.get("pressure").toString());
        iotTsp.setDeviceArea(sendMap.get("deviceArea").toString());
        String rainDeviceCode = "";
        if (iotTsp.getDeviceArea().equals("14#支洞")) {
            rainDeviceCode = "2407052002LXY-02";
            sendMap.put("device_code", "2407052002LXY-02");
        } else {
            rainDeviceCode = "2407052002LXY-01";
            sendMap.put("device_code", "2407052002LXY-01");
        }
        Rain lastMonitor = rainService.getOne(new LambdaQueryWrapper<Rain>()
                .eq(Rain::getDeviceCode, rainDeviceCode)
                .orderByDesc(Rain::getCreateTime).last("limit 1"));
        if (lastMonitor != null) {
            BigDecimal currentRainfall = new BigDecimal(sendMap.get("rainfall").toString());
            BigDecimal lastRainfall = lastMonitor.getRainfall();

            // 进行减法运算
            BigDecimal result = currentRainfall.subtract(lastRainfall).setScale(2, RoundingMode.HALF_UP);

            // 取绝对值以确保结果为正数
            lastRainfall = lastRainfall.abs();
            sendMap.put("rainfall", lastRainfall);
        } else {
            BigDecimal bigDecimal = new BigDecimal(sendMap.get("rainfall").toString());
            sendMap.put("rainfall", bigDecimal.abs());
        }
        sendMap.put("status", "在线");
        sendMap.put("date_time", now);
        sendMap.put("push_time", now);
        sendMap.put("portal_id", "1751847977770553345");
        sendMap.put("other", "");
    }

    private static void handleTemperatureResponse(byte[] receivedBytes, Map<String, Integer> map) {
        if (map.get("read") >= 5) {
            // 获取高字节和低字节
            int highByte = receivedBytes[3] & 0xFF; // 解析接收的高字节
            int lowByte = receivedBytes[4] & 0xFF;  // 解析接收的低字节

            // 组合高字节和低字节形成温度值
            int temperature = (highByte << 8) | lowByte;

            // 检查并调整温度值以处理负值
            if (temperature >= 32768) {
                temperature -= 65536; // 调整温度为负数
            }

            // 计算实际温度
            double actualTemperature = temperature / 100.0;

            // 存储温度值到相应的映射中
            if (map.get("area") == 14) {
                sendMap4322_4321.put("temperature", actualTemperature);
            } else {
                sendMap4323_4324.put("temperature", actualTemperature);
            }
        }
    }

    private static void handleHumidityResponse(byte[] receivedBytes, Map<String, Integer> map) {
        if (map.get("read") >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int humidity = (highByte << 8) | lowByte;
            double actualHumidity = humidity / 100.0;
            if (map.get("area") == 14) {
                sendMap4322_4321.put("humidity", actualHumidity);
            } else {
                sendMap4323_4324.put("humidity", actualHumidity);
            }

        }
    }


    private static void rainResponse(byte[] receivedBytes, Map<String, Integer> map) {
        if (map.get("read") >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int humidity = (highByte << 8) | lowByte;
            double actualHumidity = humidity / 100.0;
            if (map.get("area") == 14) {
                sendMap4322_4321.put("humidity", actualHumidity);
            } else {
                sendMap4323_4324.put("humidity", actualHumidity);
            }

        }
    }

    private static void handlePressureResponse(byte[] receivedBytes, Map<String, Integer> map) {
        if (map.get("read") >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int pressure = (highByte << 8) | lowByte;
            double actualPressure = pressure / 100.0;
            if (map.get("area") == 14) {
                sendMap4322_4321.put("pressure", actualPressure);
            } else {
                sendMap4323_4324.put("pressure", actualPressure);
            }

        }
    }

    private static void handleNoiseResponse(byte[] receivedBytes, Map<String, Integer> map) {
        if (map.get("read") >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int noise = (highByte << 8) | lowByte;
            double actualNoise = noise / 10.0;
            if (map.get("area") == 14) {
                sendMap4322_4321.put("noise", actualNoise);
            } else {
                sendMap4323_4324.put("noise", actualNoise);
            }
        }
    }

    private static void handlePM2_5Response(byte[] receivedBytes, Map<String, Integer> map) {
        if (map.get("read") >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int PM2_5 = (highByte << 8) | lowByte;

            if (map.get("area") == 14) {
                sendMap4322_4321.put("pm25", PM2_5);
            } else {
                sendMap4323_4324.put("pm25", PM2_5);
            }
        }
    }

    private static void handlePM10Response(byte[] receivedBytes, Map<String, Integer> map) {
        if (map.get("read") >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int PM10 = (highByte << 8) | lowByte;
            if (map.get("area") == 14) {
                sendMap4322_4321.put("pm10", PM10);
            } else {
                sendMap4323_4324.put("pm10", PM10);
            }
        }
    }

    private static void handleWindDirectionResponse(byte[] receivedBytes, Map<String, Integer> map) {
        if (map.get("read") >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int WindWirection = (highByte << 8) | lowByte;
            String direction = Direction.fromAngle(String.valueOf(WindWirection));
            if (map.get("area") == 14) {
                sendMap4322_4321.put("wind_direction", direction);
            } else {
                sendMap4323_4324.put("wind_direction", direction);
            }
        }
    }

    private static void handleWindSpeedResponse(byte[] receivedBytes, Map<String, Integer> map) {
        if (map.get("read") >= 5) {
            int highByte = receivedBytes[3] & 0xFF;
            int lowByte = receivedBytes[4] & 0xFF;
            int WindSpeed = (highByte << 8) | lowByte;
            double WindSpeedDouble = WindSpeed * 0.1;
            if (map.get("area") == 14) {
                sendMap4322_4321.put("wind_speed", WindSpeedDouble);
            } else {
                sendMap4323_4324.put("wind_speed", WindSpeedDouble);
            }
        }
    }

    public static String bytesToHex(byte[] bytes, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString().trim();
    }

    public static byte[] hexStringToByteArray(String s) {
        s = s.replace(" ", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


    /**
     * 项目启动时自动启动服务端。
     */
    @PostConstruct
    public void initTcpServerOnStartup() {
        new Thread(this::startTcpJsonServer).start();
        System.out.println("TCP JSON Server has been started!");
    }


    private static final String SERVER_IP = "10.1.3.204"; // 固定的服务器 IP
    private static final int SERVER_PORT = 4326; // 固定的端口号

    /**
     * 启动 TCP 服务端。
     */
    public void startTcpJsonServer() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("TCP JSON Server started at " + SERVER_IP + ":" + SERVER_PORT);

            while (true) {
                // 等待客户端连接
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // 为每个客户端连接启动一个新线程
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error in TCP server: " + e.getMessage());
        }
    }


    private static double lastCumulativeRainfall03 = 0.0;

    /**
     * 处理客户端连接。
     *
     * @param clientSocket 客户端 Socket
     */
    private void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "ASCII"));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "ASCII"), true)
        ) {
            // 发送欢迎消息给客户端
            out.println("Welcome to the TCP JSON Server!");
            out.flush();

            String receivedData;
            while ((receivedData = in.readLine()) != null) {

                Map<String, Object> sendMap = new HashMap<>();

                // 尝试解析接收的 JSON 数据
                try {
                    JSONObject jsonObject = JSONObject.parseObject(receivedData);
                    IotTsp iotTsp = new IotTsp();
                    String now = DateUtil.now();
                    iotTsp.setPmTwoFive(jsonObject.get("PM25").toString());
                    iotTsp.setDevId("54");
                    iotTsp.setPmTen(jsonObject.get("PM10").toString());
                    iotTsp.setNoise(jsonObject.get("noise").toString());
                    iotTsp.setWindSpeed(jsonObject.get("windSpeed").toString());
                    iotTsp.setWindDirection(jsonObject.get("windDirection").toString());
                    iotTsp.setTemperature("0.0");
                    iotTsp.setHumidity("0.0");
                    iotTsp.setPressure("0.0");
                    iotTsp.setDeviceArea("13#支洞");

                    Rain rain = new Rain();

                    String rainDeviceCode = "2407052002LXY-03";
                    rain.setDeviceCode(rainDeviceCode);

                    double currentCumulativeRainfall = jsonObject.getDouble("rainfall");

                    double rainfallDifference = currentCumulativeRainfall - lastCumulativeRainfall03;
                    if (rainfallDifference < 0 || lastCumulativeRainfall03 == 0) {
                        rainfallDifference = 0;
                    }

                    lastCumulativeRainfall03 = currentCumulativeRainfall;

                    currentCumulativeRainfall = rainfallDifference;


                   String rainfall = String.valueOf(currentCumulativeRainfall);

                    rain.setRainfall(new BigDecimal(rainfall));

                    rainService.insertRain(rain);


                    Rain lastMonitor = rainService.getOne(new LambdaQueryWrapper<Rain>()
                            .eq(Rain::getDeviceCode, rainDeviceCode)
                            .orderByDesc(Rain::getCreateTime).last("limit 1"));
                    if (lastMonitor != null) {
                        BigDecimal currentRainfall = new BigDecimal(rainfall);
                        BigDecimal lastRainfall = lastMonitor.getRainfall();

                        // 进行减法运算
                        BigDecimal result = currentRainfall.subtract(lastRainfall).setScale(2, RoundingMode.HALF_UP);

                        // 取绝对值以确保结果为正数
                        lastRainfall = lastRainfall.abs();
                        sendMap.put("rainfall", lastRainfall);
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(sendMap.get("rainfall").toString());
                        sendMap.put("rainfall", bigDecimal.abs());
                    }
                    sendMap.put("status", "在线");
                    sendMap.put("date_time", now);
                    sendMap.put("push_time", now);
                    sendMap.put("portal_id", "1751847977770553345");
                    sendMap.put("other", "");
                    sendMap.put("device_code", rainDeviceCode);
                    sendMap.put("pm25", iotTsp.getPmTwoFive());
                    sendMap.put("pm10", iotTsp.getPmTen());
                    sendMap.put("noise", iotTsp.getNoise());
                    sendMap.put("wind_speed", iotTsp.getWindSpeed());
                    sendMap.put("wind_direction", iotTsp.getWindDirection());
                    sendMap.put("temperature", iotTsp.getTemperature());
                    sendMap.put("humidity", iotTsp.getHumidity());
                    sendMap.put("pressure", iotTsp.getPressure());

                    iotTsp.setCreatedDate(DateUtils.getNowDate());
                    iotTsp.setThree(jsonObject.toJSONString());
                    iotTspService.save(iotTsp);


                } catch (Exception e) {
                    System.err.println("Failed to parse JSON: " + e.getMessage());
                    out.println("Error: Invalid JSON format");
                }

                List<Map<String, Object>> valuesList = new ArrayList<>();
                valuesList.add(sendMap);

                Map<String, Object> rootMap = new HashMap<>();
                rootMap.put("values", valuesList);
                hdyHttpUtils.pushIOT(rootMap, "f69f70f2-9fe6-49e6-bfcf-a062421cb1d2");

            }

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }


}