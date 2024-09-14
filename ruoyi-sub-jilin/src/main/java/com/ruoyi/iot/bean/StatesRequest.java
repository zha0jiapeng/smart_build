package com.ruoyi.iot.bean;

import java.util.ArrayList;

public class StatesRequest {
    /*
   门禁索引
    */
    private ArrayList<String> doorIndexCodes;

    public ArrayList<String> getDoorIndexCodes() {
        return doorIndexCodes;
    }

    public void setDoorIndexCodes(ArrayList<String> doorIndexCodes) {
        this.doorIndexCodes = doorIndexCodes;
    }
}
