package com.ruoyi.iot.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.scheduling.DeviceIpChecker;
import com.ruoyi.iot.tcp.TcpClient;
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
@RequestMapping("/deviceIpChecker")
public class DeviceIpCheckerController extends BaseController {

    /**
     * 查询14支洞ip电话设备状态
     */
    @GetMapping("/getData/14")
    public AjaxResult getData() {
        // 发送请求
        boolean value = new DeviceIpChecker().ping("10.1.3.120");
        String state = "离线";
        if (value) {
            state = "在线";
        }
        return AjaxResult.success(state);
    }

    /**
     * 查询15支洞ip电话设备状态
     */
    @GetMapping("/getData/15")
    public AjaxResult get15Data() {
        // 发送请求
        boolean value = new DeviceIpChecker().ping("10.1.3.121");
        String state = "离线";
        if (value) {
            state = "在线";
        }
        return AjaxResult.success(state);
    }
}
