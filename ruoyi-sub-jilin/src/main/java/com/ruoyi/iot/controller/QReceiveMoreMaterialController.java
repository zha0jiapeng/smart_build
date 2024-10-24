package com.ruoyi.iot.controller;


import com.ruoyi.iot.domain.QReceiveMoreMaterial;
import com.ruoyi.iot.service.IQReceiveMoreMaterialService;
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
 * 收料材料明细Controller
 * 
 * @author ruoyi
 * @date 2024-10-09
 */
@RestController
@RequestMapping("/system/material")
@Api(tags={"收料材料明细 Controller"})
public class QReceiveMoreMaterialController extends BaseController
{
    @Autowired
    private IQReceiveMoreMaterialService qReceiveMoreMaterialService;

    /**
     * 查询收料材料明细列表
     */
//    @PreAuthorize("@ss.hasPermi('system:material:list')")
    @GetMapping("/list")
    @ApiOperation("查询收料材料明细列表")
    public TableDataInfo list(QReceiveMoreMaterial qReceiveMoreMaterial)
    {
        startPage();
        List<QReceiveMoreMaterial> list = qReceiveMoreMaterialService.selectQReceiveMoreMaterialList(qReceiveMoreMaterial);
        return getDataTable(list);
    }

    /**
     * 导出收料材料明细列表
     */
//    @PreAuthorize("@ss.hasPermi('system:material:export')")
    @Log(title = "收料材料明细", businessType = BusinessType.EXPORT)
    @ApiOperation("导出收料材料明细列表Excel")
    @PostMapping("/export")
    public void export(HttpServletResponse response, QReceiveMoreMaterial qReceiveMoreMaterial)
    {
        List<QReceiveMoreMaterial> list = qReceiveMoreMaterialService.selectQReceiveMoreMaterialList(qReceiveMoreMaterial);
        ExcelUtil<QReceiveMoreMaterial> util = new ExcelUtil<QReceiveMoreMaterial>(QReceiveMoreMaterial.class);
        util.exportExcel(response, list, "收料材料明细数据");
    }

    /**
     * 获取收料材料明细详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:material:query')")
    @ApiOperation("获取收料材料明细详细信息")
    @GetMapping(value = "/{orgId}")
    public AjaxResult getInfo(@ApiParam(name = "orgId", value = "收料材料明细参数", required = true)
            @PathVariable("orgId") String orgId)
    {
        return success(qReceiveMoreMaterialService.selectQReceiveMoreMaterialByOrgId(orgId));
    }

    /**
     * 新增收料材料明细
     */
//    @PreAuthorize("@ss.hasPermi('system:material:add')")
    @Log(title = "收料材料明细", businessType = BusinessType.INSERT)
    @ApiOperation("新增收料材料明细")
    @PostMapping
    public AjaxResult add(@RequestBody QReceiveMoreMaterial qReceiveMoreMaterial)
    {
        return toAjax(qReceiveMoreMaterialService.insertQReceiveMoreMaterial(qReceiveMoreMaterial));
    }

    /**
     * 修改收料材料明细
     */
//    @PreAuthorize("@ss.hasPermi('system:material:edit')")
    @Log(title = "收料材料明细", businessType = BusinessType.UPDATE)
    @ApiOperation("修改收料材料明细")
    @PutMapping
    public AjaxResult edit(@RequestBody QReceiveMoreMaterial qReceiveMoreMaterial)
    {
        return toAjax(qReceiveMoreMaterialService.updateQReceiveMoreMaterial(qReceiveMoreMaterial));
    }

    /**
     * 删除收料材料明细
     */
//    @PreAuthorize("@ss.hasPermi('system:material:remove')")
    @Log(title = "收料材料明细", businessType = BusinessType.DELETE)
    @ApiOperation("删除收料材料明细")
	@DeleteMapping("/{orgIds}")
    public AjaxResult remove(@ApiParam(name = "orgIds", value = "收料材料明细ids参数", required = true)
            @PathVariable String[] orgIds)
    {
        return toAjax(qReceiveMoreMaterialService.deleteQReceiveMoreMaterialByOrgIds(orgIds));
    }
}
