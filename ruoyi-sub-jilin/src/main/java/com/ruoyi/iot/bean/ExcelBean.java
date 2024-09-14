package com.ruoyi.iot.bean;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelBean {
    @ExcelProperty("序号")
    private String id;
    @ExcelProperty("门户ID")
    private String portal_id;

    @ExcelIgnore
    private String push_time;
    @ExcelIgnore
    private String face_image;


    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("年龄")
    private String age;
    @ExcelProperty("性别")
    private String sex;
    @ExcelProperty("民族")
    private String nation;
    @ExcelProperty("联系方式")
    private String phone;
    @ExcelProperty("身份证号码")
    private String id_card;
    @ExcelProperty("身份证有效期")
    private String id_card_period;
    @ExcelProperty("是否党员")
    private String party_member_flag;
    @ExcelProperty("子工程")
    private String sub_project_id;
    @ExcelProperty("所属单位")
    private String org_no;
    @ExcelProperty("人员类别")
    private String work_type;
    @ExcelProperty("部门")
    private String class_name;
    @ExcelProperty("班组")
    private String team_group;
    @ExcelProperty("职务/工种")
    private String duty_name;
    @ExcelProperty("进场时间")
    private String entry_time;
    @ExcelProperty("离场时间")
    private String exit_time;
    @ExcelProperty("是否特种人员")
    private String special_flag;
    @ExcelIgnore
    private String tm_leader;


}
