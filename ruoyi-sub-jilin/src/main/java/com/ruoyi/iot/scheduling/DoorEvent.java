package com.ruoyi.iot.scheduling;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.iot.bean.DoorFunctionApi;
import com.ruoyi.iot.bean.EventsRequest;
import com.ruoyi.iot.bean.ThreadPool;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.service.IDeviceService;
import com.ruoyi.system.domain.SysWorkPeople;
import com.ruoyi.system.domain.SysWorkPeopleInoutLog;
import com.ruoyi.system.mapper.SysWorkPeopleInoutLogMapper;
import com.ruoyi.system.service.SysWorkPeopleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.joda.time.format.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;


/**
 * 门禁通行事件
 * event.put("orgname", doorevenDTO.getOrgName());
 * event.put("doorname", doorevenDTO.getDoorName());
 * event.put("owndoor",doorevenDTO.getDevName());
 * event.put("indecodex",doorevenDTO.getDoorIndexCode());
 */


@Component
public class DoorEvent {

    @Resource
    SysWorkPeopleInoutLogMapper sysWorkPeopleInoutLogMapper;

    @Resource
    SysWorkPeopleService workPeopleService;

    @Resource
    IDeviceService deviceService;

    private static final int WELINK_MODEL_ID = 200362;
    private static final Logger logger = LogManager.getLogger(DoorEvent.class);

    //    @Scheduled(cron = "0 */10 * * * ?")
    public void execute() {

        DateTime date = DateUtil.date();
        String now = DateUtil.formatDateTime(date);
        Date date1 = DateUtils.addMinutes(date, -10);
        String pre = DateUtil.formatDateTime(date1);

        logger.info("=========门禁通行事件===========");
        Map<String, List<Map<String, Object>>> request = new HashMap<>();
        List<Map<String, Object>> listt = new ArrayList<>();
        DoorFunctionApi doorFunctionApi = new DoorFunctionApi();
        EventsRequest eventsRequest = new EventsRequest(); //查询门禁事件
        eventsRequest.setPageNo(1); // 显示最后一个人
        eventsRequest.setPageSize(400);
        eventsRequest.setStartTime(getISO8601TimestampFromDateStr(pre));
        eventsRequest.setEndTime(getISO8601TimestampFromDateStr(now));
//           ArrayList<String> indexcodList = new ArrayList<String>();
//           indexcodList.add("ec8d96058dcb4dcca04468080c9570aa");
//           eventsRequest.setDoorIndexCodes(indexcodList); // 所有门禁标识
        logger.info("...门禁事件入参{}", JSON.toJSONString(eventsRequest));
        String doorcount = doorFunctionApi.events(eventsRequest);//查询门禁事件V2
        logger.info("...门禁事件返回参{}", doorcount);
        JSONObject jsonObject = JSONObject.parseObject(doorcount);
        JSONArray list = (JSONArray) ((JSONObject) jsonObject.get("data")).get("list");
        for (int i = 0; i < list.size(); i++) {
            JSONObject object = (JSONObject) list.get(i);
            if (object.get("certNo") == null && StringUtils.isEmpty(object.get("certNo").toString())) {
                continue;
            }
            if (object.get("picUri") == null && StringUtils.isEmpty(object.get("picUri").toString())) {
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("portal_id", "1751847977770553345");
            map.put("device_code", object.get("doorIndexCode").toString());//
            map.put("device_status", "在线");
            String url = "http://192.168.30.151" + object.get("picUri");
            map.put("record_Image_file", url);
            map.put("id_card", object.get("certNo"));
            map.put("in_out_direction", object.get("inAndOutType").toString().equals("1") ? "进" : "出");
            map.put("attendance_out_time", "");
            map.put("attendance_in_time", "");
            if (object.get("inAndOutType").toString().equals("1")) {
                map.put("attendance_in_time", getDateStrFromISO8601Timestamp(object.get("receiveTime").toString()));
            } else {
                map.put("attendance_out_time", getDateStrFromISO8601Timestamp(object.get("receiveTime").toString()));
            }
            map.put("type", "1");
            map.put("push_time", now);
            String devIndexCode = object.getString("devIndexCode");
            JSONObject door = getDoor(devIndexCode);
            DateTime eventTime = DateUtil.parse(getDateStrFromISO8601Timestamp(object.get("eventTime").toString()));
            insertInOutLog(door, map, eventTime);
            listt.add(map);
        }
        request.put("values", listt);
        logger.info("...推送业主入参{}", JSON.toJSONString(request));
        if (listt.size() == 0) return;
        String url = "http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/a01fa438-65cf-4da3-9bad-88a7878d0910";
        HttpResponse execute = HttpRequest.put(url).body(JSON.toJSONString(request), "application/json").execute();
        String body1 = execute.body();
        logger.info("...返回值{}", JSON.toJSONString(body1));
    }

    private static final Set<String> ALLOWED_SN = new HashSet<>();

    static {
        ALLOWED_SN.add("DS-K1T673TMW20230818V031000CHAG4966329");
        ALLOWED_SN.add("DS-K1T673M20230818V031000CHAG7090197");
        ALLOWED_SN.add("DS-K1T67XSBM20220908V030309CHK75967405");
    }

    private void insertInOutLog(JSONObject door, Map<String, Object> jsonObject, DateTime eventTime) {
        SysWorkPeopleInoutLog sysWorkPeopleInoutLog = new SysWorkPeopleInoutLog();
        String sn = door.get("devSerialNum").toString();
        if (!ALLOWED_SN.contains(sn)) {
            return;
        }
        sysWorkPeopleInoutLog.setSn(sn);

        Device one = deviceService.getOne(new LambdaQueryWrapper<Device>().eq(Device::getSn, sn), false);
        if (one != null) {
            if (com.ruoyi.common.utils.StringUtils.isNotEmpty(one.getModifyBy())) {
                sysWorkPeopleInoutLog.setSn(one.getModifyBy());
            }
        }

        SysWorkPeople workPeople = workPeopleService.getOne(
                new LambdaQueryWrapper<SysWorkPeople>()
                        .eq(SysWorkPeople::getIdCard, jsonObject.get("certNo")));
        if (workPeople != null) {
            sysWorkPeopleInoutLog.setSysWorkPeopleId(workPeople.getId());
        }



        sysWorkPeopleInoutLog.setIdCard(jsonObject.get("certNo").toString());
        sysWorkPeopleInoutLog.setMode(Integer.parseInt(jsonObject.get("inAndOutType").toString().equals("进") ? "1" : "0"));
        sysWorkPeopleInoutLog.setLogTime(DateUtil.formatDateTime(eventTime));
        sysWorkPeopleInoutLog.setName(jsonObject.get("personName").toString());
        //sysWorkPeopleInoutLog.setPhone(jsonObject.get("telephone").toString());
        sysWorkPeopleInoutLog.setPhotoUrl(jsonObject.get("record_Image_file").toString());
        sysWorkPeopleInoutLog.setCreatedDate(new Date());
        sysWorkPeopleInoutLog.setModifyDate(new Date());
        sysWorkPeopleInoutLogMapper.insert(sysWorkPeopleInoutLog);
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
        JSONObject door = (JSONObject) objects.get(0);
        return door;
    }

    public static String getISO8601TimestampFromDateStr(String timestamp) {
        java.time.format.DateTimeFormatter dtf1 = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(timestamp, dtf1);
        ZoneOffset offset = ZoneOffset.of("+08:00");
        OffsetDateTime date = OffsetDateTime.of(ldt, offset);
        java.time.format.DateTimeFormatter dtf2 = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        return date.format(dtf2);
    }


//    public static String getDateStrFromISO8601Timestamp(String ISOdate){
//        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//        OffsetDateTime offsetDateTime = OffsetDateTime.parse(ISOdate, formatter);
//
//        return offsetDateTime.toString();
//    }

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
            java.time.format.DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 如果时间中缺少秒或者分钟，需要补充0
            LocalDateTime normalizedDateTime = localDateTime
                    .withSecond(localDateTime.getSecond() == 0 ? 0 : localDateTime.getSecond())
                    .truncatedTo(ChronoUnit.SECONDS); // 去除毫秒

            return normalizedDateTime.format(outputFormatter);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date time format.");
        }
    }

}

