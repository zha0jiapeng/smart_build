package com.ruoyi.iot.bean;

/**
 *获取门禁图片
 */

public class PicturesRequest {
    /**
     * 提供picUri处会提供此字段
     */
    private String svrIndexCode;
    /**
     * 图片相对地址
     */
    private String picUri;

    public String getSvrIndexCode() {
        return svrIndexCode;
    }

    public void setSvrIndexCode(String svrIndexCode) {
        this.svrIndexCode = svrIndexCode;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }
}

