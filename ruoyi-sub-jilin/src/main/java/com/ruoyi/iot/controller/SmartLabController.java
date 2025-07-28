package com.ruoyi.iot.controller;


import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.AdmissionEducation;
import com.ruoyi.iot.scheduling.DeviceIpChecker;
import com.ruoyi.iot.scheduling.access.FTPServer;
import com.ruoyi.iot.scheduling.access.FTPServerConfig;
import com.ruoyi.iot.utils.HdyHttpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智能实验室
 */
@RestController
@RequestMapping("/SmartLab")
public class SmartLabController {

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
        map.put("10.1.3.152", "2401059");
        map.put("10.1.3.153", "2401053");
        map.put("10.1.3.154", "2401052");
        for (Map.Entry<String, String> entry : map.entrySet()) {
//            String ip = entry.getKey();
            String deviceCode = entry.getValue();
//            FTPServer ftpServer = new FTPServer();
//            String localUrl = "/";
//            FTPServerConfig ftpServerConfig = new FTPServerConfig(ip, 21, "yuancheng", "123456", localUrl, 1);
//            String localFilePath = "/home/mashir0/mdb/sctemp.mdb";
            String localFilePath = "/Users/y/Desktop/项目/吉林/更新/sctemp.mdb";
//            ftpServer.processFTPServer(ftpServerConfig, localFilePath);
            //然后读取
            String[] columnsToPrint = {"SBBH", "TestNo", "CurOrder", "Area", "TestBTime", "Operator", "MaxLoad", "KYQD", "断后标距", "FinalRate", "Agt", "Lagt", "Gauge", "TestPoint"}; // 你想打印的列
            List<Map<String, String>> mapList = getAccess(localFilePath, columnsToPrint);
            //然后解析
            for (Map<String, String> value : mapList) {
                String dataTime = getNowTimeExtractor();
                Map<String, Object> valueMap = new HashMap<>();
                valueMap.put("portal_id", "1751847977770553345");
                valueMap.put("date_time", dataTime);
                valueMap.put("device_code", deviceCode);
                valueMap.put("num_code", value.get("TestNo"));
                valueMap.put("test_piece_code", value.get("CurOrder"));
                valueMap.put("test_piece_seq", value.get("CurOrder"));
                valueMap.put("nominal_area", value.get("Area"));
                valueMap.put("test_day", value.get("TestBTime"));
                valueMap.put("tester", value.get("Operator"));
                valueMap.put("checker", "");
                valueMap.put("yield_force", value.get("FeL"));
                valueMap.put("yield_strength", value.get("PeL"));
                valueMap.put("max_force", value.get("MaxLoad"));
                valueMap.put("tensile_strength", value.get("KYQD"));
                valueMap.put("pe_at_ogl", value.get("Gauge"));
                valueMap.put("pe_at_fracture", value.get("断后标距"));
                valueMap.put("percent_elongation", value.get("FinalRate"));
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
                System.out.println(param);
//                hdyHttpUtils.pushIOT(param, "16a59ec9-08af-45d6-8af3-6631d868643a");
            }
        }

    }


    /**
     * 电液伺服万能材料试验机
     */
    @GetMapping("/getElectroHydraulicServoUniversalTesterTest")
    public void getElectroHydraulicServoUniversalTesterTest() {
        Map<String, String> map = new HashMap<>();
        map.put("10.1.3.152", "2401059");
//        map.put("10.1.3.153", "2401053");
//        map.put("10.1.3.154", "2401052");
        for (Map.Entry<String, String> entry : map.entrySet()) {
//            String ip = entry.getKey();
            String deviceCode = entry.getValue();
//            FTPServer ftpServer = new FTPServer();
//            String localUrl = "/";
//            FTPServerConfig ftpServerConfig = new FTPServerConfig(ip, 21, "yuancheng", "123456", localUrl, 1);
            String localFilePath = "/home/mashir0/mdb/sctemp.mdb";
//            ftpServer.processFTPServer(ftpServerConfig, localFilePath);
            //然后读取
            String[] columnsToPrint = {"SBBH", "TestNo", "CurOrder", "Area", "PeL","TestBTime","FeL", "Operator", "MaxLoad", "KYQD", "断后标距", "FtLoad", "Agt", "FpLoad", "Gauge", "TestPoint"}; // 你想打印的列
            List<Map<String, String>> mapList = getAccess(localFilePath, columnsToPrint);
            //然后解析
            for (Map<String, String> value : mapList) {
                String dataTime = getNowTimeExtractor();
                Map<String, Object> valueMap = new HashMap<>();
                valueMap.put("portal_id", "1751847977770553345");
                valueMap.put("data_time", dataTime);
                valueMap.put("device_code", deviceCode);
                valueMap.put("num_code", value.get("TestNo"));
                valueMap.put("test_piece_code", value.get("CurOrder"));
                valueMap.put("test_piece_seq", value.get("CurOrder"));
                valueMap.put("nominal_area", value.get("Area"));
                valueMap.put("test_day", value.get("TestBTime"));
                valueMap.put("tester", value.get("Operator"));
                valueMap.put("checker", "");
                valueMap.put("yield_force", value.get("FeL"));
                valueMap.put("yield_strength", value.get("PeL"));
                valueMap.put("max_force", value.get("MaxLoad"));
                valueMap.put("tensile_strength", value.get("KYQD"));
                valueMap.put("pe_at_ogl", value.get("Gauge"));
                valueMap.put("pe_at_fracture", "0");
                valueMap.put("percent_elongation", "0");
                valueMap.put("max_force_total_extension", value.get("Gauge"));
                valueMap.put("ext_at_max_force", value.get("FpLoad"));
                valueMap.put("total_extension", value.get("Agt"));
                valueMap.put("push_time", getNowTimeExtractor());
                valueMap.put("other", "");
                valueMap.put("curve", value.get("TestPoint"));

                // 获取当前日期和时间
                LocalDateTime now = LocalDateTime.now();

                // 定义日期时间格式
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                // 格式化当前日期时间
                String formattedDateTime = now.format(formatter);


                valueMap.put("create_date", formattedDateTime);
                List<Map<String, Object>> values = new ArrayList<>();
                values.add(valueMap);
                Map<String, List<Map<String, Object>>> param = new HashMap<>();
                param.put("values", values);
                System.out.println(param.toString());
                hdyHttpUtils.pushIOT(param, "16a59ec9-08af-45d6-8af3-6631d868643a");
            }
        }

    }

    /**
     * 实验室混凝土抗压强度实验上传
     */
    @GetMapping("/getConcreteCompressiveStrength")
    public void getConcreteCompressiveStrength() {
        //先下载文件
        FTPServer ftpServer = new FTPServer();
//        String localUrl = "/";
//        FTPServerConfig ftpServerConfig = new FTPServerConfig("10.1.3.151", 21, "yuancheng", "123456", localUrl, 1);
        String localFilePath = "/home/mashir0/mdb/151/Normal.mdb";
//        ftpServer.processFTPServer(ftpServerConfig, localFilePath);
        //然后读取
        String[] columnsToPrint = {"wbbh","lq","xzxs","date_time","yxqd","sjgg","zhz1","qdz1"};
        List<Map<String, String>> mapList = getAccessTestResult(localFilePath, columnsToPrint);
        //然后解析
        for (Map<String, String> value : mapList) {
            String dataTime = getNowTimeExtractor();
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("portal_id", "1751847977770553345");
            valueMap.put("data_time", dataTime);
            valueMap.put("device_code", "2405066");
            valueMap.put("num_code", value.get("wbbh"));
            valueMap.put("test_piece_code", value.get("wbbh"));
            valueMap.put("test_piece_seq", value.get("wbbh"));
            valueMap.put("compressive_age", value.get("lq"));
            valueMap.put("conversion_coefficient", value.get("xzxs"));
            valueMap.put("test_day", value.get("date_time"));
            valueMap.put("compressive_avg", value.get("yxqd"));
            valueMap.put("tester", "无");
            valueMap.put("checker", "无");
            valueMap.put("specimen_size", value.get("sjgg"));
            valueMap.put("failure_load", value.get("zhz1"));
            valueMap.put("compressive_strength", value.get("qdz1"));
            valueMap.put("push_time", getNowTimeExtractor());
            valueMap.put("other", " ");
            valueMap.put("curve", "[]");
            // 获取当前日期和时间
            LocalDateTime now = LocalDateTime.now();

            // 定义日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 格式化当前日期时间
            String formattedDateTime = now.format(formatter);
            valueMap.put("create_date", formattedDateTime);
            List<Map<String, Object>> values = new ArrayList<>();
            values.add(valueMap);
            Map<String, List<Map<String, Object>>> param = new HashMap<>();
            param.put("values", values);
            hdyHttpUtils.pushIOT(param, "1923f943-9a93-4640-93cd-b659a18fc258");
        }
    }


    /**
     * 实验室水泥抗压抗折试验上传
     */
    @GetMapping("/getCementStrengthTestingInterface")
    public void getCementStrengthTestingInterface() {
        //先下载文件

//        FTPServer ftpServer = new FTPServer();
//        String localUrl = "/";
//        FTPServerConfig ftpServerConfig = new FTPServerConfig("10.1.3.150", 21, "yuancheng", "123456", localUrl, 1);
        String localFilePath = "/home/mashir0/mdb/150/Normal.mdb";
//        ftpServer.processFTPServer(ftpServerConfig, localFilePath);
        //然后读取
        String[] columnsToPrint = {"wbbh","lq","xzxs","date_time","yxqd","sjgg","zhz1","qdz1","kj","yxqd"};
        List<Map<String, String>> mapList = getAccessTestResult(localFilePath, columnsToPrint);
        //然后解析
        for (Map<String, String> value : mapList) {
            String dataTime = getNowTimeExtractor();
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("portal_id", "1751847977770553345");
            valueMap.put("data_time", dataTime);
            valueMap.put("device_code", "2404129");
            valueMap.put("num_code", value.get("wbbh"));
            valueMap.put("test_piece_code", value.get("wbbh"));
            valueMap.put("test_piece_seq", value.get("wbbh"));
            valueMap.put("molding_date_time", value.get("date_time"));
            valueMap.put("compressive_age", value.get("lq"));
            valueMap.put("test_day", value.get("date_time"));
            valueMap.put("tester", "无");
            valueMap.put("checker", "无");
            valueMap.put("flexural_strength_at_side_len", value.get("sjgg"));
            valueMap.put("flexural_strength_at_span", value.get("kj"));
            valueMap.put("flexural_strength_at_load_failure",  value.get("zhz1"));
            valueMap.put("flexural_strength_at_single",  value.get("qdz1"));
            valueMap.put("flexural_strength_at_avg", value.get("yxqd"));
            valueMap.put("compressive_strength_at_area", " ");
            valueMap.put("compressive_strength_at_load_failure",  value.get("zhz1"));
            valueMap.put("compressive_strength_at_single", value.get("qdz1"));
            valueMap.put("compressive_strength_at_avg", value.get("yxqd"));
            valueMap.put("push_time", getNowTimeExtractor());
            valueMap.put("other", "");
            valueMap.put("curve", "[]");
            // 获取当前日期和时间
            LocalDateTime now = LocalDateTime.now();

            // 定义日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 格式化当前日期时间
            String formattedDateTime = now.format(formatter);
            valueMap.put("create_date", formattedDateTime);
            List<Map<String, Object>> values = new ArrayList<>();
            values.add(valueMap);
            Map<String, List<Map<String, Object>>> param = new HashMap<>();
            param.put("values", values);
            hdyHttpUtils.pushIOT(param, "54994e23-256b-42b3-bff3-9cda81bf9118");
        }
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("10.1.3.152", "2401059");
//        map.put("10.1.3.153", "2401053");
//        map.put("10.1.3.154", "2401052");
        for (Map.Entry<String, String> entry : map.entrySet()) {
//            String ip = entry.getKey();
            String deviceCode = entry.getValue();
//            FTPServer ftpServer = new FTPServer();
//            String localUrl = "/";
//            FTPServerConfig ftpServerConfig = new FTPServerConfig(ip, 21, "yuancheng", "123456", localUrl, 1);
            String localFilePath = "/Users/y/Desktop/项目/吉林/更新/sctemp.mdb";
//            ftpServer.processFTPServer(ftpServerConfig, localFilePath);
            //然后读取
            String[] columnsToPrint = {"SBBH", "TestNo", "CurOrder", "Area", "PeL","TestBTime","FeL", "Operator", "MaxLoad", "KYQD", "断后标距", "FtLoad", "Agt", "FpLoad", "Gauge", "TestPoint"}; // 你想打印的列
            List<Map<String, String>> mapList = getAccess(localFilePath, columnsToPrint);
            //然后解析
            for (Map<String, String> value : mapList) {
                String dataTime = getNowTimeExtractor();
                Map<String, Object> valueMap = new HashMap<>();
                valueMap.put("portal_id", "1751847977770553345");
                valueMap.put("date_time", dataTime);
                valueMap.put("device_code", deviceCode);
                valueMap.put("num_code", value.get("TestNo"));
                valueMap.put("test_piece_code", value.get("CurOrder"));
                valueMap.put("test_piece_seq", value.get("CurOrder"));
                valueMap.put("nominal_area", value.get("Area"));
                valueMap.put("test_day", value.get("TestBTime"));
                valueMap.put("tester", value.get("Operator"));
                valueMap.put("checker", "");
                valueMap.put("yield_force", value.get("FeL"));
                valueMap.put("yield_strength", value.get("PeL"));
                valueMap.put("max_force", value.get("MaxLoad"));
                valueMap.put("tensile_strength", value.get("KYQD"));
                valueMap.put("pe_at_ogl", value.get("Gauge"));
                valueMap.put("pe_at_fracture", value.get("断后标距"));
                valueMap.put("percent_elongation", value.get("FinalRate"));
                valueMap.put("max_force_total_extension", value.get("Gauge"));
                valueMap.put("ext_at_max_force", value.get("FpLoad"));
                valueMap.put("total_extension", value.get("Agt"));
                valueMap.put("push_time", getNowTimeExtractor());
                valueMap.put("other", "");
                valueMap.put("curve", value.get("TestPoint"));
                List<Map<String, Object>> values = new ArrayList<>();
                values.add(valueMap);
                Map<String, List<Map<String, Object>>> param = new HashMap<>();
                param.put("values", values);
                System.out.println(param.toString());
//                hdyHttpUtils.pushIOT(param, "16a59ec9-08af-45d6-8af3-6631d868643a");
            }
        }
    }


    /**
     * 读取本地Access数据库并打印内容
     */
    public static List<Map<String, String>> getAccess(String databasePath, String[] columnsToPrint) {
        String url = "jdbc:ucanaccess://" + databasePath + ";password=oke";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Map<String, String>> mapList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection(url);

            statement = connection.createStatement();

            String query = "SELECT * FROM Normal";
            resultSet = statement.executeQuery(query);
            mapList = printSelectedColumns(resultSet,columnsToPrint);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
     * 读取本地Access数据库并打印内容
     */
    public static List<Map<String, String>> getAccessTestResult(String databasePath, String[] columnsToPrint) {
        String url = "jdbc:ucanaccess://" + databasePath + ";password=oke";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Map<String, String>> mapList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection(url);

            statement = connection.createStatement();

            String query = "SELECT * FROM test_result";
            resultSet = statement.executeQuery(query);
            mapList = printSelectedColumns(resultSet,columnsToPrint);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
    public static List<Map<String, String>> printSelectedColumns(ResultSet resultSet, String[] columnsToPrint) {
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

    public static String getNowTimeExtractor() {
        //推送时间
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatterNow = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCurrentTime = currentTime.format(formatterNow);
        return formattedCurrentTime;
    }
}
