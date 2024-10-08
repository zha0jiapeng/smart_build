package com.ruoyi.assessment.modules.repo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 题库请求类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-25 13:23
*/
@Data
@ApiModel(value="题库", description="题库")
public class RepoDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "题库ID", required=true)
    private String id;

    @ApiModelProperty(value = "题库编号", required=true)
    private String code;

    @ApiModelProperty(value = "题库名称", required=true)
    private String title;

    @ApiModelProperty(value = "题库备注", required=true)
    private String remark;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", required=true)
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", required=true)
    private Date updateTime;
    
}
