package com.ruoyi.iot.bean;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;

import java.util.HashMap;
import java.util.Map;

public class DoorFunctionApi {
    /**
     * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
     */
    static {
        ArtemisConfig.host = "10.1.3.2:443";
        ArtemisConfig.appKey = "29632148"; // 秘钥appkey
        ArtemisConfig.appSecret = "7k0RVVHqdynytJBhPfz8";
    }
    /**
     * STEP2：设置OpenAPI接口的上下文
     */
    private static final String ARTEMIS_PATH = "/artemis";
    // 查询监控点列表v2
    public  String previewURLs(Map<String,Object> request ){
        String doControlDataApi = ARTEMIS_PATH +"/api/video/v2/cameras/previewURLs";
        Map<String,String> path = new HashMap<String,String>(2){
            {
                put("https://",doControlDataApi);
            }
        };
        String body= JSON.toJSONString(request);
        String result = ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json");
        return result;
    }


    //查询门禁点事件v2
    public  String events(EventsRequest eventsRequest ){
        String eventsDataApi = ARTEMIS_PATH +"/api/acs/v2/door/events";
        Map<String,String> path = new HashMap<String,String>(2){
            {
                put("https://",eventsDataApi);
            }
        };
        String body=JSON.toJSONString(eventsRequest);
        String result =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json");
        return result;
    }

    //获取门禁事件的图片
    public  String pictures(PicturesRequest picturesRequest ){
        String picturesDataApi = ARTEMIS_PATH +"/api/acs/v1/event/pictures";
        Map<String,String> path = new HashMap<String,String>(2){
            {
                put("https://",picturesDataApi);
            }
        };
        String body=JSON.toJSONString(picturesRequest);
        String result =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json");
        return result;
    }

    public String states(StatesRequest statesRequest ){
        String statesDataApi = ARTEMIS_PATH +"/api/acs/v1/door/states";
        Map<String,String> path = new HashMap<String,String>(2){
            {
                put("https://",statesDataApi);
            }
        };
        String body=JSON.toJSONString(statesRequest);
        String result =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json");
        return result;
    }

    public JSONObject search(Map<String,Object> request){
        String eventsDataApi = ARTEMIS_PATH +"/api/resource/v2/acsDevice/search";
        Map<String,String> path = new HashMap<String,String>(2){
            {
                put("https://",eventsDataApi);
            }
        };
        String body=JSON.toJSONString(request);
        String result =ArtemisHttpUtil.doPostStringArtemis(path,body,null,null,"application/json");
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }
}
