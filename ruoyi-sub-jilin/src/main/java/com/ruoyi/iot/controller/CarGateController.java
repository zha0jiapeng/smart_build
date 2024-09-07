package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/car")
public class CarGateController {



    @PostMapping("/carAccess")
    public Map<String,Object> carAccess(@RequestBody Map<String,Object> request) {
        log.info("carAccess:{}",JSON.toJSONString(request));
        return request;
    }

    private void pushCarAccess(Map<String, Object> request) {

    }


}
