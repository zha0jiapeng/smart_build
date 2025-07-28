package com.ruoyi.iot.controller;


import com.ruoyi.iot.scheduling.DeviceIpChecker;
import com.ruoyi.iot.scheduling.access.FTPServer;
import com.ruoyi.iot.scheduling.access.FTPServerConfig;
import com.ruoyi.iot.utils.HdyHttpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
     * 配合比信息  >>>生产任务单>>>生产运输单>>>单盘耗料
     */

    public void zong() {
        //原生产配料单
        //生产任务单
        //生产运输单
        //单盘耗料

    }


    /**
     * 生产任务单
     */
    @GetMapping("/listProductionTasks")
    public void listProductionTasks() {
        List<Map<String, Object>> maps = getAccessProduce(databasePath);
        for (Map<String, Object> v : maps) {

            Map<String, Object> valueMap = new HashMap<>();

            valueMap.put("id", v.get("ID"));
            valueMap.put("create_by", "");
            valueMap.put("create_time", convertSerialNumberToDate(Double.valueOf(v.get("DatTim").toString())));
            valueMap.put("update_by", "");
            valueMap.put("update_time", "");
            valueMap.put("delete_flag", "0");
            //门户ID  String
            valueMap.put("portal_id", "1751847977770553345");
            //子工程ID  String
            valueMap.put("sub_project_id", "1801194524869922817");

            valueMap.put("task_order_no", v.get("ID"));
            //任务单号
            String taskOrderNo = v.get("ID").toString();

            valueMap.put("status", "");
            valueMap.put("work_section", v.get("ConsPos"));
            valueMap.put("design_amount", v.get("Mete"));
            valueMap.put("finish_amount", v.get("Mete"));
            valueMap.put("remain_amount", "");

            String strengthGrade = "无";
            // 检查强度等级
            if (v.containsKey("BetLev")) {
                String betLev = v.get("BetLev").toString();
                if (betLev.contains("20")) {
                    strengthGrade = "C20";
                } else if (betLev.contains("15")) {
                    strengthGrade = "C15";
                } else if (betLev.contains("25")) {
                    strengthGrade = "C25";
                } else if (betLev.contains("30")) {
                    strengthGrade = "C30";
                } else if (betLev.contains("35")) {
                    strengthGrade = "C35";
                } else if (betLev.contains("40")) {
                    strengthGrade = "C40";
                }
            }
            valueMap.put("intensity", strengthGrade);
            valueMap.put("impermeability", "无");
            valueMap.put("freeze_resistance_class", "无");
            String pouringMethod = v.get("Pour").toString();
            if (pouringMethod.equals("泵送")) {
                valueMap.put("pouring_method", "0");
            } else if (pouringMethod.equals("自卸")) {
                valueMap.put("pouring_method", "3");
            } else {
                valueMap.put("pouring_method", "1");
            }
            valueMap.put("special_item", "");
            valueMap.put("shaping", "");
            valueMap.put("report_id", v.get("Recipe"));//暂无数据
            valueMap.put("apply_time", "");
            valueMap.put("apply_name", "");
            valueMap.put("construction_address", v.get("ProjAdr"));
            valueMap.put("transportation_distance", v.get("Distance"));
            valueMap.put("product_type", v.get("Variety"));
            valueMap.put("slump", v.get("Lands"));
            valueMap.put("estimated_pouring_time", v.get("BegTim"));
            valueMap.put("technical_requirements", v.get("Request"));

            valueMap.put("house_id", "MIX_3BID_15");//拌合站唯一编码 接口文档3.2拌合站字典
            //task_start_time配单时间（发起次任务单时间即派单时间）
            valueMap.put("task_start_time", convertSerialNumberToDate(Double.valueOf(v.get("DatTim").toString())));
            //working_procedure
            valueMap.put("working_procedure", "2");
            //building_type
            valueMap.put("building_type", "PR_OTHER");
            //stake_mark_start_kms
            valueMap.put("stake_mark_start_kms", "无");

            //stake_mark_start_ms
            valueMap.put("stake_mark_start_ms", "无");

            //stake_mark_end_kms
            valueMap.put("stake_mark_end_kms", "无");

            //stake_mark_end_ms
            valueMap.put("stake_mark_end_ms", "无");

            valueMap.put("out_id", v.get("ID"));//此数据存入标段数据库的id(不明白)

            valueMap.put("area_name", "AR_3BID_OTHER");//接收本车次混凝土的工区 3.3工区字典

            System.out.println(valueMap.toString());
            send(valueMap,"25696542-559b-4f95-b598-875e2ce30b76");


            //生产运输单
            transportOrderProcessing(v);

            //整车耗材
            vehicleConsumables(taskOrderNo);


            //原生产配料单
            originalProductionIngredientList(v.get("Recipe").toString());
        }
    }

    //生产运输单
    public void transportOrderProcessing(Map<String, Object> v) {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("delete_flag", "0");
        //门户ID  String
        valueMap.put("portal_id", "1751847977770553345");
        //子工程ID  String
        valueMap.put("sub_project_id", "1801194524869922817");

        valueMap.put("house_id", "MIX_3BID_15");//拌合站唯一编码 接口文档3.2拌合站字典
        valueMap.put("out_id", v.get("ID"));//此数据存入标段数据库的id(不明白)
        valueMap.put("delivery_order_no", v.get("ID"));
        valueMap.put("task_order_no", v.get("ID"));
        valueMap.put("dispatch_time", convertSerialNumberToDate(Double.valueOf(v.get("LeftTim").toString())));
        valueMap.put("license_plate", "无");
        valueMap.put("driver_name", v.get("Driver"));
        valueMap.put("area_name", "AR_3BID_OTHER");
        send(valueMap, "2b9477bd-0384-4d8a-9b3e-1a3ad5f4eb93");

    }

    //单盘耗料
    public void vehicleConsumables(String taskOrderNo) {
        List<Map<String, Object>> maps = getAccessPiece(databasePath, taskOrderNo);
        for (Map<String, Object> v : maps) {
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("delete_flag", "0");
            //门户ID  String
            valueMap.put("portal_id", "1751847977770553345");
            //子工程ID  String
            valueMap.put("sub_project_id", "1801194524869922817");

            valueMap.put("house_id", "MIX_3BID_15");//拌合站唯一编码 接口文档3.2拌合站字典
            valueMap.put("out_id", v.get("ID"));//此数据存入标段数据库的id(不明白)
            valueMap.put("task_order_no", taskOrderNo);
            valueMap.put("disk_no", v.get("ID"));
            valueMap.put("delivery_order_no", taskOrderNo);
            valueMap.put("batch_volume", v.get("PieAmnt"));
            valueMap.put("departure_time", convertSerialNumberToDate(Double.valueOf(v.get("DatTim").toString())));
            valueMap.put("mix_duration", "0");
            valueMap.put("outlet_temperature", "-1");
            valueMap.put("materials_json_value", "[]");
            send(valueMap, "85a87435-0bbe-4b80-a343-d20b39dbd25c");
        }


    }

    //配合比
    void originalProductionIngredientList(String recipe) {
        List<Map<String, Object>> maps = getAccessRecipe(databasePath, recipe);
        for (Map<String, Object> v : maps) {
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("delete_flag", "0");
            //门户ID  String
            valueMap.put("portal_id", "1751847977770553345");
            //子工程ID  String
            valueMap.put("sub_project_id", "1801194524869922817");

            valueMap.put("house_id", "MIX_3BID_15");//拌合站唯一编码 接口文档3.2拌合站字典
            valueMap.put("out_id", v.get("ID"));//此数据存入标段数据库的id
            valueMap.put("mixture_ratio_no", v.get("ID"));
            valueMap.put("mixture_type", "0");
            valueMap.put("design_mixture_no", v.get("Code"));
            valueMap.put("build_mixture_no", "");
            valueMap.put("intensity", "无");
            valueMap.put("impermeability", "无");
            valueMap.put("freeze_resistance_class", "无");
            valueMap.put("pouring_method", "无");
            valueMap.put("enable", "0");
            valueMap.put("effective_time", convertSerialNumberToDate(Double.valueOf(v.get("DatTim").toString())));
            valueMap.put("specific_gravity", "0");
            Map<String, Object> map = calculateMaterialAmounts(recipe);
            valueMap.put("sand_rate", map.get("sand_rate"));
            valueMap.put("slump", v.get("Lands"));
            valueMap.put("water_cem_ratio", map.get("water_cem_ratio"));
            valueMap.put("require_date", "0");
            valueMap.put("mixture_report", "无");
            valueMap.put("materials_json_value", "[]");
            send(valueMap, "4b4feb42-4b82-49db-b36e-7e96436b2d07");
        }
    }


    public Map<String, Object> calculateMaterialAmounts(String recipe) {
        // 获取材料列表
        List<Map<String, Object>> maps = getAccessRecipeLst(databasePath, recipe);

        // 定义所需变量
        double sha = 0.0;   // 砂
        double gl2 = 0.0;   // 骨料2
        double gl3 = 0.0;   // 骨料3
        double gl4 = 0.0;   // 骨料4
        double sn1 = 0.0;   // 水泥1
        double sn2 = 0.0;   // 水泥2
        double sn3 = 0.0;   // 水泥3
        double fmh1 = 0.0;  // 粉煤灰1
        double fmh2 = 0.0;  // 粉煤灰2
        double s = 0.0;     // 水

        // 遍历材料列表
        for (Map<String, Object> v : maps) {
            String mater = v.get("Mater").toString();
            double amount = Double.parseDouble(v.get("Amount").toString());

            // 根据原材料编号赋值
            switch (mater) {
                case "1011": // 砂
                    sha = amount;
                    break;
                case "1021": // 骨料2
                    gl2 = amount;
                    break;
                case "1031": // 骨料3
                    gl3 = amount;
                    break;
                case "1041": // 骨料4
                    gl4 = amount;
                    break;
                case "3051": // 水泥1
                    sn1 = amount;
                    break;
                case "3052": // 水泥2
                    sn2 = amount;
                    break;
                case "3053": // 水泥3
                    sn3 = amount;
                    break;
                case "4061": // 粉煤灰1
                    fmh1 = amount;
                    break;
                case "4062": // 粉煤灰2
                    fmh2 = amount;
                    break;
                case "5071": // 水
                    s = amount;
                    break;
            }
        }

        // 计算砂率
        double totalAggregate = sha + gl2 + gl3 + gl4; // 骨料总量
        double sandRate = (sha / totalAggregate) * 100; // 砂率公式

        // 计算水胶比
        double totalCementitiousMaterials = sn1 + sn2 + sn3 + fmh1 + fmh2; // 胶凝材料总量
        double waterBinderRatio = s / totalCementitiousMaterials; // 水胶比公式

        // 保留两位小数
        BigDecimal sandRateFormatted = new BigDecimal(sandRate).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal waterBinderRatioFormatted = new BigDecimal(waterBinderRatio).setScale(2, BigDecimal.ROUND_HALF_UP);

        Map<String, Object> map = new HashMap<>();
        map.put("sand_rate", sandRateFormatted);
        map.put("water_cem_ratio", waterBinderRatioFormatted);
        return map;
    }

    /**
     * 生产运输单
     */
    @GetMapping("/transportOrderProcessing")
    public void transportOrderProcessing() {
        List<Map<String, Object>> maps = getAccessProduce(databasePath);
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
//            valueMap.put("area_name", v.get("Code"));

            //新增
            valueMap.put("out_id", v.get("ID"));//此数据存入标段数据库的id(不明白)
            valueMap.put("house_id", "MIX_3BID_15");//拌合站唯一编码 接口文档3.2拌合站字典
            valueMap.put("area_name", "AR_3BID_OTHER");//接收本车次混凝土的工区 3.3工区字典

            System.out.println(valueMap.toString());
            send(valueMap, "1d5c0973-f40e-496c-acd3-5f9ad6bf13a0");
        }
    }

    /**
     * 整车耗材
     */

    /**
     * 原生产配料单
     */


    public static void main(String[] args) {
        System.out.println(getAccessProduce("/Users/y/Desktop/BCS7.2.mdb").toString());

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
    public static List<Map<String, Object>> getAccessProduce(String databasePath) {
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
            String query = "SELECT * FROM (SELECT TOP 1 * FROM Produce ORDER BY DatTim DESC) ORDER BY id ASC";
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
     * 读取本地Access数据库
     */
    public static List<Map<String, Object>> getAccessPiece(String databasePath, String produce) {
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
//            String query = "SELECT * FROM (SELECT TOP 10 * FROM Produce ORDER BY DatTim DESC) ORDER BY id ASC";
            String query = "SELECT * FROM Piece WHERE Produce = '" + produce + "'";
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
     * 读取本地Access数据库
     */
    public static List<Map<String, Object>> getAccessRecipe(String databasePath, String recipe) {
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
//            String query = "SELECT * FROM (SELECT TOP 10 * FROM Produce ORDER BY DatTim DESC) ORDER BY id ASC";
            String query = "SELECT * FROM Recipe WHERE ID = '" + recipe + "'";
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
     * 读取本地Access数据库
     */
    public static List<Map<String, Object>> getAccessRecipeLst(String databasePath, String recipe) {
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
//            String query = "SELECT * FROM (SELECT TOP 10 * FROM Produce ORDER BY DatTim DESC) ORDER BY id ASC";
            String query = "SELECT * FROM RecipeLst WHERE Recipe = '" + recipe + "'";
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
     * 读取本地Access数据库
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
