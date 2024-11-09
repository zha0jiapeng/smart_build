package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.bean.Staff;
import com.ruoyi.system.domain.SysWorkPeople;
import com.ruoyi.system.domain.SysWorkPeopleInoutLog;
import com.ruoyi.system.mapper.SysWorkPeopleInoutLogMapper;
import com.ruoyi.system.service.SysWorkPeopleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
@Slf4j
public class PeopleController {

    @Resource
    SysWorkPeopleInoutLogMapper sysWorkPeopleInoutLogMapper;

    @Resource
    private SysWorkPeopleService workPeopleService;

    //今日进洞人数
    @GetMapping("/countEnteredPeopleToday")
    public AjaxResult countEnteredPeopleToday( ){
        int inHoleNum = sysWorkPeopleInoutLogMapper.countEnteredPeopleToday();
         return AjaxResult.success(inHoleNum);
    }

    //现在在洞人数
    @GetMapping("/countOnlyEnteredPeopleToday")
    public AjaxResult inHoleNum( ){
        int inHoleNum = sysWorkPeopleInoutLogMapper.countOnlyEnteredPeopleToday();
        return AjaxResult.success(inHoleNum);
    }

    /**
     * 人员总览
     *
     * 获取今天进入场地但未离开的人数统计，以及按人员配置类型（personnel_config_type）统计的人数
     * @return
     */
    @GetMapping("/getPeopleNumStatistics")
    public Map<String,Object> getPeopleNumStatistics( ){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<SysWorkPeople> queryWrapper = Wrappers.query();
        queryWrapper.select("personnel_config_type, COUNT(*) AS count")
                .groupBy("personnel_config_type");
        List<Map<String, Object>> list = workPeopleService.listMaps(queryWrapper);
        Map<Integer, Map<String, Object>> defaultMap = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> typeMap = new HashMap<>();
            typeMap.put("personnel_config_type", i);
            typeMap.put("count", 0);
            defaultMap.put(i, typeMap);
        }
        // 更新默认结果的 count 值
        for (Map<String, Object> item : list) {
            Integer type = (Integer) item.get("personnel_config_type");
            defaultMap.put(type, item);
        }

        List<Map<String, Object>> finalList = new ArrayList<>(defaultMap.values());


        int inHoleNum = sysWorkPeopleInoutLogMapper.countOnlyEnteredPeopleToday();
        map.put("inHoleNum",inHoleNum);
        map.put("items",finalList);
        return AjaxResult.success(map);
    }


    /**
     * 施工人力统计
     *
     * 获取指定年月（格式：yyyy-MM）的考勤统计数据
     * @param yearMonth
     * @return
     */
    @GetMapping("/getAttendanceStatistics")
    public Map<String,Object> getPeopleAttendanceStatistics(String yearMonth){
        List<Map<String, Object>> list = sysWorkPeopleInoutLogMapper.getPeopleAttendanceStatistics(yearMonth);
        return AjaxResult.success(list);
    }

    /**
     * 作业人员趋势
     *
     * 获取指定年份，每月按人员配置类型统计的考勤数据
     * @param year
     * @return
     */
    @GetMapping("/getAttendanceStatisticsByPeopleType")
    public Map<String,Object> getAttendanceStatisticsByPeopleType(String year){
        List<Map<String, Object>> list = sysWorkPeopleInoutLogMapper.getMonthlyAttendanceCountByPersonnelConfigType(year);
        Map<String, Map<Integer, Integer>> monthDataMap = new LinkedHashMap<>();

        // 初始化所有可能的 personnel_config_type
        for (Map<String, Object> record : list) {
            String month = (String) record.get("month");
            Integer personnelConfigType = (Integer) record.get("personnel_config_type");
            Integer count = ((Long) record.get("attendance_count")).intValue();

            Map<Integer, Integer> attendanceMap = monthDataMap.computeIfAbsent(month, k -> {
                Map<Integer, Integer> initialMap = new LinkedHashMap<>();
                initialMap.put(1, 0);  // 默认值0
                initialMap.put(2, 0);
                initialMap.put(3, 0);
                return initialMap;
            });

            attendanceMap.put(personnelConfigType, count);
        }

        // 生成最终输出
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, Integer>> entry : monthDataMap.entrySet()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("month", entry.getKey());
            resultMap.put("personnelConfigTypeAttendance", entry.getValue());
            resultList.add(resultMap);
        }

        Map<String, Object> finalResult = new HashMap<>();
        finalResult.put("year", year);
        finalResult.put("data", resultList);
        return AjaxResult.success(finalResult);
    }

    /**
     * 工班分析
     *
     * 获取指定日期（格式：yyyy-MM-dd）按工种统计的考勤数据
     * @param today
     * @return
     */
    @GetMapping("/getPeopleAttendanceStatisticsByWorkType")
    public Map<String,Object> getPeopleAttendanceStatisticsByWorkType(String today){
        List<Map<String, Object>> list = sysWorkPeopleInoutLogMapper.getPeopleAttendanceStatisticsByWorkType(today);
        return AjaxResult.success(list);
    }

    /**
     * 分包单位分析
     *
     * 获取指定日期（格式：yyyy-MM-dd）按公司统计的考勤数据
     * @param today
     * @return
     */
    @GetMapping("/getPeopleAttendanceStatisticsByCompany")
    public Map<String,Object> getPeopleAttendanceStatisticsByCompany(String today){
        List<Map<String, Object>> list = sysWorkPeopleInoutLogMapper.getPeopleAttendanceStatisticsByCompany(today);
        return AjaxResult.success(list);
    }

    /**
     * 近七天作业人员分析
     *
     * 获取过去7天的每日考勤记录数据
     * @return
     */
    @GetMapping("/getPeopleAttendanceForLast7Days")
    public Map<String,Object> getPeopleAttendanceForLast7Days(){
        List<Map<String, Object>> list = sysWorkPeopleInoutLogMapper.countDailyAttendanceForLast7Days();
        return AjaxResult.success(list);
    }

    /**
     * 获取按不同人员配置类型的考勤率
     * @return
     */
    @GetMapping("/getAttendanceRateByPersonnelConfigType")
    public Map<String,Object> getAttendanceRateByPersonnelConfigType(){
        List<Map<String, Object>> list = sysWorkPeopleInoutLogMapper.getAttendanceRateByPersonnelConfigType();
        return AjaxResult.success(list);
    }

    /**
     * 获取当天在场停留时间的统计，包括停留的人员详细信息
     * @return
     */
    @GetMapping("/getStayStatistics")
    public Map<String,Object> getStayStatistics(){
        Map<String,Object> response = new HashMap<>();
        List<Map<String, Object>> list = sysWorkPeopleInoutLogMapper.getStayStatistics();
        Map<Object, List<Map<String, Object>>> mapp = list.stream().collect(Collectors.groupingBy(person -> {
            long hoursStayed = (long) person.get("hours_stayed");
            if (hoursStayed >= 0 && hoursStayed < 12) {
                return "onsite_people_list";
            } else if (hoursStayed >= 12 && hoursStayed <= 24) {
                return "onsite_people_over12_list";
            } else {
                return "onsite_people_over24_list";
            }
        }));
        response.put("onsite_people_data",mapp);
        HttpResponse execute = HttpRequest.post("http://192.168.1.200:9501/push/list")
                .body(com.alibaba.fastjson.JSON.toJSONString(new HashMap()), ContentType.JSON.getValue())
                .execute();
        JSONObject jsonObject = JSONUtil.parseObj(execute.body());
        JSONArray data = jsonObject.getJSONArray("data");
        int inHoleNum = data.size();
        Integer onsitePeopleCount = mapp.get("onsite_people_list").size();
        BigDecimal divide = new BigDecimal(inHoleNum).divide(new BigDecimal(onsitePeopleCount), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(0,RoundingMode.HALF_UP);
        response.put("wear_rate",divide.compareTo(new BigDecimal(100))>0?100:divide);
        return AjaxResult.success(response);
    }

    //保存或更新人员数据
    private void savePeople(List<Staff> staffList) {
        List<SysWorkPeople> list = new ArrayList<>();
        for (Staff staff : staffList) {
            SysWorkPeople workPeople = workPeopleService.getOne(
                    new LambdaQueryWrapper<SysWorkPeople>()
                            .eq(SysWorkPeople::getIdCard, staff.getIdCardNo())
                    , false);

            if(workPeople == null) {
                workPeople = new SysWorkPeople();
                workPeople.setCreatedDate(new Date());
            }else{
                workPeople.setModifyDate(new Date());
            }
            log.info("staff.getStaffName:{},{}",staff.getStaffName(),staff.getBimStaffType());
            workPeople.setName(staff.getStaffName());
            workPeople.setSex(staff.getSex().equals("男")?1:0);
            workPeople.setPhone(staff.getPhone());
            workPeople.setIdCard(staff.getIdCardNo());
            workPeople.setWorkType(staff.getWorkerType());
            workPeople.setCompany(staff.getLaborSubCom());
            workPeople.setKeyPersonnelFlag(staff.getKeyPersonnelFlag().equals("是")?1:0);

            switch (staff.getBimStaffType()){
                case "建设单位" : workPeople.setPersonnelConfigType(1); break;
                case "设计单位" : workPeople.setPersonnelConfigType(2); break;
                case "监理单位" : workPeople.setPersonnelConfigType(3); break;
                case "施工单位" : workPeople.setPersonnelConfigType(4); break;
                default:
            }
            //workPeople.setDepartureDate(staff.);
            workPeople.setYn(1);
            list.add(workPeople);
        }
        workPeopleService.saveOrUpdateBatch(list);
    }

}
