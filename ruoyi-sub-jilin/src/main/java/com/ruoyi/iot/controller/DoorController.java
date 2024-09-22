package com.ruoyi.iot.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.bean.DoorFunctionApi;
import com.ruoyi.iot.bean.EventsRequest;
import com.ruoyi.iot.scheduling.DoorEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设备Controller
 * 
 * @author mashir0
 * @date 2024-06-23
 */
@RestController
@RequestMapping("/door")
public class DoorController extends BaseController
{

    @Resource
    private RedisTemplate redisTemplate;

}
