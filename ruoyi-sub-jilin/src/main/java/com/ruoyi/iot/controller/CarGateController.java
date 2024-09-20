package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.config.MinioConfig;
import com.ruoyi.common.utils.MinioUtils;
import com.ruoyi.iot.bean.DoorFunctionApi;
import com.ruoyi.iot.enums.VehicleType;
import com.ruoyi.iot.utils.HdyHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/car")
public class CarGateController {

    @Resource
    HdyHttpUtils hdyHttpUtils;

    @Resource
    MinioUtils minioUtils;

    @Resource
    MinioConfig minioConfig;


    @PostMapping("/carAccess")
    public Map<String, Object> carAccess(@RequestBody Map<String, Object> request) {
//        log.info("carAccess:{}", JSON.toJSONString(request));
        pushCarAccess(request);
        return request;
    }


//    public static void main(String[] args) {
//        String jsonString = "{\"AlarmInfoPlate\":{\"channel\":0,\"deviceName\":\"IVS\",\"ipaddr\":\"10.1.3.210\",\"result\":{\"PlateResult\":{\"bright\":0,\"carBright\":0,\"carColor\":255,\"clean_time\":0,\"colorType\":1,\"colorValue\":0,\"confidence\":91,\"direction\":4,\"gioouts\":[{\"ctrltype\":0,\"ionum\":\"0\"},{\"ctrltype\":0,\"ionum\":\"1\"},{\"ctrltype\":0,\"ionum\":\"2\"}],\"imageFile\":\"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\",\"imageFileLen\":16431,\"imageFragmentFile\":\"/9j/4AAQSkZJRgABAQIAdgB2AAD/7wAPAAAAAAAAAAAAAAAAAP/bAEMAGxIUFxQRGxcWFx4cGyAoQisoJSUoUTo9MEJgVWVkX1VdW2p4mYFqcZBzW12FtYaQnqOrratngLzJuqbHmairpP/bAEMBHB4eKCMoTisrTqRuXW6kpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpP/AABEIAFAAwAMBIgACEQEDEQH/xAAfAAABBQEBAQEBAQAAAAAAAAAAAQIDBAUGBwgJCgv/xAC1EAACAQMDAgQDBQUEBAAAAX0BAgMABBEFEiExQQYTUWEHInEUMoGRoQgjQrHBFVLR8CQzYnKCCQoWFxgZGiUmJygpKjQ1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4eLj5OXm5+jp6vHy8/T19vf4+fr/xAAfAQADAQEBAQEBAQEBAAAAAAAAAQIDBAUGBwgJCgv/xAC1EQACAQIEBAMEBwUEBAABAncAAQIDEQQFITEGEkFRB2FxEyIygQgUQpGhscEJIzNS8BVictEKFiQ04SXxFxgZGiYnKCkqNTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqCg4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2dri4+Tl5ufo6ery8/T19vf4+fr/2gAMAwEAAhEDEQA/AIgoxkml2js2fwpOwopJFNi7fcUYooo5QuGKTFLRSaC4lHNLmkzRYLiUUuaOKLBcTcfWl3uP4jScUcUWDQPMfu2aPMbuaTFG2iw9ALt/eNG4+ppMGkxRYNBdxBzk0mT60YNJilYBc+9Jmkop2AXPFJmikpWYATSUUUajLHYCl64AHNIOi+lLnB4q4kMcw447U2jIHSkpiHKKQ9acg4zSP1pDG0D3opKAH7M96TYfWm5qRPlG49uaNQI8c4pSjdhTcmneY3t+VMBMH0NNzU6sQoY1AaBBmjNABJwOtSSKiJ1O7tVWQEeaDTScVM0a9yRSaQXIc0ZqXyVPRyKiddjlcg47imkguGRRxRIhQKT0am9afKK47CmmMMEj0pxBVsHimycOR6GpkikyfogpyZ+uaaeFUHtTx1xntSWwmB+9Te9OPXim96aAf0Skf7oNOZSV47dqQgiE57UgGDk4pDkHBozSU7DFUZYD1p0rAAKKWIfec1GzZYk0hCZp8SBzzzjnFR0e9VYCS43cE/dqOpoHLZifkEfmKgYbSVHUcUR8wJ4AAhkaoWYs2TVllXy9rfdxg1G1uCm+JsgdRSuriIG6VNd8JH6E/wBKgJBXvVtjE8YYsMAD8KqQFTvnJpGPFWFhjmQmJhuHp/WqzE7Tngjg+1UrMCxdf6uOoAfmH1qxdhjDFtBOMZxVbJ3KMHOfSiK0ETXPEi81DLkSMDU15/rY8eh/nUEvEhNQ9ikWM5RTnNOLZpqEOgUfeBzj2pdjelTGwMXOWpAcuPTNBRh1FJg+lXoImlYqw2miJvMBBHNQU4ccjrUtWGJ9aTP60Zozg1SETSArEVX6VAfcH8ql+0N/cX86UXK/xI2PrUq6AhGMgHoafNH5eCuSDTJGDuWA2g9B6VLHMrLsmHA6Maq4DLfmYewNExH2gg9NwBqVZLeAbkIY+nc1VOWySeTyaFqwLF7ncoP3ST+NMtWKTgLwCDkVMNt3DtORIvpRDbiDMjnp3IxS0tYCvONs7joOtRkD0pZZDJI7469KtTp9ptUeEZK9QBz0q1puIr27FblCO/ymi/AW4YeqgmnWsMjzoWUqoOeaZfFWuHwc7VAp6c2grlm4l8mGNlGSwAqP7bwoeLGSBkGlvf8Aj2iPTkfyqoT0z0yKmMU0O5Yv/lkh/EfrVe6IE5AqzqfDw8d6rXi/6QcdAKVtBp6kmAetHI6E/nSgUYrC5pYAzjo7D8aXzH9aSimKw7zXI5wfwpN5ptFK4WHh17qfzo3RnruH61HRTuxWJP3R/wCWuD7il2qTgSrUNJVKbFyk/lP22n8aDDL/AHDVfA9KXLDozD8arnYcpIVZeqEfhSU0TSjpI1OFxMP4gfqKftPIXKxM5OQefY0MS33mLfWl+0uVw0cbD6YpBcRn71uAR6GnzrsKzEzUkZlhO+NtuexGQaYZbf8AuSL9DmgzoRjJH1olNdASJnvZmBGFU46jk1WI4I9aeFhYf8fCg+4pwgBHyzRn8cVUXETHT3CS28cYXDr1PrVc9vYg1KbabsqH6OKT7NcAZMLY9RzWkeUm5JqMsUzQ+WwbucdulQ3rmO7+UDpnkU1o5FO0xsDnuKXUh/pp9QvPtWckkVFn/9k=\",\"imageFragmentFileLen\":1853,\"isoffline\":0,\"license\":\"渝AZ365H\",\"license_ext_type\":0,\"location\":{\"RECT\":{\"bottom\":97,\"left\":378,\"right\":428,\"top\":77}},\"plateid\":1971,\"plates\":[{\"color\":1,\"imageFragmentFile\":\"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\",\"imageFragmentFileLen\":1853,\"license\":\"渝AZ365H\",\"plate_width\":183,\"pos\":{\"bottom\":97,\"left\":378,\"right\":428,\"top\":77},\"type\":1}],\"timeStamp\":{\"Timeval\":{\"decday\":14,\"dechour\":8,\"decmin\":19,\"decmon\":9,\"decsec\":8,\"decyear\":2024,\"sec\":1726273148,\"usec\":449529}},\"timeUsed\":0,\"triggerType\":8,\"type\":1}},\"rule_id\":0,\"serialno\":\"f106f4e2-49788a49\",\"user_data\":\"\"}}";
//
//        // Parse the JSON string into a JSONObject
//        JSONObject jsonObject = JSON.parseObject(jsonString);
//        JSONObject alarmInfoPlate = jsonObject.getJSONObject("AlarmInfoPlate");
//        //设备名称
//        String deviceName = alarmInfoPlate.getString("deviceName");
//        JSONObject result = alarmInfoPlate.getJSONObject("result");
//        JSONObject plateResult = result.getJSONObject("PlateResult");
//        //抓拍照片
//        String imageFile = plateResult.getString("imageFile");
//        String isoffline = plateResult.getString("isoffline");
//        //设备工作状态
//        String onlineStatus = "0".equals(isoffline) ? "在线" : "离线";
//        //车牌号码
//        String plateNumber = plateResult.getString("license");
//        JSONObject timeStamp = plateResult.getJSONObject("timeStamp");
//        JSONObject timeval = timeStamp.getJSONObject("Timeval");
//        int decYear = timeval.getIntValue("decyear");
//        int decMon = timeval.getIntValue("decmon");
//        int decDay = timeval.getIntValue("decday");
//        int decHour = timeval.getIntValue("dechour");
//        int decMin = timeval.getIntValue("decmin");
//        int decSec = timeval.getIntValue("decsec");
//        LocalDateTime recogDateTime = LocalDateTime.of(decYear, decMon, decDay, decHour, decMin, decSec);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        //抓拍时间
//        String recogTime = recogDateTime.format(formatter);
//        //进出类型
//        String ipaddr = alarmInfoPlate.getString("ipaddr");
//        String inOutType = "10.1.3.210".equals(ipaddr) ? "进" : "出";
//
//        //车辆类型
//        JSONArray platesArray = plateResult
//                .getJSONArray("plates");
//        // 获取plates数组中的第一个对象的type字段
//        int plateType = 0;
//        if (platesArray != null && !platesArray.isEmpty()) {
//            for (int i = 0; i < platesArray.size(); i++) {
//                plateType = platesArray.getJSONObject(i).getIntValue("type");
//            }
//        } else {
//            System.out.println("No plates found.");
//        }
//        String carType = VehicleType.getRemarkByCode(String.valueOf(plateType));
//        //门户ID
//        String portalId = "1751847977770553345";
//        //标段ID
//        String subProjectId = "1801194524869922817";
//        //推送时间
//        LocalDateTime currentTime = LocalDateTime.now();
//        DateTimeFormatter formatterNow = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedCurrentTime = currentTime.format(formatterNow);
//        //准入方式
//        JSONArray giooutsArray = plateResult
//                .getJSONArray("gioouts");
//        int ctrltype = 0;
//        if (giooutsArray != null && !giooutsArray.isEmpty()) {
//            ctrltype = giooutsArray.getJSONObject(0).getIntValue("ctrltype");
//        } else {
//            System.out.println("No plates found.");
//        }
//        String allowInType = String.valueOf(ctrltype);
//
//        //设备编号
//        String deviceCode = "";
//        //报警类型
//        String alarmType = "";
//        //驾驶员
//        String driver = "";
//        //驾驶员手机号
//        String phoneNumber = "";
//        //其他
//        String other = "";
//
//        log.info("portal_id：" + portalId);
//        log.info("        sub_project_id：" + subProjectId);
//        log.info("device_code：" + deviceCode);
//        log.info("        device_name：" + deviceName);
//        log.info("work_status：" + onlineStatus);
//        log.info("        video_streaming：" + imageFile);
//        log.info("license_number：" + plateNumber);
//        log.info("        data_time：" + recogTime);
//        log.info("in_out_type：" + inOutType);
//        log.info("        alarm_type：" + alarmType);
//        log.info("car_type：" + carType);
//        log.info("        create_time：" + formattedCurrentTime);
//        log.info("driver：" + driver);
//        log.info("        phone_number：" + phoneNumber);
//        log.info("allow_in_type：" + allowInType);
//        log.info("        other：" + other);
//
//
//    }

    private void pushCarAccess(Map<String, Object> request) {
        JSONObject jsonObject = new JSONObject(request);
        JSONObject alarmInfoPlate = jsonObject.getJSONObject("AlarmInfoPlate");
        //设备名称
        String deviceName = alarmInfoPlate.getString("deviceName");
        JSONObject result = alarmInfoPlate.getJSONObject("result");
        JSONObject plateResult = result.getJSONObject("PlateResult");
        //抓拍照片
        String imageFile = plateResult.getString("imageFile");
        String picture = imageFile.replaceAll("\\-", "\\+")
                .replaceAll("\\_", "\\/").replaceAll("\\.", "\\=");
        InputStream inputStream = minioUtils.base64ToInputStream(picture);
        String filename = UUID.randomUUID().toString() + ".png";
        minioUtils.uploadFile(minioConfig.getCarAccessBucketName(), filename, inputStream);
        //String presignedObjectUrl = minioUtils.getPresignedObjectUrl("car-access", filename);
        imageFile = minioConfig.getEndpoint() + "/" + minioConfig.getCarAccessBucketName() + "/" + filename;


        //设备工作状态
        String isoffline = plateResult.getString("isoffline");
        String onlineStatus = "0".equals(isoffline) ? "在线" : "离线";
        //车牌号码
        String plateNumber = plateResult.getString("license");
        JSONObject timeStamp = plateResult.getJSONObject("timeStamp");
        JSONObject timeval = timeStamp.getJSONObject("Timeval");
        int decYear = timeval.getIntValue("decyear");
        int decMon = timeval.getIntValue("decmon");
        int decDay = timeval.getIntValue("decday");
        int decHour = timeval.getIntValue("dechour");
        int decMin = timeval.getIntValue("decmin");
        int decSec = timeval.getIntValue("decsec");
        LocalDateTime recogDateTime = LocalDateTime.of(decYear, decMon, decDay, decHour, decMin, decSec);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //抓拍时间
        String recogTime = recogDateTime.format(formatter);
        //进出类型
        String ipaddr = alarmInfoPlate.getString("ipaddr");
        String inOutType = "10.1.3.210".equals(ipaddr) ? "进" : "出";
        //车辆类型
        JSONArray platesArray = plateResult
                .getJSONArray("plates");
        // 获取plates数组中的第一个对象的type字段
        int plateType = 0;
        if (platesArray != null && !platesArray.isEmpty()) {
            for (int i = 0; i < platesArray.size(); i++) {
                plateType = platesArray.getJSONObject(i).getIntValue("type");
            }
        } else {
            System.out.println("No plates found.");
        }
        String carType = VehicleType.getRemarkByCode(String.valueOf(plateType));
        //门户ID
        String portalId = "1751847977770553345";
        //标段ID
        String subProjectId = "1801194524869922817";
        //推送时间
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatterNow = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCurrentTime = currentTime.format(formatterNow);
        //准入方式
        JSONArray giooutsArray = plateResult
                .getJSONArray("gioouts");
        int ctrltype = 0;
        if (giooutsArray != null && !giooutsArray.isEmpty()) {
            ctrltype = giooutsArray.getJSONObject(0).getIntValue("ctrltype");
        } else {
            System.out.println("No plates found.");
        }
        String allowInType = String.valueOf(ctrltype);
        //设备编号
        String deviceCode = "10.1.3.210".equals(ipaddr) ? "f106f4e2-49788a49" : "4e32e371-4291fb9a";
        //报警类型
        String alarmType = "";
        //驾驶员
        String driver = "";
        //驾驶员手机号
        String phoneNumber = "";
        //其他
        String other = "";
        Map<String, Object> value = new HashMap<>();
        value.put("portal_id", portalId);
        value.put("sub_project_id", subProjectId);
        value.put("device_code", deviceCode);
        value.put("device_name", deviceName);
        value.put("work_status", onlineStatus);
        value.put("video_streaming", imageFile);
        value.put("license_number", plateNumber);
        value.put("data_time", recogTime);
        value.put("in_out_type", inOutType);
        value.put("alarm_type", alarmType);
        value.put("car_type", carType);
        value.put("create_time", formattedCurrentTime);
        value.put("driver", driver);
        value.put("phone_number", phoneNumber);
        value.put("allow_in_type", allowInType);
        value.put("other", other);

        List<Map<String, Object>> values = new ArrayList<>();
        values.add(value);
        Map<String, List<Map<String, Object>>> rootMap = new HashMap<>();
        rootMap.put("values", values);

        hdyHttpUtils.pushIOT(rootMap, "bbe55ec4-fc7b-4cd1-a704-1f07964b82d6");

    }

    /**
     * 14号洞口海康道闸
     *
     * @return
     */
    @GetMapping("/crossRecords/page")
    public void carAccessHik() {
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("pageNo", 1);
        rootMap.put("pageSize", 100);
        ZonedDateTime endTime = ZonedDateTime.now();
        ZonedDateTime startTime = endTime.minusMinutes(1000);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String formattedEndTime = endTime.format(formatter);
        String formattedStartTime = startTime.format(formatter);
        rootMap.put("startTime", formattedStartTime);
        rootMap.put("endTime", formattedEndTime);
        DoorFunctionApi doorFunctionApi = new DoorFunctionApi();
        JSONObject JSONObject = doorFunctionApi.crossRecordsPage(rootMap);
        JSONArray objects = (JSONArray) ((JSONObject) JSONObject.get("data")).get("list");
        for (Object value : objects) {
            JSONObject jsonObject = (JSONObject) value;
            //设备名称（无）

            //抓拍照片
            String imageFile = "http://10.1.3.2" + jsonObject.getString("vehiclePicUri");

            //设备工作状态
            String onlineStatus = "在线";

            //车牌号码
            String plateNumber = jsonObject.getString("plateNo");

            //抓拍时间
            String crossTime = jsonObject.getString("crossTime");
            String recogTime = convertIso8601ToCustomFormat(crossTime);

            //进出类型
            String vehicleOut = jsonObject.getString("vehicleOut");
            String inOutType = "0".equals(vehicleOut) ? "进" : "出";

            //车辆类型
            String vehicleType = jsonObject.getString("vehicleType");
            String carType = VehicleType.getRemarkByCode("hik" + vehicleType);

            //门户ID
            String portalId = "1751847977770553345";
            //标段ID
            String subProjectId = "1801194524869922817";

            //推送时间
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatterNow = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedCurrentTime = currentTime.format(formatterNow);

            //准入方式
            String releaseWay = jsonObject.getString("releaseWay");
            String allowInType = VehicleType.getRemarkByCode("hik" + releaseWay);

            //设备编号
            String deviceCode = jsonObject.getString("entranceSyscode");
            //报警类型
            String alarmType = "";
            //驾驶员
            String driver = "";
            //驾驶员手机号
            String phoneNumber = "";
            //其他
            String other = "";

            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("portal_id", portalId);
            valueMap.put("sub_project_id", subProjectId);
            valueMap.put("device_code", deviceCode);
//            valueMap.put("device_name", deviceName);
            valueMap.put("work_status", onlineStatus);
            valueMap.put("video_streaming", imageFile);
            valueMap.put("license_number", plateNumber);
            valueMap.put("data_time", recogTime);
            valueMap.put("in_out_type", inOutType);
            valueMap.put("alarm_type", alarmType);
            valueMap.put("car_type", carType);
            valueMap.put("create_time", formattedCurrentTime);
            valueMap.put("driver", driver);
            valueMap.put("phone_number", phoneNumber);
            valueMap.put("allow_in_type", allowInType);
            valueMap.put("other", other);

            List<Map<String, Object>> values = new ArrayList<>();
            values.add(valueMap);
            Map<String, List<Map<String, Object>>> param = new HashMap<>();
            param.put("values", values);

            log.info("14洞口道闸：{}", param.toString());
//            hdyHttpUtils.pushIOT(param, "bbe55ec4-fc7b-4cd1-a704-1f07964b82d6");

        }
    }


    public static String convertIso8601ToCustomFormat(String iso8601String) {
        // 定义ISO 8601格式的解析器
        DateTimeFormatter iso8601Formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        // 解析ISO 8601字符串到ZonedDateTime对象
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(iso8601String, iso8601Formatter);

        // 定义目标格式
        DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化到目标格式并返回
        return zonedDateTime.format(targetFormatter);
    }

}
