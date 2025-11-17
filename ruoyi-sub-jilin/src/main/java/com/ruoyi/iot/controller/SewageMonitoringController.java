package com.ruoyi.iot.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.utils.HdyHttpUtils;
import com.ruoyi.iot.utils.Modbus4jReadUtil;
import com.ruoyi.iot.utils.ModbusTcpMaster;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 环境污水检测Controller
 *
 * @author liang
 * @date 2024-12-19
 */
@RestController
@RequestMapping("/SewageMonitoring")
public class SewageMonitoringController extends BaseController {

    @Resource
    HdyHttpUtils hdyHttpUtils;
    private static final int MODBUS_TCP_PORT = 502;
    private static final String DEVICE_IP_14 = "10.1.3.80";
    private static final String DEVICE_IP_15 = "10.1.23.32";


    /**
     * 推送接口
     *
     * @return
     */
    @GetMapping("/list")
    public AjaxResult list() throws Exception {
        if (isIpReachable(DEVICE_IP_14)) {
            ModbusMaster master = new ModbusTcpMaster().getSlave(DEVICE_IP_14, MODBUS_TCP_PORT);

            //14
            Number mA1 = Modbus4jReadUtil.readHoldingRegister(master, 1, 16, DataType.TWO_BYTE_INT_UNSIGNED, "mA1");
            Number mA2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 17, DataType.TWO_BYTE_INT_UNSIGNED, "mA2");
            String deviceCode = "sewage_14";
            pushIOT(mA1, mA2, deviceCode);
        }

        if (isIpReachable(DEVICE_IP_15)) {
            //15
            ModbusMaster master = new ModbusTcpMaster().getSlave(DEVICE_IP_15, MODBUS_TCP_PORT);
            Number mA1 = Modbus4jReadUtil.readHoldingRegister(master, 1, 16, DataType.TWO_BYTE_INT_UNSIGNED, "mA1");
            Number mA2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 17, DataType.TWO_BYTE_INT_UNSIGNED, "mA2");
            String deviceCode = "sewage_15";
            pushIOT(mA1, mA2, deviceCode);
        }
        return success();
    }

    public static boolean isIpReachable(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            // 尝试发送 ping
            return inetAddress.isReachable(2000); // 设置超时时间为 2000 毫秒
        } catch (UnknownHostException e) {
            System.out.println("IP 地址无法识别: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("发生错误: " + e.getMessage());
        }
        return false; // 默认返回 false
    }

    public void pushIOT(Number mA1, Number mA2, String deviceCode) throws Exception {
        // 计算电流
        double current1 = mA1.doubleValue() * 0.01;
        double current2 = mA2.doubleValue() * 0.01;
//        System.out.println("mA1:" + mA1.doubleValue());
//        System.out.println("mA2:" + mA2.doubleValue());


        Map<String, Object> valueMap = new HashMap<>();
        //门户ID  String
        valueMap.put("portal_id", "1751847977770553345");
        //子工程ID  String
        valueMap.put("sub_project_id", "1801194524869922817");
        //设备编号  String
        valueMap.put("device_code", deviceCode);
        //设备状态（是否在线）
        valueMap.put("status", "在线");
        String timestampStr = "";
        try {
            // 创建 Date 对象
            Date date = new Date();
            // 定义日期时间格式化器
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 格式化 Date 为字符串
            timestampStr = formatter.format(date);
        } catch (NumberFormatException e) {
            System.out.println("Invalid timestamp format: " + e.getMessage());
        }
        //推送时间
        valueMap.put("push_time", timestampStr);
        //获取数据时间
        valueMap.put("update_time", timestampStr);


        //浑浊度（单位：NTU）
        valueMap.put("turbidity", "");
        //氨氮浓度（单位：mg/L）
        valueMap.put("ammonia_concentration", "");
        //液位（单位：m）
        valueMap.put("liquid_level", "");
        if ("sewage_14".equals(deviceCode)) {
            //流量
            valueMap.put("waste_water_flow", current1);
            //ph值 是
            valueMap.put("ph", calculatePH(current2));

            //漂浮物 （单位：mg/L）是
            valueMap.put("flotage", calculateSS(current1));
        } else {
            //流量
            valueMap.put("waste_water_flow", current1);
            //ph值 是
            valueMap.put("ph", calculatePH(current1));
            //漂浮物 （单位：mg/L）是
            valueMap.put("flotage", calculateSS(current2));
        }
        valueMap.put("other", "");

        List<Map<String, Object>> values = new ArrayList<>();
        values.add(valueMap);
        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put("values", values);
        String number = deviceCode.replaceAll(".*_", "");
//        checkRequestStatus(number, String.valueOf(valueMap.get("ph")), String.valueOf(valueMap.get("flotage")));
        hdyHttpUtils.pushIOT(param, "0ad4e4ad-81b3-4838-babe-69bfd6d65f58");
    }


    /**
     * 发送GET请求并检查响应状态码是否为200
     *
     * @param filePath 文件路径参数
     * @param ph       PH参数
     * @param flotage  浮标参数
     * @return 如果请求成功且状态码为200返回true，否则返回false
     * @throws Exception 如果请求过程中发生异常
     */
    public void checkRequestStatus(String filePath, String ph, String flotage) throws Exception {
        System.out.println("开始请求接口");
        String result1 = HttpUtil.get("http://10.1.3.208:11478/eq2008/shangsheng?filePath=" + filePath + "&ph=" + ph + "&flotage=" + flotage);

    }

    @GetMapping("/checkStatus")
    public void checkStatus() throws Exception {
        if (isIpReachable(DEVICE_IP_14)) {
            ModbusMaster master = new ModbusTcpMaster().getSlave(DEVICE_IP_14, MODBUS_TCP_PORT);
            //14
            Number mA1 = Modbus4jReadUtil.readHoldingRegister(master, 1, 16, DataType.TWO_BYTE_INT_UNSIGNED, "mA1");
            Number mA2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 17, DataType.TWO_BYTE_INT_UNSIGNED, "mA2");
            String deviceCode = "sewage_14";
            // 计算电流
            double current1 = mA1.doubleValue() * 0.01;
            double current2 = mA2.doubleValue() * 0.01;
            Map<String, Object> valueMap = new HashMap<>();
            //流量
            valueMap.put("waste_water_flow", current1);
            //ph值 是
            valueMap.put("ph", calculatePH(current2));
            String number = deviceCode.replaceAll(".*_", "");
            //漂浮物 （单位：mg/L）是
            valueMap.put("flotage", calculateSS(current1));
            checkRequestStatus(number, String.valueOf(valueMap.get("ph")), String.valueOf(valueMap.get("flotage")));

        }

        if (isIpReachable(DEVICE_IP_15)) {
            //15
            ModbusMaster master = new ModbusTcpMaster().getSlave(DEVICE_IP_15, MODBUS_TCP_PORT);
            Number mA1 = Modbus4jReadUtil.readHoldingRegister(master, 1, 16, DataType.TWO_BYTE_INT_UNSIGNED, "mA1");
            Number mA2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 17, DataType.TWO_BYTE_INT_UNSIGNED, "mA2");
            String deviceCode = "sewage_15";
            // 计算电流
            double current1 = mA1.doubleValue() * 0.01;
            double current2 = mA2.doubleValue() * 0.01;
            Map<String, Object> valueMap = new HashMap<>();
            //流量
            valueMap.put("waste_water_flow", current1);
            //ph值 是
            valueMap.put("ph", calculatePH(current2));
            String number = deviceCode.replaceAll(".*_", "");
            //漂浮物 （单位：mg/L）是
            valueMap.put("flotage", calculateSS(current1));
            checkRequestStatus(number, String.valueOf(valueMap.get("ph")), String.valueOf(valueMap.get("flotage")));
        }
    }


    // 计算 pH 值的方法
    public static double calculatePH(double current) {
        // 电流范围为 4 mA 到 20 mA，对应 pH 范围为 0 到 14
        double minCurrent = 4.0;
        double maxCurrent = 20.0;
        double minPH = 0.0;
        double maxPH = 14.0;

        // 计算 pH
        return (current - minCurrent) / (maxCurrent - minCurrent) * (maxPH - minPH) + minPH;
    }

    // 计算 ss 值的方法
    public static double calculateSS(double current) {
        // 电流范围为 4 mA 到 20 mA，对应浊度范围为 0 到 100
        double minCurrent = 4.0;
        double maxCurrent = 20.0;
        double minSS = 0.0;
        double maxSS = 100.0;

        // 计算 ss
        return (current - minCurrent) / (maxCurrent - minCurrent) * (maxSS - minSS) + minSS;
    }


    public static void main(String[] args) {
        System.out.println(calculatePH(6.42));
        System.out.println(calculatePH(14.05));
        System.out.println(calculateSS(7.60));
        System.out.println(calculateSS(14.05));
    }


    public static double generateRandomDoubleInRange(double min, double max) {
        if (min >= max) {
            throw new IllegalArgumentException("max必须大于min");
        }
        return formatDecimal(ThreadLocalRandom.current().nextDouble(min, max + Double.MIN_VALUE));
    }

    private static double formatDecimal(double value) {
        return Double.parseDouble(new DecimalFormat("#.00").format(value));
    }
}
