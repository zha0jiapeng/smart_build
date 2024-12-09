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
 * IP广播对象 ip_broadcast
 *
 * @author liang
 * @date 2024-12-06
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("ip_broadcast")
@ApiModel(value = "IpBroadcast", description = "IP广播对象 ip_broadcast")
public class IpBroadcast implements Serializable {

    private static final long serialVersionUID=1L;


    /**  */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Long id;

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

    /** 任务名称 */
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /** 任务类型（定时任务/及时任务） */
    @ApiModelProperty(value = "任务类型（定时任务/及时任务）")
    private String taskType;

    /** 任务编号 */
    @ApiModelProperty(value = "任务编号")
    private String taskNo;

    /** 任务执行时间 */
    @ApiModelProperty(value = "任务执行时间")
    private String taskExecuteTime;

    /** 任务设备 */
    @ApiModelProperty(value = "任务设备")
    private String taskEquipment;

    /** 设备编号 */
    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;

    /** 音频类型1文件音频 /2文字音频 */
    @ApiModelProperty(value = "音频类型1文件音频 /2文字音频")
    private String audioType;

    /** 音频编号 */
    @ApiModelProperty(value = "音频编号")
    private String audioNo;

    /** 播放次数 */
    @ApiModelProperty(value = "播放次数")
    private String playTimes;

}