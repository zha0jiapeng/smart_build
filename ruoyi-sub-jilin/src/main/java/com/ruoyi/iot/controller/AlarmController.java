package com.ruoyi.iot.controller;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.iot.domain.Alarm;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.domain.Order;
import com.ruoyi.iot.service.IAlarmService;
import com.ruoyi.iot.service.IDeviceService;
import com.ruoyi.iot.service.RuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 报警Controller
 *
 * @author liang
 * @date 2024-08-21
 */
@RestController
@RequestMapping("/system/alarm")
@Api(tags = {"报警 Controller"})
public class AlarmController extends BaseController {
    @Autowired
    private IAlarmService alarmService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private IDeviceService deviceService;

    /**
     * 查询报警列表
     */
//    @PreAuthorize("@ss.hasPermi('system:alarm:list')")
    @GetMapping("/list")
    @ApiOperation("查询报警列表")
    public TableDataInfo list(Alarm alarm) {
        startPage();
        List<Alarm> list = alarmService.selectAlarmList(alarm);
        return getDataTable(list);
    }

    /**
     * 导出报警列表
     */
//    @PreAuthorize("@ss.hasPermi('system:alarm:export')")
    @Log(title = "报警", businessType = BusinessType.EXPORT)
    @ApiOperation("导出报警列表Excel")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Alarm alarm) {
        List<Alarm> list = alarmService.selectAlarmList(alarm);
        ExcelUtil<Alarm> util = new ExcelUtil<Alarm>(Alarm.class);
        util.exportExcel(response, list, "报警数据");
    }

    /**
     * 获取报警详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:alarm:query')")
    @ApiOperation("获取报警详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(name = "id", value = "报警参数", required = true)
                              @PathVariable("id") Long id) {
        return success(alarmService.selectAlarmById(id));
    }

    /**
     * 新增报警
     */
//    @PreAuthorize("@ss.hasPermi('system:alarm:add')")
    @Log(title = "报警", businessType = BusinessType.INSERT)
    @ApiOperation("新增报警")
    @PostMapping
    public AjaxResult add(@RequestBody Alarm alarm) {
        return toAjax(alarmService.insertAlarm(alarm));
    }

    /**
     * 修改报警
     */
//    @PreAuthorize("@ss.hasPermi('system:alarm:edit')")
    @Log(title = "报警", businessType = BusinessType.UPDATE)
    @ApiOperation("修改报警")
    @PutMapping
    public AjaxResult edit(@RequestBody Alarm alarm) {
        return toAjax(alarmService.updateAlarm(alarm));
    }

    /**
     * 删除报警
     */
//    @PreAuthorize("@ss.hasPermi('system:alarm:remove')")
    @Log(title = "报警", businessType = BusinessType.DELETE)
    @ApiOperation("删除报警")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(name = "ids", value = "报警ids参数", required = true)
                             @PathVariable Long[] ids) {
        return toAjax(alarmService.deleteAlarmByIds(ids));
    }

    /**
     * 报警数量
     */
    @ApiOperation("报警数量")
    @GetMapping("/alarmsNumber")
    public AjaxResult alarmsNumber() {
        QueryWrapper<Alarm> alarmQueryWrapper = new QueryWrapper<>();
        alarmQueryWrapper.eq("alarm_status", 0);
        int count = alarmService.count(alarmQueryWrapper);
        return success(count);
    }

    /**
     * 报警数量
     */
    @ApiOperation("报警设备数量")
    @GetMapping("/alarmsDeviceNumber")
    public AjaxResult alarmsDeviceNumber() {
        QueryWrapper<Alarm> alarmQueryWrapper = new QueryWrapper<>();
        alarmQueryWrapper.select("alarm_point");
        alarmQueryWrapper.eq("alarm_status", 0);
        alarmQueryWrapper.groupBy("alarm_point");
        List<Alarm> alarmList = alarmService.list(alarmQueryWrapper);
        return success(alarmList.size());
    }

    /**
     * 报警设备列表
     */
    @ApiOperation("报警设备列表")
    @GetMapping("/alarmDeviceList")
    public AjaxResult alarmDeviceList() {
//        startPage();
        Alarm alarm = new Alarm();
        alarm.setAlarmStatus(0);
        List<JSONObject> list = alarmService.selectAlarmDeviceList(alarm);
//        TableDataInfo pageInfo = getDataTable(list);
//        QueryWrapper<Alarm> alarmQueryWrapper = new QueryWrapper<>();
//        alarmQueryWrapper.select("alarm_point");
//        alarmQueryWrapper.eq("alarm_status", 0);
//        alarmQueryWrapper.groupBy("alarm_point");
//        List<Alarm> alarmList = alarmService.list(alarmQueryWrapper);
//        pageInfo.setTotal(alarmList.size());
        return success(list);
    }

    /**
     * 报警列表
     */
    @ApiOperation("报警列表")
    @GetMapping("/alarmList")
    public TableDataInfo alarmList(Long alarmPoint) {
        startPage();
        Alarm alarm = new Alarm();
        alarm.setAlarmStatus(0);
        alarm.setAlarmPoint(alarmPoint);
        List<Alarm> list = alarmService.selectAlarmList(alarm);
        return getDataTable(list);
    }

    /**
     * 报警状态修改
     */
    @ApiOperation("报警状态修改")
    @GetMapping("/alarmStatusModification")
    public AjaxResult alarmStatusModification(Long id) {
        QueryWrapper<Alarm> alarmQueryWrapper = new QueryWrapper<>();
        alarmQueryWrapper.eq("id", id);
        Alarm alarm = alarmService.getOne(alarmQueryWrapper);
        alarm.setAlarmStatus(2);
        return toAjax(alarmService.updateAlarm(alarm));
    }

    /**
     * 新增ThingsBoard状态报警
     */
    @ApiOperation("新增ThingsBoard报警")
    @GetMapping("/addThingsBoardAlarm")
    public void addThingsBoardAlarm(Order order) {
        //先判断设备id
        //由于当前信息跟设备表没有对应，只能手动去数据库中查找（sys_device）
        if (order.getDeviceId() == 65) {
            //由于当前信息跟设备表没有对应，只能手动去数据库中查找（sys_device）
            order.setAlarmPoint(65L);
        } else if (order.getDeviceId() == 52) {
            //由于当前信息跟设备表没有对应，只能手动去数据库中查找（sys_device）
            order.setAlarmPoint(52L);
        }

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 格式化当前时间
        String formattedDateTime = now.format(formatter);
        order.setAlarmTime(formattedDateTime);
        order.setAlarmCapture("");
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", order.getDeviceId());
        Device device = deviceService.getOne(queryWrapper);
        order.setAlarmContent("区域：" + device.getDeviceArea() + "；报警设备名称：" + device.getDeviceName() + "；报警内容：" + order.getAlarmType() + "；");
        order.setRemark("");

        //查设备id、报警类型id、报警状态
        QueryWrapper<Alarm> alarmQueryWrapper = new QueryWrapper<>();
        alarmQueryWrapper.eq("device_id", order.getDeviceId())
                .eq("alarm_type_id", order.getAlarmTypeId())
                .orderByDesc("id")
                .last("LIMIT 1");
        Alarm latestAlarm = alarmService.getOne(alarmQueryWrapper);
        if (latestAlarm == null) {
            ruleService.executeSignRule(order);
        } else {
            LocalDateTime alarmDateTime = LocalDateTime.parse(latestAlarm.getAlarmTime(), formatter);
            LocalDateTime alarmTime = LocalDateTime.parse(order.getAlarmTime(), formatter);
            Duration duration = Duration.between(alarmDateTime, alarmTime);
            //计算时间差，判断是否在5分钟以内
            if (!(duration.toMinutes() <= 5)) {
                ruleService.executeSignRule(order);
            }
        }
    }


    /**
     * 有毒有害气体超标实时监测
     */
    @ApiOperation("有毒有害气体超标实时监测")
    @GetMapping("/getHazardousGasAlarmCount")
    public AjaxResult getHazardousGasAlarmCount() {
        QueryWrapper<Alarm> alarmQueryWrapper = new QueryWrapper<>();
        alarmQueryWrapper.in("alarm_type_id", 3, 4, 5, 6, 7, 8, 9, 10);
        List<Alarm> list = alarmService.list(alarmQueryWrapper);
        int count = 0;
        if (list != null){
            count = list.size();
        }
        return success(count);
    }

}
