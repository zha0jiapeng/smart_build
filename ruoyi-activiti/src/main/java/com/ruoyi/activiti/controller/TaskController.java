package com.ruoyi.activiti.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.domain.dto.ActTaskDTO;
import com.ruoyi.activiti.domain.dto.ActWorkflowFormDataDTO;
import com.ruoyi.activiti.service.IActTaskService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import org.activiti.api.task.runtime.TaskRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private IActTaskService actTaskService;



    //获取我的代办任务
    @GetMapping(value = "/list")
    public TableDataInfo getTasks() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Page<ActTaskDTO> hashMaps = actTaskService.selectProcessDefinitionList(pageDomain);
         return getDataTable(hashMaps);


    }

    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    //渲染表单
    @GetMapping(value = "/formDataShow/{taskID}")
    public AjaxResult formDataShow(@PathVariable("taskID") String taskID) {

        return AjaxResult.success(actTaskService.formDataShow(taskID));
    }

    //保存表单
//    @PostMapping(value = "/formDataSave/{taskID}")
//    public AjaxResult formDataSave(@PathVariable("taskID") String taskID,
//                                   @RequestBody   List<ActWorkflowFormDataDTO> formData ) throws ParseException {
//        return toAjax(actTaskService.formDataSave(taskID, formData));
//
//    }

}
