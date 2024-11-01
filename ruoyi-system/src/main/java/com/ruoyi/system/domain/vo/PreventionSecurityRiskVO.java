package com.ruoyi.system.domain.vo;

import com.ruoyi.common.annotation.Excel;

public class PreventionSecurityRiskVO {

    /**
     * 装置名称
     */
    @Excel(name = "装置名称")
    private String deviceName;
    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    private String deptName;
    /**
     * 用户名称
     */
    @Excel(name = "用户名称")
    private String userName;
    /**
     * 分析单元
     */
    @Excel(name = "分析单元")
    private String analysisUnit;
    /**
     * 安全风险事件
     */
    @Excel(name = "安全风险事件")
    private String dangerEvent;
    /**
     * 措施分类1
     */
    @Excel(name = "措施分类1")
    private String measuresOne;
    /**
     * 措施分类2
     */
    @Excel(name = "措施分类2")
    private String measuresTwo;
    /**
     * 措施分类3
     */
    @Excel(name = "措施分类3")
    private String measuresThree;
    /**
     * 管控措施
     */
    @Excel(name = "管控措施")
    private String controlMeasures;
    /**
     * 隐患排查内容
     */
    @Excel(name = "隐患排查内容")
    private String hiddenContent;
    /**
     * 岗位负责人
     */
    @Excel(name = "岗位负责人")
    private String deptUserName;
    /**
     * 周期
     */
    @Excel(name = "周期")
    private String cycle;
    /**
     * 单位
     */
    @Excel(name = "单位")
    private String unit;
    /**
     * 隐患问题描述
     */
    @Excel(name = "隐患问题描述")
    private String hiddenInfo;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remarks;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAnalysisUnit() {
        return analysisUnit;
    }

    public void setAnalysisUnit(String analysisUnit) {
        this.analysisUnit = analysisUnit;
    }

    public String getDangerEvent() {
        return dangerEvent;
    }

    public void setDangerEvent(String dangerEvent) {
        this.dangerEvent = dangerEvent;
    }

    public String getMeasuresOne() {
        return measuresOne;
    }

    public void setMeasuresOne(String measuresOne) {
        this.measuresOne = measuresOne;
    }

    public String getMeasuresTwo() {
        return measuresTwo;
    }

    public void setMeasuresTwo(String measuresTwo) {
        this.measuresTwo = measuresTwo;
    }

    public String getMeasuresThree() {
        return measuresThree;
    }

    public void setMeasuresThree(String measuresThree) {
        this.measuresThree = measuresThree;
    }

    public String getControlMeasures() {
        return controlMeasures;
    }

    public void setControlMeasures(String controlMeasures) {
        this.controlMeasures = controlMeasures;
    }

    public String getHiddenContent() {
        return hiddenContent;
    }

    public void setHiddenContent(String hiddenContent) {
        this.hiddenContent = hiddenContent;
    }

    public String getDeptUserName() {
        return deptUserName;
    }

    public void setDeptUserName(String deptUserName) {
        this.deptUserName = deptUserName;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getHiddenInfo() {
        return hiddenInfo;
    }

    public void setHiddenInfo(String hiddenInfo) {
        this.hiddenInfo = hiddenInfo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
