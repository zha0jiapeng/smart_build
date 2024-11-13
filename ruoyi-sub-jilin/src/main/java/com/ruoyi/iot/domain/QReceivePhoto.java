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
 * 收料照片对象 q_receive_photo
 *
 * @author ruoyi
 * @date 2024-11-13
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("q_receive_photo")
@ApiModel(value = "QReceivePhoto", description = "收料照片对象 q_receive_photo")
public class QReceivePhoto implements Serializable {

private static final long serialVersionUID=1L;


    /** 组织机构id */
//    @TableId(value = "org_id", type = IdType.AUTO)
    @ApiModelProperty(value = "组织机构id")
    private String orgId;

    /** 单据id */
//    @TableId(value = "order_id", type = IdType.AUTO)
    @ApiModelProperty(value = "单据id")
    private String orderId;

    /** 主键id */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private String id;

    /** 摄像机位置 */
    @ApiModelProperty(value = "摄像机位置")
    private String cameraPosition;

    /** 进场或出场 */
    @ApiModelProperty(value = "进场或出场")
    private String photoType;

    /** 摄像机编码 */
    @ApiModelProperty(value = "摄像机编码")
    private String cameraCode;

    /** 上传到平台端的路径 */
    @ApiModelProperty(value = "上传到平台端的路径")
    private String photoUrl;

    /** 本地文件路径 */
    @ApiModelProperty(value = "本地文件路径")
    private String localUrl;

    /** 上传状态 */
    @ApiModelProperty(value = "上传状态")
    private Long isUpload;

    /** 创建人id */
    @ApiModelProperty(value = "创建人id")
    private String creatorId;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    private String creatorName;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    /** 更新人id */
    @ApiModelProperty(value = "更新人id")
    private String modifierId;

    /** 更新人 */
    @ApiModelProperty(value = "更新人")
    private String modifierName;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    /** 是否移除 */
    @ApiModelProperty(value = "是否移除")
    private Long isRemoved;

    /** 版本号 */
    @Version
    @ApiModelProperty(value = "版本号")
    private Long version;

}