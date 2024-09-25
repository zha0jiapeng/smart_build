package com.ruoyi.iot.controller;


import com.ruoyi.iot.domain.GenericDelTable;
import com.ruoyi.iot.service.IGenericDelTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 删除表Controller
 *
 * @author ruoyi
 * @date 2024-09-22
 */
@RestController
@RequestMapping("/system/table")
@Api(tags = {"删除表 Controller"})
public class GenericDelTableController extends BaseController {
    @Autowired
    private IGenericDelTableService genericDelTableService;

    /**
     * 查询删除表列表
     */
//    @PreAuthorize("@ss.hasPermi('system:table:list')")
    @GetMapping("/list")
    @ApiOperation("查询删除表列表")
    public TableDataInfo list(GenericDelTable genericDelTable) {
        startPage();
        List<GenericDelTable> list = genericDelTableService.selectGenericDelTableList(genericDelTable);
        return getDataTable(list);
    }

    /**
     * 导出删除表列表
     */
//    @PreAuthorize("@ss.hasPermi('system:table:export')")
    @Log(title = "删除表", businessType = BusinessType.EXPORT)
    @ApiOperation("导出删除表列表Excel")
    @PostMapping("/export")
    public void export(HttpServletResponse response, GenericDelTable genericDelTable) {
        List<GenericDelTable> list = genericDelTableService.selectGenericDelTableList(genericDelTable);
        ExcelUtil<GenericDelTable> util = new ExcelUtil<GenericDelTable>(GenericDelTable.class);
        util.exportExcel(response, list, "删除表数据");
    }

    /**
     * 获取删除表详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:table:query')")
    @ApiOperation("获取删除表详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(name = "id", value = "删除表参数", required = true)
                              @PathVariable("id") Long id) {
        return success(genericDelTableService.selectGenericDelTableById(id));
    }

    /**
     * 新增删除表
     */
//    @PreAuthorize("@ss.hasPermi('system:table:add')")
    @Log(title = "删除表", businessType = BusinessType.INSERT)
    @ApiOperation("新增删除表")
    @PostMapping
    public AjaxResult add(@RequestBody GenericDelTable genericDelTable) {
        return toAjax(genericDelTableService.insertGenericDelTable(genericDelTable));
    }

    /**
     * 修改删除表
     */
//    @PreAuthorize("@ss.hasPermi('system:table:edit')")
    @Log(title = "删除表", businessType = BusinessType.UPDATE)
    @ApiOperation("修改删除表")
    @PutMapping
    public AjaxResult edit(@RequestBody GenericDelTable genericDelTable) {
        return toAjax(genericDelTableService.updateGenericDelTable(genericDelTable));
    }

    /**
     * 删除删除表
     */
//    @PreAuthorize("@ss.hasPermi('system:table:remove')")
    @Log(title = "删除表", businessType = BusinessType.DELETE)
    @ApiOperation("删除删除表")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(name = "ids", value = "删除表ids参数", required = true)
                             @PathVariable Long[] ids) {
        return toAjax(genericDelTableService.deleteGenericDelTableByIds(ids));
    }


    @GetMapping("/del")
    public void del() {
        List<GenericDelTable> list = genericDelTableService.list();
        for (GenericDelTable value : list) {
            if (value.getIntervalMonths() == null) {
                genericDelTableService.deleteOldRecords(value.getTableName(), value.getFieldName(), 1);
            } else {
                genericDelTableService.deleteOldRecords(value.getTableName(), value.getFieldName(), value.getIntervalMonths());
            }

        }

    }


}
