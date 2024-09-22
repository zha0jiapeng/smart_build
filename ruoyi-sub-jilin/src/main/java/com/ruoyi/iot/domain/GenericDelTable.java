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
 * 删除表对象 generic_del_table
 *
 * @author ruoyi
 * @date 2024-09-22
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("generic_del_table")
@ApiModel(value = "GenericDelTable", description = "删除表对象 generic_del_table")
public class GenericDelTable implements Serializable {

private static final long serialVersionUID=1L;


    /** $column.columnComment */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "${column.columnComment}")
    private Long id;

    /** 表名 */
    @ApiModelProperty(value = "表名")
    private String tableName;

    /** 要保留的月份 */
    @ApiModelProperty(value = "要保留的月份")
    private String fieldName;

    /** 字段名 */
    @ApiModelProperty(value = "字段名")
    private Integer intervalMonths;

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

}