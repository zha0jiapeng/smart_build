package com.ruoyi.iot.controller;


import com.ruoyi.iot.scheduling.DeviceIpChecker;
import com.ruoyi.iot.scheduling.access.FTPServer;
import com.ruoyi.iot.scheduling.access.FTPServerConfig;
import com.ruoyi.iot.utils.HdyHttpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拌合站
 */
@RestController
@RequestMapping("/MixingPlant")
public class MixingPlantController {

    @Resource
    HdyHttpUtils hdyHttpUtils;


    /**
     * 实验室设备运行状态推送
     */
    @GetMapping("/getEquipmentStatus")
    public void getEquipmentStatus() {
        //全自动抗折抗压恒应力试验机10.1.3.150 出厂编号：2404129
        pushEquipmentStatus("2404129", "10.1.3.150");

        //全自动恒应力压力试验机10.1.3.151 出厂编号：2405066
        pushEquipmentStatus("2405066", "10.1.3.151");

        //电液伺服万能材料试验机-出厂编号：2401059 10.1.3.152
        pushEquipmentStatus("2401059", "10.1.3.152");

        //电液伺服万能材料试验机-出厂编号：2401053 10.1.3.153
        pushEquipmentStatus("2401053", "10.1.3.153");

        //电液伺服万能材料试验机-出厂编号：2401052 10.1.3.154
        pushEquipmentStatus("2401052", "10.1.3.154");
    }

    public void pushEquipmentStatus(String factoryNumber, String ip) {
        String dataTime = getNowTimeExtractor();
        // 发送请求
        boolean value = new DeviceIpChecker().ping(ip);
        String status = "关机";
        if (value) {
            status = "工作中";
        }

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("portal_id", "1751847977770553345");
        valueMap.put("device_code", factoryNumber);
        valueMap.put("data_time", dataTime);
        valueMap.put("status", status);
        valueMap.put("push_time", getNowTimeExtractor());
        valueMap.put("other", "");
        List<Map<String, Object>> values = new ArrayList<>();
        values.add(valueMap);
        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put("values", values);

        hdyHttpUtils.pushIOT(param, "4249b29e-391d-41ab-9e34-6b9b6df49920");
    }


    /**
     * 实验室环境监测推送
     */
    @GetMapping("/getEnvironmentalMonitoring")
    public void getEnvironmentalMonitoring() {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("portal_id", "1751847977770553345");
        //温度
        valueMap.put("temperature", "");
        //湿度
        valueMap.put("humidness", "");
        valueMap.put("push_time", getNowTimeExtractor());
        valueMap.put("other", "");
        List<Map<String, Object>> values = new ArrayList<>();
        values.add(valueMap);
        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put("values", values);

        hdyHttpUtils.pushIOT(param, "2f745c53-8376-4c10-b650-8cc66aed2cce");
    }

    /**
     * 电液伺服万能材料试验机
     */
    @GetMapping("/getElectroHydraulicServoUniversalTester")
    public void getElectroHydraulicServoUniversalTester() {

        Map<String, String> map = new HashMap<>();
        map.put("10.1.3.152","2401059");
        map.put("10.1.3.153","2401053");
        map.put("10.1.3.154","2401052");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String ip = entry.getKey();
            String deviceCode = entry.getValue();
            FTPServer ftpServer = new FTPServer();
            String localUrl = "/";
            FTPServerConfig ftpServerConfig = new FTPServerConfig(ip, 21, "yuancheng", "123456", localUrl, 1);
            String localFilePath = "/home/mashir0/project/Normal.mdb";
            ftpServer.processFTPServer(ftpServerConfig, localFilePath);
            //然后读取
            String[] columnsToPrint = {"SBBH", "TestNo", "CurOrder", "Area", "TestBTime", "Operator", "MaxLoad", "KYQD", "断后标距", "Ae", "Agt", "Lagt", "Gauge", "TestPoint"}; // 你想打印的列
            List<Map<String, String>> mapList = getAccess(localFilePath, columnsToPrint);
            //然后解析
            for (Map<String, String> value : mapList) {
                String dataTime = getNowTimeExtractor();
                Map<String, Object> valueMap = new HashMap<>();
                valueMap.put("portal_id", "1751847977770553345");
                valueMap.put("date_time", dataTime);
                valueMap.put("device_code", value.get("SBBH"));
                valueMap.put("num_code", value.get("TestNo"));
                valueMap.put("test_piece_code", value.get("CurOrder"));
                valueMap.put("test_piece_seq", value.get("CurOrder"));
                valueMap.put("nominal_area", value.get("Area"));
                valueMap.put("test_day", value.get("TestBTime"));
                valueMap.put("tester", value.get("Operator"));
                valueMap.put("checker", "");
                valueMap.put("yield_force", "");
                valueMap.put("yield_strength", "");
                valueMap.put("max_force", value.get("MaxLoad"));
                valueMap.put("tensile_strength", value.get("KYQD"));
                valueMap.put("pe_at_ogl", value.get("Gauge"));
                valueMap.put("pe_at_fracture", value.get("断后标距"));
                valueMap.put("percent_elongation", value.get("Ae"));
                valueMap.put("max_force_total_extension", value.get("Gauge"));
                valueMap.put("ext_at_max_force", value.get("Lagt"));
                valueMap.put("total_extension", value.get("Agt"));
                valueMap.put("push_time", getNowTimeExtractor());
                valueMap.put("other", "");
                valueMap.put("curve", value.get("TestPoint"));
                List<Map<String, Object>> values = new ArrayList<>();
                values.add(valueMap);
                Map<String, List<Map<String, Object>>> param = new HashMap<>();
                param.put("values", values);

                hdyHttpUtils.pushIOT(param, "2f745c53-8376-4c10-b650-8cc66aed2cce");
            }
        }

    }


    /**
     * 实验室水泥抗压抗折试验上传
     */
    @GetMapping("/getCementStrengthTestingInterface")
    public void getCementStrengthTestingInterface() {
        //先下载文件

        FTPServer ftpServer = new FTPServer();
        String localUrl = "/";
        FTPServerConfig ftpServerConfig = new FTPServerConfig("10.1.3.151", 21, "yuancheng", "123456", localUrl, 1);
        String localFilePath = "/home/mashir0/project/Normal.mdb";
        ftpServer.processFTPServer(ftpServerConfig, localFilePath);
        //然后读取
        String[] columnsToPrint = {""};
        List<Map<String, String>> mapList = getAccess(localFilePath, columnsToPrint);
        //然后解析
        for (Map<String, String> value : mapList) {
            String dataTime = getNowTimeExtractor();
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("portal_id", "1751847977770553345");
            valueMap.put("date_time ", dataTime);
            valueMap.put("device_code ", "2404129");
            valueMap.put("num_code ", " ");
            valueMap.put("test_piece_code ", " ");
            valueMap.put("test_piece_seq ", " ");
            valueMap.put("molding_date_time ", " ");
            valueMap.put("compressive_age ", " ");
            valueMap.put("test_day ", " ");
            valueMap.put("tester ", " ");
            valueMap.put("checker ", " ");
            valueMap.put("flexural_strength_at_side_len ", " ");
            valueMap.put("flexural_strength_at_span ", " ");
            valueMap.put("flexural_strength_at_load_failure ", " ");
            valueMap.put("flexural_strength_at_single ", " ");
            valueMap.put("flexural_strength_at_avg ", " ");
            valueMap.put("compressive_strength_at_area ", " ");
            valueMap.put("compressive_strength_at_load_failure ", " ");
            valueMap.put("compressive_strength_at_single ", " ");
            valueMap.put("compressive_strength_at_avg ", " ");
            valueMap.put("push_time", getNowTimeExtractor());
            valueMap.put("other", "");
            List<Map<String, Object>> values = new ArrayList<>();
            values.add(valueMap);
            Map<String, List<Map<String, Object>>> param = new HashMap<>();
            param.put("values", values);
            hdyHttpUtils.pushIOT(param, "2f745c53-8376-4c10-b650-8cc66aed2cce");
        }
    }


    /**
     * 读取本地Access数据库并打印内容
     */
    public List<Map<String, String>> getAccess(String databasePath, String[] columnsToPrint) {
        // 构建 UCanAccess 连接 URL，明确设置密码为空
        // 如果数据库文件有密码保护，包含密码
        String url = "jdbc:ucanaccess://" + databasePath + ";password=oke";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Map<String, String>> mapList = new ArrayList<>();

        try {
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
            mapList = printSelectedColumns(resultSet, columnsToPrint);

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
        return mapList;
    }

    /**
     * 打印ResultSet内容
     */
    public List<Map<String, String>> printSelectedColumns(ResultSet resultSet, String[] columnsToPrint) {
        List<Map<String, String>> mapList = new ArrayList<>();
        try {
            // 遍历 ResultSet 并打印每一行的指定列
            while (resultSet.next()) {
                Map<String, String> map = new HashMap<>();
                for (String columnName : columnsToPrint) {
                    String value = resultSet.getString(columnName);
                    map.put(columnName, value);
                }
                mapList.add(map);
            }
            System.out.println(mapList.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    public String getNowTimeExtractor() {
        //推送时间
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatterNow = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCurrentTime = currentTime.format(formatterNow);
        return formattedCurrentTime;
    }
}
