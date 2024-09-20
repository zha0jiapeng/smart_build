package com.ruoyi.iot.scheduling;


import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Component
public class DeviceIpChecker {

    private static final List<String> IP_PHONE = new ArrayList<>();

    static {
        IP_PHONE.add("");
    }


    /**
     * 检查设备ip是否存在
     *
     * @return
     */
    public void ping() {
        for (String ip: IP_PHONE) {
            String ipAddress = ip; // 可以替换为具体的 IP 地址或域名
            int timeout = 3000; // 超时时间设置为 3000 毫秒（3 秒）
            boolean reachable = false;
            try {
                InetAddress address = InetAddress.getByName(ipAddress);
                reachable = address.isReachable(timeout);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
}