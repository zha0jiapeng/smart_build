package com.ruoyi.iot.controller;

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
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
    private static final String DEVICE_IP_15 = "10.1.3.90";


    /**
     * 推送接口
     *
     * @return
     */
    @GetMapping("/list")
    public AjaxResult list() {

        ModbusMaster master = new ModbusTcpMaster().getSlave(DEVICE_IP_14, MODBUS_TCP_PORT);

        Number ADC1 = Modbus4jReadUtil.readHoldingRegister(master, 1, 0, DataType.TWO_BYTE_INT_UNSIGNED, "ADC1");
        Number ADC2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 1, DataType.TWO_BYTE_INT_UNSIGNED, "ADC2");
        //14
        Number mA1 = Modbus4jReadUtil.readHoldingRegister(master, 1, 16, DataType.TWO_BYTE_INT_UNSIGNED, "mA1");
        Number mA2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 17, DataType.TWO_BYTE_INT_UNSIGNED, "mA2");
        String deviceCode = "sewage_14";
        pushIOT(mA1, mA2, deviceCode);

        //15
//        master = new ModbusTcpMaster().getSlave(DEVICE_IP_15, MODBUS_TCP_PORT);
//        mA1 = Modbus4jReadUtil.readHoldingRegister(master, 1, 16, DataType.TWO_BYTE_INT_UNSIGNED, "mA1");
//        mA2 = Modbus4jReadUtil.readHoldingRegister(master, 1, 18, DataType.TWO_BYTE_INT_UNSIGNED, "mA2");
        deviceCode = "sewage_15";
        pushIOT(mA1, mA2, deviceCode);
        return success();
    }

    public void pushIOT(Number mA1, Number mA2, String deviceCode) {
        // 计算电流
        double current1 = mA1.doubleValue() * 0.01;
        double current2 = mA2.doubleValue() * 0.01;


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

        for (int i = 0; i < 2; i++) {
            double current = 0; // 电流值
            //流量（单位：m³/s）是
            if (i == 0) {
                current = current1;
            } else if (i == 1) {
                current = current2;
            }
            valueMap.put("waste_water_flow", current);

            double pH = -0.2 * current + 8.0; // 计算 pH 值
            double floatingMatter = 0.5 * current + 0; // 计算漂浮物浓度
            //ph值 是
            valueMap.put("ph", pH);

            //漂浮物 （单位：mg/L）是
            valueMap.put("flotage", floatingMatter);

            valueMap.put("other", "");
            List<Map<String, Object>> values = new ArrayList<>();
            values.add(valueMap);
            Map<String, List<Map<String, Object>>> param = new HashMap<>();
            param.put("values", values);
            hdyHttpUtils.pushIOT(param, "0ad4e4ad-81b3-4838-babe-69bfd6d65f58");
        }
    }


}
