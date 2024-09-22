package com.ruoyi.iot.controller;

import cn.hutool.json.JSONUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.bean.DoorFunctionApi;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.service.IDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/camera")
public class CameraController {

    @GetMapping("/previewUrl")
    public AjaxResult previewURLs(String sn) {
        DoorFunctionApi doorFunctionApi = new DoorFunctionApi();
        Map<String, Object> cameraData = new HashMap<>();
        cameraData.put("cameraIndexCode", sn);
        cameraData.put("streamType", 0);
        cameraData.put("protocol", "hls");
        cameraData.put("transmode", 1);
        cameraData.put("expand", "transcode=0");
        cameraData.put("streamform", "rtp");
        String body = doorFunctionApi.previewURLs(cameraData);
        return AjaxResult.success(JSONUtil.getByPath(JSONUtil.parseObj(body),"data.url"));
    }

}
