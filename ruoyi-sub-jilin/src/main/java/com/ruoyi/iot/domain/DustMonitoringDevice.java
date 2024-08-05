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
 * 扬尘监测仪对象 dust_monitoring_device
 *
 * @author liang
 * @date 2024-08-05
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("dust_monitoring_device")
@ApiModel(value = "DustMonitoringDevice", description = "扬尘监测仪对象 dust_monitoring_device")
public class DustMonitoringDevice implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "${column.columnComment}")
    private Long id;

    /** 门户ID */
    @ApiModelProperty(value = "门户ID")
    private String protalId;

    /** 标段ID */
    @ApiModelProperty(value = "标段ID")
    private String subProjectId;

    /** 设备编号 */
    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    /** 设备工作状态 */
    @ApiModelProperty(value = "设备工作状态")
    private String workStatus;

    /** PM2.5 */
    @ApiModelProperty(value = "PM2.5")
    private String pm25;

    /** PM10 */
    @ApiModelProperty(value = "PM10")
    private String pm10;

    /** 噪音 */
    @ApiModelProperty(value = "噪音")
    private String noise;

    /** 风速 */
    @ApiModelProperty(value = "风速")
    private String windSpeed;

    /** 风向 */
    @ApiModelProperty(value = "风向")
    private String windDirection;

    /** 温度 */
    @ApiModelProperty(value = "温度")
    private String temperature;

    /** 湿度 */
    @ApiModelProperty(value = "湿度")
    private String humidity;

    /** 气压 */
    @ApiModelProperty(value = "气压")
    private String pressure;

    /** 雨量 */
    @ApiModelProperty(value = "雨量")
    private String rainfall;

    /** 状态 */
    @ApiModelProperty(value = "状态")
    private String status;

    /** 其他 */
    @ApiModelProperty(value = "其他")
    private String other;

    /** 推送时间 */
    @ApiModelProperty(value = "推送时间")
    private String pushTime;

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