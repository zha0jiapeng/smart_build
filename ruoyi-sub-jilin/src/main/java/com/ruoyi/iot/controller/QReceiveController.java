package com.ruoyi.iot.controller;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.iot.domain.QReceive;
import com.ruoyi.iot.service.IQReceiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 收料Controller
 * 
 * @author ruoyi
 * @date 2024-10-08
 */
@RestController
@RequestMapping("/system/receive")
@Api(tags={"收料 Controller"})
public class QReceiveController extends BaseController
{
    @Autowired
    private IQReceiveService qReceiveService;

    /**
     * 查询收料列表
     */
//    @PreAuthorize("@ss.hasPermi('system:receive:list')")
    @GetMapping("/list")
    @ApiOperation("查询收料列表")
    public TableDataInfo list(QReceive qReceive)
    {
        startPage();
        List<QReceive> list = qReceiveService.selectQReceiveList(qReceive);
        return getDataTable(list);
    }

    /**
     * 导出收料列表
     */
    @PreAuthorize("@ss.hasPermi('system:receive:export')")
    @Log(title = "收料", businessType = BusinessType.EXPORT)
    @ApiOperation("导出收料列表Excel")
    @PostMapping("/export")
    public void export(HttpServletResponse response, QReceive qReceive)
    {
        List<QReceive> list = qReceiveService.selectQReceiveList(qReceive);
        ExcelUtil<QReceive> util = new ExcelUtil<QReceive>(QReceive.class);
        util.exportExcel(response, list, "收料数据");
    }

    /**
     * 获取收料详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:receive:query')")
    @ApiOperation("获取收料详细信息")
    @GetMapping(value = "/{orgId}")
    public AjaxResult getInfo(@ApiParam(name = "orgId", value = "收料参数", required = true)
            @PathVariable("orgId") String orgId)
    {
        return success(qReceiveService.selectQReceiveByOrgId(orgId));
    }

    /**
     * 新增收料
     */
    @PreAuthorize("@ss.hasPermi('system:receive:add')")
    @Log(title = "收料", businessType = BusinessType.INSERT)
    @ApiOperation("新增收料")
    @PostMapping
    public AjaxResult add(@RequestBody QReceive qReceive)
    {
        return toAjax(qReceiveService.insertQReceive(qReceive));
    }

    /**
     * 修改收料
     */
    @PreAuthorize("@ss.hasPermi('system:receive:edit')")
    @Log(title = "收料", businessType = BusinessType.UPDATE)
    @ApiOperation("修改收料")
    @PutMapping
    public AjaxResult edit(@RequestBody QReceive qReceive)
    {
        return toAjax(qReceiveService.updateQReceive(qReceive));
    }

    /**
     * 删除收料
     */
    @PreAuthorize("@ss.hasPermi('system:receive:remove')")
    @Log(title = "收料", businessType = BusinessType.DELETE)
    @ApiOperation("删除收料")
	@DeleteMapping("/{orgIds}")
    public AjaxResult remove(@ApiParam(name = "orgIds", value = "收料ids参数", required = true)
            @PathVariable String[] orgIds)
    {
        return toAjax(qReceiveService.deleteQReceiveByOrgIds(orgIds));
    }
}
