package com.ruoyi.iot.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 用电监测对象 electricity_monitoring
 *
 * @author ruoyi
 * @date 2024-09-23
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("electricity_monitoring")
@ApiModel(value = "ElectricityMonitoring", description = "用电监测对象 electricity_monitoring")
public class ElectricityMonitoring implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "${column.columnComment}")
    private Long id;

    /** 原始数据 */
    @ApiModelProperty(value = "原始数据")
    private String rawData;

    /** 正向无功总电能 */
    @ApiModelProperty(value = "正向无功总电能")
    private String totalPRE;

    /** 反向无功总电能 */
    @ApiModelProperty(value = "反向无功总电能")
    private String totalPRRE;

    /** 正向有功总电量 */
    @ApiModelProperty(value = "正向有功总电量")
    private String totalPAE;

    /** 反向有功总电量 */
    @ApiModelProperty(value = "反向有功总电量")
    private String totalPRAE;

    /** ABC三项电压 */
    @ApiModelProperty(value = "ABC三项电压")
    private String aBCVoltage;

    /** ABC三项电流 */
    @ApiModelProperty(value = "ABC三项电流")
    private String aBCCurrent;

    /** A相电流 */
    @ApiModelProperty(value = "A相电流")
    private String aElectricity;

    /** B相电流 */
    @ApiModelProperty(value = "B相电流")
    private String bBlectricity;

    /** C相电流 */
    @ApiModelProperty(value = "C相电流")
    private String cElectricity;

    /** A相电压 */
    @ApiModelProperty(value = "A相电压")
    private String aVoltage;

    /** B相电压 */
    @ApiModelProperty(value = "B相电压")
    private String bVoltage;

    /** C相电压 */
    @ApiModelProperty(value = "C相电压")
    private String cVoltage;

    /** 用电负载功率 */
    @ApiModelProperty(value = "用电负载功率")
    private String boxPower;

    /** 线缆A温度 */
    @ApiModelProperty(value = "线缆A温度")
    private String lineATemperature;

    /** 线缆B温度 */
    @ApiModelProperty(value = "线缆B温度")
    private String lineBTemperature;

    /** 线缆C温度 */
    @ApiModelProperty(value = "线缆C温度")
    private String lineCTemperature;

    /** 线缆N温度 */
    @ApiModelProperty(value = "线缆N温度")
    private String lineNTemperature;

    /** 推送时间 */
    @ApiModelProperty(value = "推送时间")
    private String pushTime;

    /** 总用电量 */
    @ApiModelProperty(value = "总用电量")
    private String totalWattage;

    /** 删除标志（0代表存在 1代表删除） */
    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    private String delFlag;

    /** 创建者 */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /** 更新者 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;

}