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

    private static String databasePath = "/home/mashir0/mdb/BCS7.2.mdb";

    /**
     * 生产运输单
     */
    @GetMapping("/transportOrderProcessing")
    public void transportOrderProcessing() {
        List<Map<String, Object>> maps = getAccess(databasePath);
        for (Map<String, Object> v : maps) {

            Map<String, Object> valueMap = new HashMap<>();

            valueMap.put("id", v.get("ID"));
            valueMap.put("create_by", "");
            valueMap.put("create_date", convertSerialNumberToDate(Double.valueOf(v.get("DatTim").toString())));
            valueMap.put("update_by", "");
            valueMap.put("update_date", convertSerialNumberToDate(Double.valueOf(v.get("DatTim").toString())));
            valueMap.put("delete_flag", "");
            valueMap.put("delivery_order_no", v.get("Code"));
            //门户ID  String
            valueMap.put("portal_id", "1751847977770553345");
            //子工程ID  String
            valueMap.put("sub_project_id", "1801194524869922817");
            valueMap.put("task_order_no", v.get("Code"));
            valueMap.put("customer_name", v.get("Customer"));
            valueMap.put("project_name", v.get("ProjName"));
            valueMap.put("pouring_section", v.get("ConsPos"));
            valueMap.put("pouring_method", v.get("Pour"));
            valueMap.put("dispatch_time", convertSerialNumberToDate(Double.valueOf(v.get("LeftTim").toString())));
            if (v.get("Lands").toString() == null || v.get("Lands").toString().equals("")) {
                continue;
            }
            valueMap.put("slump", v.get("Lands"));
            valueMap.put("mixer_no", "无");
            valueMap.put("strength_class", v.get("BetLev"));
            valueMap.put("special_item", "无");

            Map<String, Object> vehicleMap = getAccessVehicleById(databasePath, v.get("Vehicle").toString());
            valueMap.put("license_plate", vehicleMap.get("Code"));
            valueMap.put("driver_name", v.get("Vehicle"));
            valueMap.put("dispatched_volume", v.get("ProdMete"));
            valueMap.put("cumulative_volume", v.get("TotMete"));
            valueMap.put("signed_volume", v.get("ProdMete"));
            valueMap.put("cumulative_trips", v.get("TotVehs"));
            valueMap.put("signoff_time", convertSerialNumberToDate(Double.valueOf(v.get("DatTim").toString())));
            valueMap.put("pump_truck_license_plate", "");
            valueMap.put("pump_truck_model", "");
            valueMap.put("creator", v.get("Operator"));
            valueMap.put("area_name", v.get("Code"));

            System.out.println(valueMap.toString());
            send(valueMap, "1d5c0973-f40e-496c-acd3-5f9ad6bf13a0");
        }
    }

    /**
     * 生产任务单
     */
    @GetMapping("/listProductionTasks")
    public void listProductionTasks() {
        List<Map<String, Object>> maps = getAccessTask(databasePath);
        for (Map<String, Object> v : maps) {

            Map<String, Object> valueMap = new HashMap<>();

            valueMap.put("id", v.get("ID"));
            valueMap.put("create_by", "");
            valueMap.put("create_time", convertSerialNumberToDate(Double.valueOf(v.get("DatTim").toString())));
            valueMap.put("update_by", "");
            valueMap.put("update_time", "");
            valueMap.put("delete_flag", "");
            //门户ID  String
            valueMap.put("portal_id", "1751847977770553345");
            //子工程ID  String
            valueMap.put("sub_project_id", "1801194524869922817");
            valueMap.put("task_order_no", v.get("Code"));
            valueMap.put("status", "");
            valueMap.put("work_section", v.get("ConsPos"));
            valueMap.put("design_amount", v.get("Mete"));
            valueMap.put("finish_amount",  v.get("Mete"));
            valueMap.put("remain_amount", "");
            valueMap.put("intensity", v.get("BetLev"));
            valueMap.put("impermeability", v.get("Filter"));
            valueMap.put("freeze_resistance_class", v.get("Freeze"));
            valueMap.put("pouring_method", v.get("Pour"));
            valueMap.put("special_item", "");
            valueMap.put("shaping", "");
            valueMap.put("report_id", "");
            valueMap.put("apply_time", "");
            valueMap.put("apply_name", "");
            valueMap.put("construction_address", v.get("ProjAdr"));
            valueMap.put("transportation_distance", v.get("Distance"));
            valueMap.put("product_type", v.get("Variety"));
            valueMap.put("slump", v.get("Lands"));
            valueMap.put("estimated_pouring_time", v.get("BegTim"));
            valueMap.put("technical_requirements", v.get("Request"));

            System.out.println(valueMap.toString());
//            send(valueMap,"3e8f335f-edbc-4270-ba56-b98cbac6f5ee");
        }
    }

    public static void main(String[] args) {
        System.out.println(getAccess("/Users/y/Desktop/BCS7.2.mdb").toString());

    }

    public void send(Map<String, Object> valueMap, String rid) {
        List<Map<String, Object>> values = new ArrayList<>();
        values.add(valueMap);
        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put("values", values);

        hdyHttpUtils.pushIOT(param, rid);
    }

    public static String convertSerialNumberToDate(double serialNumber) {
        // 基础日期为1899年12月30日
        LocalDateTime baseDate = LocalDateTime.of(1899, 12, 30, 0, 0, 0);

        // 将序列号分为完整天数和时间部分
        int days = (int) serialNumber;
        double fractionalDay = serialNumber - days;

        // 计算时间部分（将小数转换为秒数）
        long secondsInDay = (long) (fractionalDay * 24 * 60 * 60);

        // 将天数和时间相加
        LocalDateTime resultDate = baseDate.plusDays(days).plusSeconds(secondsInDay);

        // 定义格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化日期时间为字符串
        return resultDate.format(formatter);
    }

    /**
     * 读取本地Access数据库Produce表并打印最后50条内容
     */
    public static List<Map<String, Object>> getAccess(String databasePath) {
        String url = "jdbc:ucanaccess://" + databasePath + ";password=BCS7.2_SDBS";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> mapList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection(url);

            statement = connection.createStatement();

            // 获取最后50条数据
            String query = "SELECT * FROM (SELECT TOP 50 * FROM Produce ORDER BY DatTim DESC) ORDER BY id ASC";
            resultSet = statement.executeQuery(query);
            mapList = printAllColumns(resultSet);

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
     * 读取本地Access数据库Task表并打印最后50条内容
     */
    public static List<Map<String, Object>> getAccessTask(String databasePath) {
        String url = "jdbc:ucanaccess://" + databasePath + ";password=BCS7.2_SDBS";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> mapList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection(url);

            statement = connection.createStatement();

            // 获取最后50条数据
            String query = "SELECT * FROM (SELECT TOP 10 * FROM Task ORDER BY id DESC) ORDER BY id ASC";
            resultSet = statement.executeQuery(query);
            mapList = printAllColumns(resultSet);

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
     * 读取本地Access数据库Vehicle指定id
     */
    public static Map<String, Object> getAccessVehicleById(String databasePath, String id) {
        String url = "jdbc:ucanaccess://" + databasePath + ";password=BCS7.2_SDBS";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> mapList = new ArrayList<>();

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection(url);

            statement = connection.createStatement();

            String query = "SELECT * FROM Vehicle WHERE id = " + id;
            resultSet = statement.executeQuery(query);
            mapList = printAllColumns(resultSet);

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
        return mapList.get(0);
    }

    /**
     * 打印 ResultSet 内容
     */
    public static List<Map<String, Object>> printAllColumns(ResultSet resultSet) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        try {
            // 获取ResultSet的元数据，以便动态获取列名
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 遍历ResultSet的每一行
            while (resultSet.next()) {
                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    rowMap.put(columnName, value);
                }
                mapList.add(rowMap);
            }
//            System.out.println("Retrieved Data: " + mapList.toString());
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
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
