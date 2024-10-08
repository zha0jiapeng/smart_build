package com.ruoyi.iot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收料对象 q_receive
 *
 * @author ruoyi
 * @date 2024-10-08
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("q_receive")
@ApiModel(value = "QReceive", description = "收料对象 q_receive")
@DataSource(value = DataSourceType.SLAVE)
public class QReceive implements Serializable {

private static final long serialVersionUID=1L;


    /** 组织机构id */
//    @TableId(value = "org_id", type = IdType.AUTO)
    @ApiModelProperty(value = "组织机构id")
    private String orgId;

    /** 主键id */
    @TableId(value = "order_id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private String orderId;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String rdsId;

    /** 组织机构名称 */
    @ApiModelProperty(value = "组织机构名称")
    private String orgName;

    /** 过磅日期 */
    @ApiModelProperty(value = "过磅日期")
    private String recordedDate;

    /** 账期 */
    @ApiModelProperty(value = "账期")
    private String orderDate;

    /** 单号 */
    @ApiModelProperty(value = "单号")
    private String orderCode;

    /** 业务类型 */
    @ApiModelProperty(value = "业务类型")
    private Long serviceType;

    /** 操作类型 */
    @ApiModelProperty(value = "操作类型")
    private Long orderType;

    /** 过磅类型（普通进料、直进直出、一车多料） */
    @ApiModelProperty(value = "过磅类型（普通进料、直进直出、一车多料）")
    private String weightType;

    /** 物料集（一车多料用） */
    @ApiModelProperty(value = "物料集（一车多料用）")
    private String materialCollection;

    /** 过磅员 */
    @ApiModelProperty(value = "过磅员")
    private String maker;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String exitMaker;

    /** 过磅日期 */
    @ApiModelProperty(value = "过磅日期")
    private String makerDate;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;

    /** 打印次数 */
    @ApiModelProperty(value = "打印次数")
    private Long printTimes;

    /** 车牌号 */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;

    /** 供应商id */
    @ApiModelProperty(value = "供应商id")
    private String supplierId;

    /** 供应商 */
    @ApiModelProperty(value = "供应商")
    private String supplierName;

    /** 用料单位id */
    @ApiModelProperty(value = "用料单位id")
    private String labourId;

    /** 用料单位 */
    @ApiModelProperty(value = "用料单位")
    private String labourName;

    /** 工号id */
    @ApiModelProperty(value = "工号id")
    private String ghId;

    /** 工号主键链 */
    @ApiModelProperty(value = "工号主键链")
    private String ghFullId;

    /** 工号全称 */
    @ApiModelProperty(value = "工号全称")
    private String ghFullName;

    /** 工号名称 */
    @ApiModelProperty(value = "工号名称")
    private String ghName;

    /** 入场时间 */
    @ApiModelProperty(value = "入场时间")
    private String enterTime;

    /** 出场时间 */
    @ApiModelProperty(value = "出场时间")
    private String exitTime;

    /** 是否被冲红 */
    @ApiModelProperty(value = "是否被冲红")
    private Long isRed;

    /** 审核状态 */
    @ApiModelProperty(value = "审核状态")
    private Long isAudit;

    /** 审核人 */
    @ApiModelProperty(value = "审核人")
    private String auditor;

    /** 审核时间 */
    @ApiModelProperty(value = "审核时间")
    private String auditDate;

    /** 平台端组织机构 */
    @ApiModelProperty(value = "平台端组织机构")
    private String oriOrgId;

    /** 平台端供应商 */
    @ApiModelProperty(value = "平台端供应商")
    private String oriSupplierId;

    /** 平台端工号 */
    @ApiModelProperty(value = "平台端工号")
    private String oriGhId;

    /** 平台端用料单位 */
    @ApiModelProperty(value = "平台端用料单位")
    private String oriLabourId;

    /** 冲红单的原始单据id */
    @ApiModelProperty(value = "冲红单的原始单据id")
    private String oriRedId;

    /** 上传状态 */
    @ApiModelProperty(value = "上传状态")
    private Long isUpload;

    /** 识别车牌号 */
    @ApiModelProperty(value = "识别车牌号")
    private String discernPlateNumber;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String stockbinFullName;

    /** 料仓名称 */
    @ApiModelProperty(value = "料仓名称")
    private String stockbinName;

    /** 料仓id */
    @ApiModelProperty(value = "料仓id")
    private String stockbinId;

    /** 平台端料仓id */
    @ApiModelProperty(value = "平台端料仓id")
    private String oriStockbinId;

    /** 出场状态 */
    @ApiModelProperty(value = "出场状态")
    private Long isExit;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private Long isSplit;

    /** 是否称皮 */
    @ApiModelProperty(value = "是否称皮")
    private Long isTare;

    /** 毛重 */
    @ApiModelProperty(value = "毛重")
    private BigDecimal roughQuantity;

    /** 皮重 */
    @ApiModelProperty(value = "皮重")
    private BigDecimal tareQuantity;

    /** 扣率 */
    @ApiModelProperty(value = "扣率")
    private BigDecimal deductRate;

    /** 扣重 */
    @ApiModelProperty(value = "扣重")
    private BigDecimal deductQuantity;

    /** 净重 */
    @ApiModelProperty(value = "净重")
    private BigDecimal auxiliaryNetQuantity;

    /** 运单量 */
    @ApiModelProperty(value = "运单量")
    private BigDecimal oriNetQuantity;

    /** 复称（不计入核算） */
    @ApiModelProperty(value = "复称（不计入核算）")
    private Long isNotAccounting;

    /** 运单结算 */
    @ApiModelProperty(value = "运单结算")
    private Long isUseOriNetQuantity;

    /** 单号排序 */
    @ApiModelProperty(value = "单号排序")
    private String sortOrderCode;

    /** 退货状态 */
    @ApiModelProperty(value = "退货状态")
    private Long isReturn;

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

    /** 客户端版本号 */
    @ApiModelProperty(value = "客户端版本号")
    private String clientVersion;

    /** 识别方式 */
    @ApiModelProperty(value = "识别方式")
    private String discernMode;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String settlementName;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private Long settlementId;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String purchaseMethod;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String purchaseMethodFullName;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private Long isQrcode;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private Long qrCodeType;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String logistId;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String qrOrgId;

    /** 平台端工队id */
    @ApiModelProperty(value = "平台端工队id")
    private String oriTeamId;

    /** 工队名称 */
    @ApiModelProperty(value = "工队名称")
    private String teamName;

    /** 队长姓名 */
    @ApiModelProperty(value = "队长姓名")
    private String teamLeader;

    /** 主表发货时间 */
    @ApiModelProperty(value = "主表发货时间")
    private String colVarchar50No01;

    /** 运单编号 */
    @ApiModelProperty(value = "运单编号")
    private String colVarchar500No01;

    /** 补录原因(冲红原因) */
    @ApiModelProperty(value = "补录原因(冲红原因)")
    private String reason;

    /** 原始单据id */
    @ApiModelProperty(value = "原始单据id")
    private String originalOrderId;

    /** 原始单据编号 */
    @ApiModelProperty(value = "原始单据编号")
    private String originalOrderCode;

}