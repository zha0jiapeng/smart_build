package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.iot.utils.BroadcastAlarmUtil;
import com.ruoyi.iot.utils.HdyHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * IP广播报警
 * @author hu_p
 * @date 2024/6/22
 */
@RestController
@Slf4j
@RequestMapping("broadcastalarm")
public class BroadcastAlarmController {

    @Autowired
    BroadcastAlarmUtil broadcastAlarmUtil;

    @Resource
    HdyHttpUtils hdyHttpUtils;

    /**
     * 获取广播报警设备
     */
    @GetMapping("device")
    public JSONObject deviceList(){
        JSONObject deviceList = broadcastAlarmUtil.getDeviceList();
        return deviceList;
    }

    private static final String API_URL = "https://your-api-domain.com/v1/task_log";

    // 每10分钟查询一次定时任务的日志
    @Scheduled(cron = "0 */10 * * * ?") // 每10分钟执行一次
    public void queryScheduledTaskLogs() {
        log.info("============BroadcastAlarmController.queryScheduledTaskLogs================");
        // 获取当前时间和10分钟前的时间
        DateTime date = DateUtil.date();
        String endTime = DateUtil.formatDateTime(date);
        Date pre = DateUtils.addMinutes(date, -10);
        String startTime = DateUtil.formatDateTime(pre);

        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("start_time", startTime);
        params.put("end_time", endTime);
        JSONObject result = broadcastAlarmUtil.getLogList(params);
        log.info("log:{}",result);
        processLogResult(result);
    }

    // 处理日志结果
    private void processLogResult(JSONObject result) {
        if (result.getInt("code") == 200) {
            JSONArray data = result.getJSONArray("data");
            for (Object obj : data) {
                JSONObject log = (JSONObject) obj;
                Map<String, Object> uploadData = new HashMap<>();
                uploadData.put("portal_id", "1751847977770553345");
                uploadData.put("sub_project_id", "1801194524869922817");
                uploadData.put("task_name", log.get("task_name"));
                uploadData.put("task_no", log.get("task_id"));
                uploadData.put("task_type", "2");
                uploadData.put("task_execute_time", log.get("start_time"));
                uploadData.put("volume", "10");
                uploadData.put("task_equipment", "任务设备");
                uploadData.put("play_times", "1");
                uploadData.put("equipment_no", "99999,88888");
                uploadData.put("audio_type", "1");
                uploadData.put("audio_no", log.get("audio_no"));
                uploadData.put("content", log.get("content"));

                hdyHttpUtils.pushIOT(uploadData,"a9ab60bf-1430-486f-984b-8b7630b63b0d");

            }
        } else {
            log.error("查询任务日志失败: {}" , result.getStr("message"));
        }
    }


}
