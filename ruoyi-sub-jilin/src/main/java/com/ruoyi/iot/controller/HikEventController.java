package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.iot.bean.EventApi;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.domain.Order;
import com.ruoyi.iot.domain.SysEvents;
import com.ruoyi.iot.domain.hik.Event;
import com.ruoyi.iot.scheduling.DoorEvent;
import com.ruoyi.iot.service.IDeviceService;
import com.ruoyi.iot.service.ISysEventsService;
import com.ruoyi.iot.service.RuleService;
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

    @Resource
    IDeviceService deviceService;


    @Autowired
    private RuleService ruleService;


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
     *
     * @param data
     */
    @RequestMapping(value = "/receiveDataUnhelmeted", method = RequestMethod.POST, consumes = "application/json")
    public void receiveDataUnhelmeted(@RequestBody String data) {
        System.out.println("AI摄像头报警未带安全帽内容：" + data);
        jsonHandle(data, "未带安全帽", 1L);
    }

    /**
     * 倒地
     *
     * @param data
     */
    @RequestMapping(value = "/receiveDataFallen", method = RequestMethod.POST, consumes = "application/json")
    public void receiveDataFallen(@RequestBody String data) {
        System.out.println("AI摄像头报警倒地内容：" + data);
        jsonHandle(data, "倒地", 2L);

    }

    /**
     * 吸烟
     *
     * @param data
     */
    @RequestMapping(value = "/receiveDataSmoking", method = RequestMethod.POST, consumes = "application/json")
    public void receiveDataSmoking(@RequestBody String data) {
        System.out.println("AI摄像头报警吸烟内容：" + data);
        jsonHandle(data, "吸烟", 3L);
    }

    /**
     * 未穿反光衣
     *
     * @param data
     */
    @RequestMapping(value = "/receiveDataNoReflectiveApparel", method = RequestMethod.POST, consumes = "application/json")
    public void receiveDataNoReflectiveApparel(@RequestBody String data) {
        System.out.println("AI摄像头报警未穿反光衣：" + data);
        jsonHandle(data, "未穿反光衣", 4L);
    }

    // 声明静态变量
    private static Map<Integer, Long> map;

    // 静态初始化块
    static {
        map = new HashMap<>();
        // 15支洞雨量监测旁球机
        map.put(34, 65L);
        //14支洞洞口
        map.put(33, 76L);
        //14支洞进厂路
        map.put(35, 115L);
    }


    public void jsonHandle(String rawData, String alarmType, long alarmTypeId) {

        // 解析 JSON 字符串为 JSON 对象
        JSONObject jsonObject = JSON.parseObject(rawData);
        String prettyData = JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue);
        JSONObject prettyDataJSON = JSON.parseObject(prettyData);
        JSONObject params = JSON.parseObject(prettyDataJSON.get("params").toString());
        //数据报告时间
        String time = DateUtil.parse(DoorEvent.getDateStrFromISO8601Timestamp(params.get("sendTime").toString())).toString();
        JSONArray events = JSON.parseArray(params.get("events").toString());
        for (Object object : events) {
            SysEvents sysEvents = new SysEvents();
            JSONObject event = (JSONObject) object;

            // 获取 "data" 对象
            JSONObject data = event.getJSONObject("data");

            // 获取 "AIOPResultData" 对象
            JSONObject aiopResultData = data.getJSONObject("AIOPResultData");
            // 获取 "imageUrl"
            String imageUrl = aiopResultData.getString("imageUrl");
            // 获取 "channelID"
            int channelID = data.getIntValue("channelID");

            //channelName
            String channelName = data.getString("channelName");

            //摄像头名称
            sysEvents.setCameraName(channelName);

            //摄像头编号
            sysEvents.setCameraNum(String.valueOf(channelID));


            Device one = deviceService.getOne(new LambdaQueryWrapper<Device>().eq(Device::getId, map.get(channelID)), false);

            //摄像头ip地址
            sysEvents.setCameraIp(one.getDeviceIp());

            sysEvents.setCameraState(1L);
            sysEvents.setMonitorTime(time);
            sysEvents.setAlertType(alarmType);
            sysEvents.setBidCode("1751847977770553345");
            sysEvents.setWorkAreaCode("1801194524869922817");
            sysEvents.setImageUrl(imageUrl);
            sysEvents.setRawData(rawData);

            sysEventsService.insertSysEvents(sysEvents);

            //添加报警信息
            Order order = new Order();
            //由于当前信息跟设备表没有对应，只能手动去数据库中查找（sys_device）
            order.setDeviceId(one.getId());
            //由于当前信息跟设备表没有对应，只能手动去数据库中查找（sys_device）
            order.setAlarmPoint(one.getId());
            //由于当前信息跟设备表没有对应，只能手动去数据库中查找（alarm_type）
            order.setAlarmTypeId(alarmTypeId);
            //由于当前信息跟设备表没有对应，只能手动去数据库中查找（alarm_type）
            order.setAlarmType(alarmType);
            // 获取当前时间
            LocalDateTime now = LocalDateTime.now();
            // 定义时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 格式化当前时间
            String formattedDateTime = now.format(formatter);
            order.setAlarmTime(formattedDateTime);
            order.setAlarmCapture(imageUrl);
            order.setAlarmContent("报警设备名称：" + channelName + "；报警内容：" + order.getAlarmType() + "；");
            order.setRemark("");
            ruleService.executeSignRule(order);
        }

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
