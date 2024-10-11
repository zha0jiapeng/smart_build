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
 * 收料材料明细对象 q_receive_more_material
 *
 * @author ruoyi
 * @date 2024-10-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("q_receive_more_material")
@ApiModel(value = "QReceiveMoreMaterial", description = "收料材料明细对象 q_receive_more_material")
public class QReceiveMoreMaterial implements Serializable {

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

    /** 材料id */
    @ApiModelProperty(value = "材料id")
    private String materialId;

    /** 材料编码 */
    @ApiModelProperty(value = "材料编码")
    private String materialCode;

    /** 材料名称 */
    @ApiModelProperty(value = "材料名称")
    private String materialName;

    /** 规格型号 */
    @ApiModelProperty(value = "规格型号")
    private String materialModel;

    /** 材料单位 */
    @ApiModelProperty(value = "材料单位")
    private String materialUnit;

    /** 材料类别id */
    @ApiModelProperty(value = "材料类别id")
    private String classId;

    /** 材料类别全id */
    @ApiModelProperty(value = "材料类别全id")
    private String classFullId;

    /** 确认量 */
    @ApiModelProperty(value = "确认量")
    private BigDecimal netQuantity;

    /** 转换率 */
    @ApiModelProperty(value = "转换率")
    private BigDecimal conversionRate;

    /** 运单量 */
    @ApiModelProperty(value = "运单量")
    private BigDecimal oriNetQuantity;

    /** 实际量 */
    @ApiModelProperty(value = "实际量")
    private BigDecimal mainNetQuantity;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private BigDecimal itemAuxiliaryNetQuantity;

    /** 备注 */
    @ApiModelProperty(value = "备注")
    private String remark;

    /** 平台端材料id */
    @ApiModelProperty(value = "平台端材料id")
    private String oriMaterialId;

    /** 平台端材料类别id */
    @ApiModelProperty(value = "平台端材料类别id")
    private String oriClassId;

    /** 条码 */
    @ApiModelProperty(value = "条码")
    private String itemBarCode;

    /** 平台端组织机构 */
    @ApiModelProperty(value = "平台端组织机构")
    private String oriOrgId;

    /** 冲红来源明细id */
    @ApiModelProperty(value = "冲红来源明细id")
    private String oriItemRedId;

    /** 被冲红状态 */
    @ApiModelProperty(value = "被冲红状态")
    private Long isRed;

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

    /** 料仓id */
    @ApiModelProperty(value = "料仓id")
    private String stockbinId;

    /** 料仓名称 */
    @ApiModelProperty(value = "料仓名称")
    private String stockbinName;

    /** 料仓全称 */
    @ApiModelProperty(value = "料仓全称")
    private String stockbinFullName;

    /** 平台端料仓id */
    @ApiModelProperty(value = "平台端料仓id")
    private String oriStockbinId;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private BigDecimal deductQuantity;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private BigDecimal labDeductQuantity;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private BigDecimal labDeductRate;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String manufacturer;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String batchNo;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String testReportNo;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String qualityCertificate;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String acceptanceRecord;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String purchaseMethod;

    /** $column.columnComment */
    @ApiModelProperty(value = "${column.columnComment}")
    private String purchaseMethodFullName;

    /** 发货时间 （云南交投定制） */
    @ApiModelProperty(value = "发货时间 （云南交投定制）")
    @TableField("col_varchar_50_no_01")
    private String colVarchar50No01;

    /** 偏差 */
    @ApiModelProperty(value = "偏差")
    @TableField("col_decimal_8_no_01")
    private BigDecimal colDecimal8No01;

    /** 偏差结果 */
    @ApiModelProperty(value = "偏差结果")
    @TableField("col_varchar_50_no_02")
    private String colVarchar50No02;

    /** 偏差单位 */
    @ApiModelProperty(value = "偏差单位")
    @TableField("col_varchar_50_no_03")
    private String colVarchar50No03;

}