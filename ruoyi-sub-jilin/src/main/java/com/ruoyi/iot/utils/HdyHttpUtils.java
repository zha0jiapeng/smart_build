package com.ruoyi.iot.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class HdyHttpUtils {

    public String pushIOT(Map param){
        param.put("portalId", "1751847977770553345");
        param.put("subProjectId", "1763492186013306882");
        HttpResponse execute = HttpRequest.put("https://iot.ecidihdjg.com:18443/sdata/rest/dataservice/rest/standard/0828707d-f9ea-41fe-a2ba-fa7ac8741a77")
                .body(JSON.toJSONString(param), "application/json").execute();
        return execute.body();
    }
}
