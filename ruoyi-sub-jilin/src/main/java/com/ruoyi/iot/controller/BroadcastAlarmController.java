package com.ruoyi.iot.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.IpBroadcast;
import com.ruoyi.iot.service.IIpBroadcastService;
import com.ruoyi.iot.utils.BroadcastAlarmUtil;
import com.ruoyi.iot.utils.HdyHttpUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import cn.hutool.json.JSONObject;

/**
 * IP广播报警
 *
 * @author hu_p
 * @date 2024/6/22
 */
@RestController
@Slf4j
@RequestMapping("broadcastalarm")
public class BroadcastAlarmController extends BaseController {

    @Autowired
    BroadcastAlarmUtil broadcastAlarmUtil;


    @Resource
    HdyHttpUtils hdyHttpUtils;


    @Autowired
    private IIpBroadcastService ipBroadcastService;

    /**
     * 查询IP广播列表
     */
    @PreAuthorize("@ss.hasPermi('system:broadcast:list')")
    @GetMapping("/list")
    @ApiOperation("查询IP广播列表")
    public TableDataInfo list(IpBroadcast ipBroadcast) {
        startPage();
        List<IpBroadcast> list = ipBroadcastService.selectIpBroadcastList(ipBroadcast);
        return getDataTable(list);
    }

    /**
     * 导出IP广播列表
     */
    @PreAuthorize("@ss.hasPermi('system:broadcast:export')")
    @Log(title = "IP广播", businessType = BusinessType.EXPORT)
    @ApiOperation("导出IP广播列表Excel")
    @PostMapping("/export")
    public void export(HttpServletResponse response, IpBroadcast ipBroadcast) {
        List<IpBroadcast> list = ipBroadcastService.selectIpBroadcastList(ipBroadcast);
        ExcelUtil<IpBroadcast> util = new ExcelUtil<IpBroadcast>(IpBroadcast.class);
        util.exportExcel(response, list, "IP广播数据");
    }

    /**
     * 获取IP广播详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:broadcast:query')")
    @ApiOperation("获取IP广播详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(name = "id", value = "IP广播参数", required = true)
                              @PathVariable("id") Long id) {
        return success(ipBroadcastService.selectIpBroadcastById(id));
    }

    /**
     * 新增IP广播
     */
    @PreAuthorize("@ss.hasPermi('system:broadcast:add')")
    @Log(title = "IP广播", businessType = BusinessType.INSERT)
    @ApiOperation("新增IP广播")
    @PostMapping
    public AjaxResult add(@RequestBody IpBroadcast ipBroadcast) {
        return toAjax(ipBroadcastService.insertIpBroadcast(ipBroadcast));
    }

    /**
     * 修改IP广播
     */
    @PreAuthorize("@ss.hasPermi('system:broadcast:edit')")
    @Log(title = "IP广播", businessType = BusinessType.UPDATE)
    @ApiOperation("修改IP广播")
    @PutMapping
    public AjaxResult edit(@RequestBody IpBroadcast ipBroadcast) {
        return toAjax(ipBroadcastService.updateIpBroadcast(ipBroadcast));
    }

    /**
     * 删除IP广播
     */
    @PreAuthorize("@ss.hasPermi('system:broadcast:remove')")
    @Log(title = "IP广播", businessType = BusinessType.DELETE)
    @ApiOperation("删除IP广播")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(name = "ids", value = "IP广播ids参数", required = true)
                             @PathVariable Long[] ids) {
        return toAjax(ipBroadcastService.deleteIpBroadcastByIds(ids));
    }

    /**
     * 获取广播报警设备
     */
    @GetMapping("device")
    public JSONObject deviceList(@RequestParam(required = false) Integer id,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer limit,
                                 @RequestParam(required = false) String keyword) {
        Map<String, Object> map = new HashMap<>();
        Optional.ofNullable(id).ifPresent(i -> map.put("id", i));
        Optional.ofNullable(page).ifPresent(p -> map.put("page", p));
        Optional.ofNullable(limit).ifPresent(l -> map.put("limit", l));
        Optional.ofNullable(keyword).ifPresent(k -> map.put("keyword", k));
        JSONObject deviceList = broadcastAlarmUtil.getDeviceList(map);
        return deviceList;
    }


    private static final String API_URL = "http://10.1.3.201:8090/v1/task_log";


    // 每10分钟查询一次定时任务的日志
    @GetMapping("queryScheduledTaskLogs")
    @Scheduled(cron = "40 13 * * * ?") // 每13分40秒执行一次
    public void queryScheduledTaskLogs() {
        log.info("============BroadcastAlarmController.queryScheduledTaskLogs================");
        if (isBroadcastingTime()) {
            broadcast15FieldArea();
        }
    }

    //15支洞场区广播
    public void broadcast15FieldArea() {
        IpBroadcast ipBroadcast = new IpBroadcast();
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        if (isTimeBetween()) {
            ipBroadcast.setTaskName("播放15支洞厂区进场安全须知（上午）");
            ipBroadcast.setTaskExecuteTime(currentDate.toString() + " 07:30:00");
        }else {
            ipBroadcast.setTaskName("播放15支洞厂区进场安全须知（下午）");
            ipBroadcast.setTaskExecuteTime(currentDate.toString() + " 14:00:00");
        }


        ipBroadcast.setTaskNo("1");
        ipBroadcast.setTaskType("1");
        ipBroadcast.setTaskEquipment("15支洞场区");
        QueryWrapper<IpBroadcast> ipBroadcastQueryWrapper = new QueryWrapper<>();
        ipBroadcastQueryWrapper.eq("equipment_no", "06FA10BB")
                .orderByDesc("id")
                .last("LIMIT 1");
        IpBroadcast latestIpBroadcast = ipBroadcastService.getOne(ipBroadcastQueryWrapper);
        int playTimes = 0;
        if (latestIpBroadcast != null) {
            playTimes = Integer.parseInt(latestIpBroadcast.getPlayTimes());
        }
        //音频文件1分22秒
        playTimes = playTimes + 10;
        ipBroadcast.setPlayTimes(String.valueOf(playTimes));
        ipBroadcast.setEquipmentNo("06FA10BB");
        ipBroadcast.setAudioType("1");
        processLogResult(ipBroadcast);
    }

    public static boolean isTimeBetween() {
        LocalTime now = LocalTime.now();
        // 定义开始时间和结束时间
        LocalTime startTime = LocalTime.of(7, 30);
        LocalTime endTime = LocalTime.of(12, 0);

        // 判断时间是否在区间内
        return !now.isBefore(startTime) && !now.isAfter(endTime);
    }


    public static boolean isBroadcastingTime() {
        // 定义工作时间的开始和结束
        LocalTime startTime = LocalTime.of(7, 30);
        LocalTime endTime = LocalTime.of(18, 30);

        // 定义中午休息时间
        LocalTime breakStart = LocalTime.of(12, 0);
        LocalTime breakEnd = LocalTime.of(14, 0);

        // 获取当前时间
        LocalTime now = LocalTime.now();

        // 判断当前时间是否在工作时间内且不在休息时间
        return (now.isAfter(startTime) || now.equals(startTime)) &&
                (now.isBefore(endTime) || now.equals(endTime)) &&
                (now.isBefore(breakStart) || now.isAfter(breakEnd));
    }

    // 处理日志结果
    private void processLogResult(IpBroadcast ipBroadcast) {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("portal_id", "1751847977770553345");
        valueMap.put("sub_project_id", "1801194524869922817");
        valueMap.put("task_name", ipBroadcast.getTaskName());
        valueMap.put("task_no", ipBroadcast.getTaskNo());
        valueMap.put("task_type", ipBroadcast.getTaskType());
        valueMap.put("task_execute_time", ipBroadcast.getTaskExecuteTime());
        valueMap.put("volume", "50");
        valueMap.put("task_equipment", ipBroadcast.getTaskEquipment());
        valueMap.put("play_times", ipBroadcast.getPlayTimes());
        valueMap.put("equipment_no", ipBroadcast.getEquipmentNo());
        valueMap.put("audio_type", ipBroadcast.getAudioType());
        valueMap.put("audio_no", ipBroadcast.getAudioNo());
        valueMap.put("content", "");

        List<Map<String, Object>> values = new ArrayList<>();
        values.add(valueMap);
        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put("values", values);
        //开始插入数据库
        ipBroadcastService.insertIpBroadcast(ipBroadcast);
        hdyHttpUtils.pushIOT(param, "a9ab60bf-1430-486f-984b-8b7630b63b0d");
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

                hdyHttpUtils.pushIOT(uploadData, "a9ab60bf-1430-486f-984b-8b7630b63b0d");

            }
        } else {
            log.error("查询任务日志失败: {}", result.getStr("message"));
        }
    }


}
