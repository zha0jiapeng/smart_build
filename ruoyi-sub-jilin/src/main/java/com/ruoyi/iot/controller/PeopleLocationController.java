package com.ruoyi.iot.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/peopleLocation")
public class PeopleLocationController {

//    @Resource
//    SwzkHttpUtils swzkHttpUtils;


    @PostMapping("/outTunnelLocation")
    public Map<String,Object> outPeopleLocation(@RequestBody Map request){
        Map<String,Object> parse = new HashMap();
        if(!request.containsKey("map_id")){
            parse.put("code",500);
            parse.put("msg","map_id必传");
            return parse;
        }
        HttpResponse execute = HttpRequest.post("http://10.1.3.207:9501/push/list")
                .body(JSON.toJSONString(request), "application/json")
                .execute();
        String body = execute.body();
        return JSONObject.parseObject(body, Map.class);
    }


    //@Scheduled(cron = "0 */1 * * * *")
    private void pushSwzkOut() {
        Map<String,Object> req = new HashMap();
        req.put("map_id","1");
        HttpResponse execute = HttpRequest.post("http://10.1.3.207:9501/push/list")
                .body(JSON.toJSONString(req), "application/json")
                .execute();
        String body = execute.body();
        Map parse = JSONObject.parseObject(body, Map.class);
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("deviceNumber", parse.get("tid").toString());
        bodyMap.put("tabWorkStatus", "在线");
        bodyMap.put("positionX", parse.get("result_x").toString());
        bodyMap.put("positionY", parse.get("result_y").toString());
        bodyMap.put("positionZ", parse.get("result_z").toString());
        bodyMap.put("power", parse.get("bat").toString());
        bodyMap.put("tabStatus", parse.get("speed").toString());
        bodyMap.put("sosStatus", "");
        bodyMap.put("positionTime",  parse.get("time").toString());
        bodyMap.put("localStation","");
        bodyMap.put("deviceCode", "jizhan1");
        bodyMap.put("InOrOut", "洞外");
        bodyMap.put("pushTime", DateUtil.now());
        bodyMap.put("other", "");

        // 创建values部分
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put("body", bodyMap);
        valuesMap.put("facturer", "人员定位基站");
        valuesMap.put("push_time",DateUtil.now());

        // 创建最外层的Map
        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put("values", new Map[] { valuesMap });



    }


}
