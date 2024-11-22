package com.ruoyi.iot.controller;


import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.AdmissionEducation;
import com.ruoyi.iot.scheduling.DeviceIpChecker;
import com.ruoyi.iot.utils.HdyHttpUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
        boolean value = new DeviceIpChecker().ping("10.1.3.150");
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
     * 实验室水泥抗压抗折试验上传
     */
    @GetMapping("/getCementStrengthTestingInterface")
    public void getCementStrengthTestingInterface() {
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

    public String getNowTimeExtractor() {
        //推送时间
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatterNow = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCurrentTime = currentTime.format(formatterNow);
        return formattedCurrentTime;
    }
}
