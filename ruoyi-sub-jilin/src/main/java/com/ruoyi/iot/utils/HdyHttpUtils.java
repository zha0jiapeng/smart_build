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

    public String pushIOT(Map param, String rid) {
        //param.put("subProjectId", "1763492186013306882");
        log.info("push hdy url:{} , request:{}", "http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/" + rid, JSON.toJSONString(param, SerializerFeature.WriteMapNullValue));
        HttpResponse execute = HttpRequest.put("http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/" + rid)
                .body(JSON.toJSONString(param, SerializerFeature.WriteMapNullValue), "application/json").execute();
        log.info("push hdy url:{} , response:{}", "http://10.0.100.23:18080/sdata/rest/dataservice/rest/standard/" + rid, execute.body());
        return execute.body();
    }

    public String pushPicture(String url) {
        String getUrl = "http://10.0.100.9:8001/api/smart-working/image/uploadUrl?url=" + url;
        log.info("Calling GET url: {}", getUrl);
        HttpResponse getResponse = HttpRequest.get(getUrl).execute();
        log.info("GET response: {}", getResponse.body());
        String data = getResponse.body();
        data = JSON.parseObject(data).get("data").toString();
        if (data.equals("-1")) {
            System.out.println("上传图片失败，返回信息：" + getResponse.body());
            return url;
        }
        return data;
    }
}
