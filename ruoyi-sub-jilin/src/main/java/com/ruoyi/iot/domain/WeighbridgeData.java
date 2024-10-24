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
 * 地磅数据处理对象 weighbridge_data
 *
 * @author ruoyi
 * @date 2024-10-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("weighbridge_data")
@ApiModel(value = "WeighbridgeData", description = "地磅数据处理对象 weighbridge_data")
public class WeighbridgeData implements Serializable {

private static final long serialVersionUID=1L;


    /**  */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Long id;

    /** 设备编号 */
    @ApiModelProperty(value = "设备编号")
    private String deviceCode;

    /** 工作状态 */
    @ApiModelProperty(value = "工作状态")
    private String workStatus;

    /** 车牌号码 */
    @ApiModelProperty(value = "车牌号码")
    private String licenseNumber;

    /** 一次过磅照片 */
    @ApiModelProperty(value = "一次过磅照片")
    private String carPicture;

    /** 二次过磅照片 */
    @ApiModelProperty(value = "二次过磅照片")
    private String boxPicture;

    /** 一次过磅时间 */
    @ApiModelProperty(value = "一次过磅时间")
    private String oneWeightTime;

    /** 二次过磅时间 */
    @ApiModelProperty(value = "二次过磅时间")
    private String twoWeightTime;

    /** 实重 */
    @ApiModelProperty(value = "实重")
    private String actualWeight;

    /** 扣重 */
    @ApiModelProperty(value = "扣重")
    private String deductWeight;

    /** 净重 */
    @ApiModelProperty(value = "净重")
    private String suttleWeight;

    /** 毛重 */
    @ApiModelProperty(value = "毛重")
    private String roughWeight;

    /** 皮重 */
    @ApiModelProperty(value = "皮重")
    private String tare;

    /** 进磅时间 */
    @ApiModelProperty(value = "进磅时间")
    private String inWeightTime;

    /** 出磅时间 */
    @ApiModelProperty(value = "出磅时间")
    private String outWeightTime;

    /** 货单号 */
    @ApiModelProperty(value = "货单号")
    private String orderCode;

    /** 货单量 */
    @ApiModelProperty(value = "货单量")
    private String orderNumber;

    /** 偏差量 */
    @ApiModelProperty(value = "偏差量")
    private String deviationNumber;

    /** 上偏差量 */
    @ApiModelProperty(value = "上偏差量")
    private String deviationNumberUp;

    /** 下偏差量 */
    @ApiModelProperty(value = "下偏差量")
    private String deviationNumberDown;

    /** 其他 */
    @ApiModelProperty(value = "其他")
    private String other;

    /** 过磅时间 */
    @ApiModelProperty(value = "过磅时间")
    private String weightTime;

    /** 过磅照片 */
    @ApiModelProperty(value = "过磅照片")
    private String weightPicture;

    /** 预警类型 */
    @ApiModelProperty(value = "预警类型")
    private String waringType;

    /** 数据类型 */
    @ApiModelProperty(value = "数据类型")
    private String dateType;

    /** 司机姓名 */
    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    /** 联系方式 */
    @ApiModelProperty(value = "联系方式")
    private String driverPhone;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private String createdBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    private Date createdDate;

    /** 更新人 */
    @ApiModelProperty(value = "更新人")
    private String modifyBy;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    private Date modifyDate;

    /** 逻辑删除标识 0删除 1正常 */
    @ApiModelProperty(value = "逻辑删除标识 0删除 1正常")
    private Long yn;

    /** 目标数据库唯一标识 */
    @ApiModelProperty(value = "目标数据库唯一标识")
    private String orderId;

    /** 目标数据库区域 */
    @ApiModelProperty(value = "目标数据库区域")
    private String region;

}