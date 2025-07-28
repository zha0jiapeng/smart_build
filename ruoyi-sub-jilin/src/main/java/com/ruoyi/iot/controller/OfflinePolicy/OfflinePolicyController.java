package com.ruoyi.iot.controller.OfflinePolicy;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.iot.domain.IpBroadcast;
import com.ruoyi.iot.service.IIpBroadcastService;
import com.ruoyi.iot.utils.BroadcastAlarmUtil;
import com.ruoyi.iot.utils.HdyHttpUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * 是否在线
 *
 * @author hu_p
 * @date 2024/6/22
 */
@RestController
@Slf4j
@RequestMapping("offlinePolicy")
public class OfflinePolicyController extends BaseController {

    @GetMapping("/sendOfflinePolicy")
    public void sendOfflinePolicy() {
        //ip广播
        broadcastAlarmDeviceOfflinePolicy();
    }

    public void broadcastAlarmDeviceOfflinePolicy() {
        Map<String, String> map = new HashMap<>();
        //14#大门
        map.put("10.1.3.141", "0201B1A1");
        //14#洞口
        map.put("10.1.3.143", "93B1237D");
        //14#门卫-14#钢筋厂
        map.put("10.1.3.142", "053D315A");
        //15#大门
        map.put("10.1.3.140", "06FA10BB");
        //值班室-4G收扩机
        map.put("10.1.3.198", "7910BA03");
        //施工3标-14#-大门口车辆道闸【入场】
        map.put("10.1.3.210", "f106f4e2-49788a49");
        //施工3标-14#-大门口车辆道闸【出场】
        map.put("10.1.3.211", "4e32e371-4291fb9a");
        //施工3标-14#-洞口车辆道闸【入场】
        map.put("10.1.3.108", "77091ae04d404a58925f59631975cf34");
        //施工3标-14#-洞口车辆道闸【出场】
        map.put("10.1.3.107", "64b8b4e9988143bba67de8d96672be9f");
        // 施工3标-15#-大门口车辆道闸【入场】
        map.put("10.1.3.131", "DS-TCG205-E 20220610AIK11586998");
        // 施工3标-15#-大门口车辆道闸【出场】
        map.put("10.1.3.132", "DS-TCG205-E 20220610AIK11586950");
        // 施工3标-15#-支洞口车辆道闸【出场】
        map.put("10.1.3.91", "DS-TCG205-A 20210221AIF56682850");
        // 施工3标-15#-支洞口车辆道闸【入场】
        map.put("10.1.3.92", "DS-TCG205-A 20210301AIF60001195");

        //人员定位
        //14洞外
        map.put("10.1.3.50","3009f9b0bb24");
        //14洞内
        map.put("10.1.3.75","3009f9b07ee2");
        //15洞外
        map.put("10.1.3.76","3009f9b085a5");
        //15洞内
        map.put("10.1.3.79","3009f9b08665");


        for (Map.Entry<String, String> entry : map.entrySet()) {
            String ip = entry.getKey();
            String deviceId = entry.getValue();
            Map<String, List<Map<String, String>>> mapListMap = new HashMap<>();
            Map<String, String> messagesMap = new HashMap<>();
            messagesMap.put("portal_id", "1751847977770553345");
            messagesMap.put("device_id", deviceId);
            String now = DateUtil.now();
            messagesMap.put("push_time", now);
            if (ping(ip, 3000)) {
                //在线
                messagesMap.put("status", "1");
            } else {
                //离线
                messagesMap.put("status", "2");
            }
            List<Map<String, String>> mapList = new ArrayList<>();
            mapList.add(messagesMap);
            mapListMap.put("values", mapList);
            send(mapListMap);
        }
    }

    public void send(Map param) {
        HttpResponse execute = HttpRequest.put("http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/abe3df03-f8c5-4416-bac8-2c7524d9d9ed")
                .body(JSON.toJSONString(param, SerializerFeature.WriteMapNullValue), "application/json").execute();
        log.info("push hdy url:{} , response:{}", "http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/abe3df03-f8c5-4416-bac8-2c7524d9d9ed", execute.body());
    }

    /**
     * 检查ip地址是否能够成功链接
     */
    public boolean ping(String ip, int timeoutMs) {
        try {
            return InetAddress.getByName(ip).isReachable(timeoutMs);
        } catch (Exception e) {
            // 任何异常均视为不可达
            return false;
        }
    }
}

