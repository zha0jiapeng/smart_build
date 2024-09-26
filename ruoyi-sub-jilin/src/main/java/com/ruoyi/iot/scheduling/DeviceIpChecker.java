package com.ruoyi.iot.scheduling;


import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DeviceIpChecker {

    private static final Logger logger = LoggerFactory.getLogger(DeviceIpChecker.class);
    private static final List<String> IP_PHONE = new ArrayList<>();

    // 超时时间设置为 3000 毫秒（3 秒）
    private static final int TIMEOUT = 3000;
    // 默认重试次数为 1
    private static final int RETRY_COUNT = 1;
    // 默认重试延迟为 1000 毫秒
    private static final int RETRY_DELAY = 1000;

    static {
        IP_PHONE.add("");
    }



    /**
     * 检查设备 IP 是否存在
     */
    public void ping() {
        for (String ip : IP_PHONE) {
            boolean reachable = isReachableWithRetry(ip);
            logger.info("IP: {} is reachable: {}", ip, reachable);
        }
    }

    /**
     * 检测 IP 是否可达，并进行二次验证
     *
     * @param ipAddress IP 地址
     * @return IP 是否可达
     */
    private boolean isReachableWithRetry(String ipAddress) {
        for (int attempt = 0; attempt <= RETRY_COUNT; attempt++) {
            if (isReachable(ipAddress)) {
                return true;
            }

            // 如果不是第一次尝试且未成功，等待一段时间后重试
            if (attempt < RETRY_COUNT) {
                try {
                    Thread.sleep(RETRY_DELAY);
                } catch (InterruptedException e) {
                    logger.error("Retry delay interrupted", e);
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }

    /**
     * 检测 IP 是否可达
     *
     * @param ipAddress IP 地址
     * @return IP 是否可达
     */
    private boolean isReachable(String ipAddress) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            return address.isReachable(TIMEOUT);
        } catch (Exception e) {
            logger.error("Error pinging IP: {}", ipAddress, e);
            return false;
        }
    }

}