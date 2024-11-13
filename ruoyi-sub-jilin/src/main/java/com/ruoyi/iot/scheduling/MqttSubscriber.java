package com.ruoyi.iot.scheduling;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.iot.domain.ElectricityMonitoring;
import com.ruoyi.iot.service.IElectricityMonitoringService;
import com.ruoyi.iot.utils.HdyHttpUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author y
 */
@Service
@Component
public class MqttSubscriber {

    @Autowired
    private IElectricityMonitoringService electricityMonitoringService;

    @Resource
    HdyHttpUtils hdyHttpUtils;

    @PostConstruct
    public void subscribe() {
        // MQTT 代理地址
        String broker = "tcp://10.1.3.234:1883";
        // 客户端 ID
        String clientId = "JavaClient";
        // 订阅的主题
        String topic = "24090502540001";
        // 用户名
        String username = "root";
        // 密码
        String password = "sj4xMw65BE9y";

        try {
            // 创建 MQTT 客户端
            MqttClient client = new MqttClient(broker, clientId);

            // 创建连接选项
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(30);
            // 以秒为单位的心跳间隔
            options.setKeepAliveInterval(60);
            // 设置用户名
            options.setUserName(username);
            // 设置密码
            options.setPassword(password.toCharArray());

            // 连接到 MQTT 代理
            client.connect(options);
            // 订阅主题
            client.subscribe(topic, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // 接收到的消息处理
                    String json = new String(message.getPayload());
                    parseMessage(json);
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static int callCount = 0;
    private static Map<Integer, String> idValueMap = new HashMap<>();
    private static String rawData = "";

    private void parseMessage(String json) {
        boolean isConfirm = false;

        try {
            System.out.println("初始数据：" + json);

            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(json);

            if (jsonObject.containsKey("data")) {
                com.alibaba.fastjson.JSONArray dataArray = jsonObject.getJSONArray("data");

                for (int i = 0; i < dataArray.size(); i++) {
                    com.alibaba.fastjson.JSONObject obj = dataArray.getJSONObject(i);
                    String timestamp = obj.getString("tp");
                    idValueMap.put(-1, timestamp);

                    JSONArray pointsArray = obj.getJSONArray("point");
                    for (int j = 0; j < pointsArray.size(); j++) {
                        JSONObject pointObj = pointsArray.getJSONObject(j);
                        int id = pointObj.getIntValue("id");
                        String value = pointObj.getString("val");

                        if (id == 36) {
                            isConfirm = true;
                        } else if (id == 1 && callCount != 0) {
                            resetData();
                        }

                        idValueMap.put(id, value);
                    }
                }
            }

            rawData += json;
            callCount++;
            System.out.println("当前状态：callCount" + callCount + "isConfirm：" + isConfirm);
            if (callCount == 2 && isConfirm) {
                System.out.println("开始执行用电监测数据解析");
                ElectricityMonitoring electricityMonitoring = new ElectricityMonitoring();
                electricityMonitoring.setRawData(rawData);
                pushIOT(idValueMap, electricityMonitoring);
                resetData();
            }

        } catch (Exception e) {
            System.out.println("Failed to parse message: " + e.getMessage());
        }
    }

    private void resetData() {
        idValueMap.clear();
        rawData = "";
        callCount = 0;
    }

    public void pushIOT(Map<Integer, String> idValueMap, ElectricityMonitoring electricityMonitoring) {
        Map<String, Object> valueMap = new HashMap<>();
        //门户ID  String
        valueMap.put("portal_id", "1751847977770553345");
        //标段ID  String
        valueMap.put("sub_project_id", "1801194524869922817");
        //设备编号  String 0
        valueMap.put("device_code", idValueMap.get(0));
        //设备工作状态  String
        valueMap.put("work_status", "正常");
        //电箱类型  String
        valueMap.put("electric_box_type", "二级电箱");
        //父级电箱  String
        valueMap.put("electric_box_parent", "");
        //正向无功总电能  Decimal 30
        valueMap.put("total_p_r_e", idValueMap.get(30));
        electricityMonitoring.setTotalPRE(idValueMap.get(30));
        //反向无功总电能  Decimal 31
        valueMap.put("total_p_r_r_e", idValueMap.get(31));
        electricityMonitoring.setTotalPRRE(idValueMap.get(31));
        //正向有功总电量  Decimal 28
        valueMap.put("total_p_a_e", idValueMap.get(28));
        electricityMonitoring.setTotalPAE(idValueMap.get(28));
        //反向有功总电量  Decimal 29
        valueMap.put("total_p_r_a_e", idValueMap.get(29));
        electricityMonitoring.setTotalPRAE(idValueMap.get(29));
        //ABC三项电压  Decimal 1+2+3
        String a_b_c_voltage = calculateAndFormatSum(idValueMap, 1, 2, 3);
        valueMap.put("a_b_c_voltage", a_b_c_voltage);
        electricityMonitoring.setABCVoltage(a_b_c_voltage);
        //ABC三项电流  Decimal (7+8+9)/3
        String a_b_c_currentString = calculateAndFormatSum(idValueMap, 1, 2, 3);
        Double a_b_c_currentDouble = Double.parseDouble(a_b_c_currentString) / 3;
        Double result = a_b_c_currentDouble / 3;
        // 格式化结果保留一位小数
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        result = Double.valueOf(decimalFormat.format(result));
        valueMap.put("a_b_c_current", String.valueOf(result));
        electricityMonitoring.setABCCurrent(String.valueOf(result));
        //A相电流  Decimal 7
        valueMap.put("a_electricity", idValueMap.get(7));
        electricityMonitoring.setAElectricity(idValueMap.get(7));
        //B相电流  Decimal 8
        valueMap.put("b_blectricity", idValueMap.get(8));
        electricityMonitoring.setBBlectricity(idValueMap.get(8));
        //C相电流  Decimal 9
        valueMap.put("c_electricity", idValueMap.get(9));
        electricityMonitoring.setCElectricity(idValueMap.get(9));
        //A相电压  Decimal 1
        valueMap.put("a_voltage", idValueMap.get(1));
        electricityMonitoring.setAVoltage(idValueMap.get(1));
        //B相电压  Decimal 2
        valueMap.put("b_voltage", idValueMap.get(2));
        electricityMonitoring.setBVoltage(idValueMap.get(2));
        //C相电压  Decimal 3
        valueMap.put("c_voltage", idValueMap.get(3));
        electricityMonitoring.setCVoltage(idValueMap.get(3));
        //用电负载功率  Decimal
        valueMap.put("box_power", calculateLoadPower(idValueMap));
        //线缆A温度  Decimal 36
        valueMap.put("line_a_temperature", idValueMap.get(36));
        electricityMonitoring.setLineATemperature(idValueMap.get(36));
        //线缆B温度  Decimal 37
        valueMap.put("line_b_temperature", idValueMap.get(37));
        electricityMonitoring.setLineBTemperature(idValueMap.get(37));
        //线缆C温度  Decimal 38
        valueMap.put("line_c_temperature", idValueMap.get(38));
        electricityMonitoring.setLineCTemperature(idValueMap.get(38));
        //线缆N温度  Decimal 39
        valueMap.put("line_n_temperature", idValueMap.get(39));
        electricityMonitoring.setLineNTemperature(idValueMap.get(39));

        //总瞬时功率  Decimal
        valueMap.put("totalI_a_p", calculateTotalInstantaneousPower(idValueMap));
        //跳闸  String
        valueMap.put("switch_off", "");
        //合闸  String
        valueMap.put("switch_on", "");
        //推送时间  Date tp（时间戳）
        String timestampStr = idValueMap.get(-1);
        try {
            // 将时间戳字符串解析为长整型
            long timestamp = Long.parseLong(timestampStr);
            // 创建 Date 对象
            Date date = new Date(timestamp);
            // 定义日期时间格式化器
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 格式化 Date 为字符串
            timestampStr = formatter.format(date);
        } catch (NumberFormatException e) {
            System.out.println("Invalid timestamp format: " + e.getMessage());
        }
        valueMap.put("push_time", timestampStr);


        //其他  String
        valueMap.put("other", "");
        //总用电量  String
        String totalElectricityUsageKWh = calculateTotalElectricityUsage(valueMap.get("box_power").toString());
        electricityMonitoring.setTotalWattage(totalElectricityUsageKWh);
        QueryWrapper<ElectricityMonitoring> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("total_wattage");
        // 执行查询
        List<ElectricityMonitoring> monitoringList = electricityMonitoringService.list(queryWrapper);
        if (monitoringList.size() != 0) {
            BigDecimal totalWattageSum = BigDecimal.ZERO;

            for (ElectricityMonitoring monitoring : monitoringList) {
                String wattageStr = monitoring.getTotalWattage();
                if (wattageStr != null && !wattageStr.isEmpty()) {
                    BigDecimal wattageValue = new BigDecimal(wattageStr);
                    totalWattageSum = totalWattageSum.add(wattageValue);
                }
            }
            valueMap.put("total_wattage", totalWattageSum.toString());
        } else {
            valueMap.put("total_wattage", "0");
        }


        //预警类型（设备传输：漏电...  String
        valueMap.put("alarmCode", "");
        //数据类型(0、正常 1、报警)  String
        valueMap.put("data_type", "0");


        List<Map<String, Object>> values = new ArrayList<>();
        values.add(valueMap);
        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put("values", values);
        //开始插入数据库
        electricityMonitoringService.insertElectricityMonitoring(electricityMonitoring);
        hdyHttpUtils.pushIOT(param, "2e2529ef-f03a-4159-8ebb-a050e0e0fc89");
    }

    public String calculateTotalElectricityUsage(String loadPower) {
        // 将负载功率转换为 BigDecimal
        BigDecimal loadPowerValue = new BigDecimal(loadPower);

        // 将五分钟转换为小时 = 5/60 小时
        BigDecimal timeInHours = BigDecimal.valueOf(5).divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP);

        // 计算总用电量（单位为 kWh）
        BigDecimal totalElectricityUsageKWh = loadPowerValue.multiply(timeInHours).divide(BigDecimal.valueOf(1000), 10, RoundingMode.HALF_UP);

        return totalElectricityUsageKWh.toString(); // 返回结果为字符串
    }


    public String calculateTotalInstantaneousPower(Map<Integer, String> idValueMap) {
        BigDecimal aVoltage = new BigDecimal(idValueMap.get(1));
        BigDecimal bVoltage = new BigDecimal(idValueMap.get(2));
        BigDecimal cVoltage = new BigDecimal(idValueMap.get(3));
        BigDecimal aCurrent = new BigDecimal(idValueMap.get(7));
        BigDecimal bCurrent = new BigDecimal(idValueMap.get(8));
        BigDecimal cCurrent = new BigDecimal(idValueMap.get(9));

        // 计算总瞬时功率
        BigDecimal totalInstantaneousPower = aVoltage.multiply(aCurrent)
                .add(bVoltage.multiply(bCurrent))
                .add(cVoltage.multiply(cCurrent));

        return totalInstantaneousPower.toString(); // 返回结果为字符串
    }


    public String calculateLoadPower(Map<Integer, String> idValueMap) {
        BigDecimal aVoltage = new BigDecimal(idValueMap.get(1));
        BigDecimal bVoltage = new BigDecimal(idValueMap.get(2));
        BigDecimal cVoltage = new BigDecimal(idValueMap.get(3));
        BigDecimal aCurrent = new BigDecimal(idValueMap.get(7));
        //功率因数
        BigDecimal powerFactor = calculatePowerFactor(idValueMap);

        // 计算平均电压
        BigDecimal avgVoltage = (aVoltage.add(bVoltage).add(cVoltage)).divide(BigDecimal.valueOf(3));

        // 计算用电负载功率
        BigDecimal loadPower = BigDecimal.valueOf(Math.sqrt(3)) // √3
                .multiply(avgVoltage) // 平均电压
                .multiply(aCurrent) // A相电流
                .multiply(powerFactor); // 假设功率因数

        return loadPower.toString(); // 返回结果为字符串
    }


    public BigDecimal calculatePowerFactor(Map<Integer, String> idValueMap) {
        // 从 idValueMap 中获取正向有功功率（单位瓦特）
        BigDecimal totalPAE = new BigDecimal(idValueMap.get(28).toString()); // 正向有功总电量

        // 从 idValueMap 中获取 A、B、C 三项电压和电流
        BigDecimal aVoltage = new BigDecimal(idValueMap.get(1).toString()); // A相电压
        BigDecimal bVoltage = new BigDecimal(idValueMap.get(2).toString()); // B相电压
        BigDecimal cVoltage = new BigDecimal(idValueMap.get(3).toString()); // C相电压

        BigDecimal aCurrent = new BigDecimal(idValueMap.get(7).toString()); // A相电流
        BigDecimal bCurrent = new BigDecimal(idValueMap.get(8).toString()); // B相电流
        BigDecimal cCurrent = new BigDecimal(idValueMap.get(9).toString()); // C相电流

        // 计算平均电压
        BigDecimal avgVoltage = (aVoltage.add(bVoltage).add(cVoltage)).divide(BigDecimal.valueOf(3), 10, RoundingMode.HALF_UP);

        // 计算总电流
        BigDecimal totalCurrent = aCurrent.add(bCurrent).add(cCurrent);

        // 计算视在功率 S = V * I
        BigDecimal apparentPower = avgVoltage.multiply(totalCurrent); // 视在功率

        // 计算功率因数
        if (apparentPower.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal powerFactor = totalPAE.divide(apparentPower, 4, RoundingMode.HALF_UP); // 保留四位小数
            return powerFactor;
        }
        return BigDecimal.valueOf(0.0000); // 假设功率因数

    }


    private static String calculateAndFormatSum(Map<Integer, String> map, Integer... keys) {
        double sum = 0.0;

        for (Integer key : keys) {
            String value = map.get(key);
            if (value != null) {
                try {
                    sum += Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing value for key " + key + ": " + value);
                }
            }
        }
        // 保留两位小数
        return String.format("%.2f", sum);
    }
}