package com.ruoyi.quartz.task;

import cn.hutool.http.HttpUtil;
import com.ruoyi.iot.scheduling.DeviceIpChecker;
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

        //15#支洞扬尘
        String result3 = HttpUtil.get("http://127.0.0.1:8097/TCP/server4323");
        //15#支洞雨量
        String result4 = HttpUtil.get("http://127.0.0.1:8097/TCP/server4324");
    }

    /**
     * 气体检测推送（每分钟一次）
     */
    public void sendGasDetection(){
        String result1 = HttpUtil.get("http://127.0.0.1:8097/gasDetection/pushSwzk");
    }

    /**
     * 道闸信息获取和推送（每十分钟一次）
     */
    public void sendDoor(){
        event.execute();
        String result1 = HttpUtil.get("http://127.0.0.1:8097/car/crossRecords/push");
    }

    /**
     * 地磅信息获取和推送（每五分钟一次）
     */
    public void sendWeighbridgeData(){
        String result = HttpUtil.get("http://127.0.0.1:8097/system/data/upload");
    }

    /**
     * 车辆定位（五分钟一次）
     */
    public void sendPushHDY(){
        String result1 = HttpUtil.get("http://127.0.0.1:8097/carLocation/pushHdy");
    }


}
