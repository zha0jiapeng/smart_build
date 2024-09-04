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

    public String pushIOT(Map param,String rid){
        //param.put("subProjectId", "1763492186013306882");
        log.info("push hdy url:{} , request:{}", "http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/"+rid ,JSON.toJSONString(param, SerializerFeature.WriteMapNullValue));
        HttpResponse execute = HttpRequest.put("http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/"+rid)
                .body(JSON.toJSONString(param, SerializerFeature.WriteMapNullValue), "application/json").execute();
        log.info("push hdy url:{} , response:{}", "http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/"+rid ,execute.body());
        return execute.body();
    }
}
