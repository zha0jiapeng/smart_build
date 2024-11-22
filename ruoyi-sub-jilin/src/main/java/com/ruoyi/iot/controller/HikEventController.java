package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.iot.bean.EventApi;
import com.ruoyi.iot.domain.SysEvents;
import com.ruoyi.iot.domain.hik.Event;
import com.ruoyi.iot.scheduling.DoorEvent;
import com.ruoyi.iot.service.ISysEventsService;
import com.ruoyi.iot.utils.HdyHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备Controller
 *
 * @author mashir0
 * @date 2024-06-23
 */
@RestController
@RequestMapping("/ai/events")
public class HikEventController extends BaseController {
    @Resource
    HdyHttpUtils hdyHttpUtils;

    @Autowired
    private ISysEventsService sysEventsService;


    @PostMapping("/push")
    public void push() {
        //未带安全帽
        String url = "http://10.1.3.204:8097/ai/events/receiveDataUnhelmeted";
        int type = 422000000;
        tet(url, type);

        //倒地
        url = "http://10.1.3.204:8097/ai/events/receiveDataFallen";
        type = 422400001;
        tet(url, type);

        //吸烟
        url = "http://10.1.3.204:8097/ai/events/receiveDataSmoking";
        type = 422400000;
        tet(url, type);

        //未穿反光衣
        url = "http://10.1.3.204:8097/ai/events/receiveDataNoReflectiveApparel";
        type = 422000001;
        tet(url, type);

    }

    public void tet(String EventDest, int type) {
        Event event = new Event();
        event.setEventDest(EventDest);
        int eventTypes[] = {type};
        event.setEventTypes(eventTypes);
        EventApi eventApi = new EventApi();
        String url = eventApi.eventSubscriptionByEventTypes(event);
        System.out.println(url);
    }


    /**
     * 未带安全帽
     * @param data
     */
    @RequestMapping(value = "/receiveDataUnhelmeted", method = RequestMethod.POST, consumes = "application/json")
    public void receiveDataUnhelmeted(@RequestBody String data) {

        System.out.println("AI摄像头报警未带安全帽内容：" + data);

    }

    /**
     * 倒地
     * @param data
     */
    @RequestMapping(value = "/receiveDataFallen", method = RequestMethod.POST, consumes = "application/json")
    public void receiveDataFallen(@RequestBody String data) {

        System.out.println("AI摄像头报警倒地内容：" + data);

    }

    /**
     * 吸烟
     * @param data
     */
    @RequestMapping(value = "/receiveDataSmoking", method = RequestMethod.POST, consumes = "application/json")
    public void receiveDataSmoking(@RequestBody String data) {

        System.out.println("AI摄像头报警吸烟内容：" + data);

    }

    /**
     * 未穿反光衣
     * @param data
     */
    @RequestMapping(value = "/receiveDataNoReflectiveApparel", method = RequestMethod.POST, consumes = "application/json")
    public void receiveDataNoReflectiveApparel(@RequestBody String data) {
        System.out.println("AI摄像头报警未穿反光衣：" + data);

    }


    public static Map<String, Object> jsonObjectToMap(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JSONObject) {
                map.put(key, jsonObjectToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                map.put(key, value); // 简单处理数组类型，如有需要可进一步处理
            } else {
                map.put(key, value);
            }
        }
        return map;
    }
}
