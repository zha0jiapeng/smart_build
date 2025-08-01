package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;

import com.ruoyi.iot.bean.DoorFunctionApi;
import com.ruoyi.iot.bean.EventsRequest;
import com.ruoyi.iot.bean.ExcelBean;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.listen.DemoDataListener;
import com.ruoyi.iot.service.IDeviceService;
import com.ruoyi.iot.utils.HdyHttpUtils;
import com.ruoyi.system.domain.SysWorkPeople;
import com.ruoyi.system.domain.SysWorkPeopleInoutLog;
import com.ruoyi.system.mapper.SysWorkPeopleInoutLogMapper;
import com.ruoyi.system.service.SysWorkPeopleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;


@RestController
@RequestMapping("/test")
@Slf4j
public class Controller {

    @Resource
    SysWorkPeopleInoutLogMapper sysWorkPeopleInoutLogMapper;

    @Resource
    SysWorkPeopleService workPeopleService;

    @Resource
    IDeviceService deviceService;

    @Resource
    HdyHttpUtils hdyHttpUtils;


    @RequestMapping("/peopleImport")
    public String peopleImport() throws Exception {
        String fileName = "/home/mashir0/副本14支洞人员基础信息.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, ExcelBean.class, new DemoDataListener()).sheet().doRead();
        return null;
    }

    @PostMapping("/hikPush")
    public void hikPush(@RequestBody Map<String, String> requestt) throws Exception {
        String end = requestt.get("end");
        String start = requestt.get("start");
        String name = requestt.get("name");
        String deviceCode = "";
        if (StringUtils.isNotEmpty(requestt.get("deviceCode"))){
            deviceCode = requestt.get("deviceCode");
        }
        log.info("...start{}...end:{}", start, end);
        log.info("=========门禁通行事件===========");

        fetchEventsRecursively(start, end, name, 1, deviceCode);  // 从第一页开始递归


    }

    private void pushHdy(JSONArray list, String deviceCode) {
        String now = DateUtil.now();
        Map<String, List<Map<String, Object>>> request = new HashMap<>();
        List<Map<String, Object>> listt = new ArrayList<>();
        DoorFunctionApi doorFunctionApi = new DoorFunctionApi();
        for (int i = 0; i < list.size(); i++) {
            JSONObject object = (JSONObject) list.get(i);
            String devIndexCode = object.getString("devIndexCode");
            JSONObject door = getDoor(devIndexCode);
            if (door == null) continue;
            String sn = door.get("devSerialNum").toString();
            if (deviceCode != null && !deviceCode.equals("")) {
                if (!deviceCode.equals(sn)) {
                    continue;
                }
            }
            if (object.get("certNo") == null || StringUtils.isEmpty(object.get("certNo").toString())) {
                continue;
            }
            if (object.get("picUri") == null || StringUtils.isEmpty(object.get("picUri").toString())) {
                Map<String, Object> map = new HashMap<>();
                map.put("pageNo", 1);
                map.put("pageSize", 1);
                map.put("certificateNo", object.get("certNo").toString());
                String s = doorFunctionApi.personList(map);
                JSONObject jsonObject1 = JSONObject.parseObject(s);
                JSONObject data = jsonObject1.getJSONObject("data");
                if (data != null) {
                    JSONArray list1 = data.getJSONArray("list");
                    if (list1 != null && list1.size() > 0) {
                        object.put("picUri", list1.getJSONObject(0).getJSONArray("personPhoto").getJSONObject(0).getString("picUri"));
                    }
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("portal_id", "1751847977770553345");
            //  map.put("device_code", object.get("doorIndexCode").toString());
            map.put("device_status", "在线");
            String url = "http://10.1.3.2" + object.get("picUri");
            map.put("record_Image_file", hdyHttpUtils.pushPicture(url));
            map.put("record_Image_file_InOutLog", url);
            map.put("id_card", object.get("certNo"));
            map.put("in_out_direction", object.get("inAndOutType").toString().equals("1") ? "进" : "出");
            map.put("attendance_out_time", "");
            map.put("attendance_in_time", "");
            if (object.get("inAndOutType").toString().equals("1")) {
                map.put("attendance_in_time", getDateStrFromISO8601Timestamp(object.get("eventTime").toString()));
            } else {
                map.put("attendance_out_time", getDateStrFromISO8601Timestamp(object.get("eventTime").toString()));
            }

            map.put("push_time", now);

            Device one = deviceService.getOne(new LambdaQueryWrapper<Device>().eq(Device::getSn, sn), false);
            if (one == null) {
                log.error("找不到设备sn:{}", sn);
                continue;
            }
            map.put("type", one.getCameraType());

            map.put("device_code", sn);
            DateTime eventTime = DateUtil.parse(getDateStrFromISO8601Timestamp(object.get("eventTime").toString()));
            String personName = object.get("personName").toString();
            SysWorkPeopleInoutLog sysWorkPeopleInoutLog = insertInOutLog(door, map, eventTime, personName, one);
            if (sysWorkPeopleInoutLog == null) continue;
            map.put("id", sysWorkPeopleInoutLog.getId());
            map.remove("record_Image_file_InOutLog");
            listt.add(map);
        }
        request.put("values", listt);
        log.info("...推送业主入参{}", JSON.toJSONString(request));
        if (listt.size() == 0) return;
        String url = "http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/a01fa438-65cf-4da3-9bad-88a7878d0910";
        HttpResponse execute = HttpRequest.put(url).body(JSON.toJSONString(request), "application/json").execute();
        String body1 = execute.body();
        log.info("...返回值{}", JSON.toJSONString(body1));
    }

    private void fetchEventsRecursively(String start, String end, String name, int page, String deviceCode) {
        JSONObject data = getObjects(end, start, name, page);

        if (data != null) {
            Integer total = data.getInteger("total");
            JSONArray list = data.getJSONArray("list");

            if (list != null && !list.isEmpty()) {
                pushHdy(list, deviceCode);
            }

            log.info("当前页码: {}, 当前页事件数: {}, 总事件数: {}", page, list != null ? list.size() : 0, total);

            // 如果 total 大于当前页的大小，说明还有下一页
            if (total != null && total > page * 1000) {

                // 递归调用下一页
                fetchEventsRecursively(start, end, name, page + 1, deviceCode);
            }
        } else {
            log.warn("未获取到有效的事件数据");
        }
    }

    private static JSONObject getObjects(String end, String start, String name, Integer page) {
        DoorFunctionApi doorFunctionApi = new DoorFunctionApi();
        EventsRequest eventsRequest = new EventsRequest(); //查询门禁事件
        eventsRequest.setPageNo(page); // 显示最后一个人
        eventsRequest.setPageSize(1000);
        eventsRequest.setStartTime(getISO8601TimestampFromDateStr(start));
        eventsRequest.setEndTime(getISO8601TimestampFromDateStr(end));
        eventsRequest.setPersonName(name);
        log.info("...门禁事件入参{}", JSON.toJSONString(eventsRequest));
        String doorcount = doorFunctionApi.events(eventsRequest);//查询门禁事件V2
        JSONObject jsonObject = JSONObject.parseObject(doorcount);
        JSONObject data = (JSONObject) jsonObject.get("data");
        return data;
    }

    public static String getISO8601TimestampFromDateStr(String timestamp) {
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(timestamp, dtf1);
        ZoneOffset offset = ZoneOffset.of("+08:00");
        OffsetDateTime date = OffsetDateTime.of(ldt, offset);
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        return date.format(dtf2);
    }

    public static String getDateStrFromISO8601Timestamp(String dateTimeStr) {
        // 检查并补全不完整的时间字符串
        if (dateTimeStr.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}\\+\\d{2}:\\d{2}$")) {
            // 如果只有小时信息，补充 :00:00
            dateTimeStr = dateTimeStr.replaceFirst("(\\d{2})\\+(\\d{2}:\\d{2})$", "$1:00:00+$2");
        } else if (dateTimeStr.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}\\+\\d{2}:\\d{2}$")) {
            // 如果没有秒信息，补充 :00
            dateTimeStr = dateTimeStr.replaceFirst("(\\d{2}:\\d{2})\\+(\\d{2}:\\d{2})$", "$1:00+$2");
        }

        try {
            // 尝试将输入字符串解析为 OffsetDateTime
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeStr);

            // 将 OffsetDateTime 转换为 LocalDateTime
            LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();

            // 确保格式为 yyyy-MM-dd HH:mm:ss
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 如果时间中缺少秒或者分钟，需要补充0
            LocalDateTime normalizedDateTime = localDateTime
                    .withSecond(localDateTime.getSecond() == 0 ? 0 : localDateTime.getSecond())
                    .truncatedTo(ChronoUnit.SECONDS); // 去除毫秒

            return normalizedDateTime.format(outputFormatter);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date time format.");
        }
    }


    private SysWorkPeopleInoutLog insertInOutLog(JSONObject door, Map<String, Object> jsonObject, DateTime eventTime, String personName, Device device) {
        SysWorkPeopleInoutLog sysWorkPeopleInoutLog = new SysWorkPeopleInoutLog();
        String sn = door.get("devSerialNum").toString();
//        if (!ALLOWED_SN.contains(sn)) {
//            return null;
//        }
        sysWorkPeopleInoutLog.setSn(sn);

        if (device != null) {
            if (com.ruoyi.common.utils.StringUtils.isNotEmpty(device.getModifyBy())) {
                sysWorkPeopleInoutLog.setSn(device.getModifyBy());
            }
        }

        SysWorkPeople workPeople = workPeopleService.getOne(
                new LambdaQueryWrapper<SysWorkPeople>()
                        .eq(SysWorkPeople::getIdCard, jsonObject.get("id_card").toString()));
        if (workPeople != null) {
            sysWorkPeopleInoutLog.setSysWorkPeopleId(workPeople.getId());
        }

        Integer certNo = sysWorkPeopleInoutLogMapper.selectCount(
                new LambdaQueryWrapper<SysWorkPeopleInoutLog>()
                        .eq(SysWorkPeopleInoutLog::getIdCard, jsonObject.get("id_card").toString())
                        .eq(SysWorkPeopleInoutLog::getLogTime, DateUtil.formatDateTime(eventTime))
        );
        if (certNo > 1) {
            return null;
        }


        sysWorkPeopleInoutLog.setIdCard(jsonObject.get("id_card").toString());
        sysWorkPeopleInoutLog.setMode(Integer.parseInt(jsonObject.get("in_out_direction").toString().equals("进") ? "1" : "0"));
        sysWorkPeopleInoutLog.setLogTime(DateUtil.formatDateTime(eventTime));
        sysWorkPeopleInoutLog.setName(personName);
        //sysWorkPeopleInoutLog.setPhone(jsonObject.get("telephone").toString());
        sysWorkPeopleInoutLog.setPhotoUrl(jsonObject.get("record_Image_file_InOutLog").toString());
        sysWorkPeopleInoutLog.setCreatedDate(new Date());
        sysWorkPeopleInoutLog.setModifyDate(new Date());
        sysWorkPeopleInoutLogMapper.insert(sysWorkPeopleInoutLog);
        log.info("进行插入数据库操作：{}", sysWorkPeopleInoutLog);
        return sysWorkPeopleInoutLog;
    }

    private static JSONObject getDoor(String devIndexCode) {
        // 创建根Map
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("pageNo", 1);
        rootMap.put("pageSize", 1);

        // 创建expressions列表
        List<Map<String, Object>> expressionsList = new ArrayList<>();

        // 创建expression Map
        Map<String, Object> expressionMap = new HashMap<>();
        expressionMap.put("key", "indexCode");
        expressionMap.put("operator", 0);

        // 创建values列表
        List<String> valuesList2 = new ArrayList<>();
        valuesList2.add(devIndexCode);

        // 将values列表添加到expression Map中
        expressionMap.put("values", valuesList2);

        // 将expression Map添加到expressions列表中
        expressionsList.add(expressionMap);

        // 将expressions列表添加到根Map中
        rootMap.put("expressions", expressionsList);
        DoorFunctionApi doorFunctionApi = new DoorFunctionApi();

        JSONObject JSONObject = doorFunctionApi.search(rootMap);
        JSONArray objects = (JSONArray) ((JSONObject) JSONObject.get("data")).get("list");
        if (objects == null || objects.isEmpty()) {
            log.info("indexCode:{},找不到门禁信息.", devIndexCode);
            return null;
        }
        JSONObject door = (JSONObject) objects.get(0);
        return door;
    }


    public static void main(String[] args) {
        String iso8601TimestampFromDateStr = getISO8601TimestampFromDateStr("2024-07-21 00:00:00");
        System.out.println(iso8601TimestampFromDateStr);
    }
}
