package com.ruoyi.quartz.task;

import cn.hutool.http.HttpUtil;
import com.ruoyi.iot.scheduling.DoorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("syncTask")
public class SyncTask {

    @Autowired
    private DoorEvent event;


    /**
     * 调用扬尘监测仪（每2分钟一次）
     */
    public void callMonitoring() {
        //CO2/PM通信协议
        String result1 = HttpUtil.get("http://127.0.0.1:8097/TCP/server4322");
        //雨量传感器通信协议
        String result2 = HttpUtil.get("http://127.0.0.1:8097/TCP/server4321");
    }

    /**
     * 气体检测推送（每分钟一次）
     */
    public void sendGasDetection(){
        String result1 = HttpUtil.get("http://127.0.0.1:8097/gasDetection/pushSwzk");
    }

    /**
     * 门禁信息获取和推送（每十分钟一次）
     */
    public void sendDoor(){
        event.execute();
    }


}
