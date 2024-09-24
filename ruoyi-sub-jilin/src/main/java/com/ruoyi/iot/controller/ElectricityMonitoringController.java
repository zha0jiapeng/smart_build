package com.ruoyi.iot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.iot.domain.ElectricityMonitoring;
import com.ruoyi.iot.mapper.ElectricityMonitoringMapper;
import com.ruoyi.iot.service.IElectricityMonitoringService;
import com.ruoyi.system.domain.basic.Rain;
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
 * 用电监测Controller
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@RestController
@RequestMapping("/system/monitoring")
@Api(tags={"用电监测 Controller"})
public class ElectricityMonitoringController extends BaseController
{
    @Autowired
    private IElectricityMonitoringService electricityMonitoringService;

    @Autowired
    private ElectricityMonitoringMapper electricityMonitoringMapper;

    /**
     * 查询用电监测列表
     */
//    @PreAuthorize("@ss.hasPermi('system:monitoring:list')")
    @GetMapping("/list")
    @ApiOperation("查询用电监测列表")
    public TableDataInfo list(ElectricityMonitoring electricityMonitoring)
    {
        startPage();
        List<ElectricityMonitoring> list = electricityMonitoringService.selectElectricityMonitoringList(electricityMonitoring);
        return getDataTable(list);
    }

    /**
     * 导出用电监测列表
     */
//    @PreAuthorize("@ss.hasPermi('system:monitoring:export')")
    @Log(title = "用电监测", businessType = BusinessType.EXPORT)
    @ApiOperation("导出用电监测列表Excel")
    @PostMapping("/export")
    public void export(HttpServletResponse response, ElectricityMonitoring electricityMonitoring)
    {
        List<ElectricityMonitoring> list = electricityMonitoringService.selectElectricityMonitoringList(electricityMonitoring);
        ExcelUtil<ElectricityMonitoring> util = new ExcelUtil<ElectricityMonitoring>(ElectricityMonitoring.class);
        util.exportExcel(response, list, "用电监测数据");
    }

    /**
     * 获取用电监测详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:monitoring:query')")
    @ApiOperation("获取用电监测详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(name = "id", value = "用电监测参数", required = true)
            @PathVariable("id") Long id)
    {
        return success(electricityMonitoringService.selectElectricityMonitoringById(id));
    }

    /**
     * 新增用电监测
     */
//    @PreAuthorize("@ss.hasPermi('system:monitoring:add')")
    @Log(title = "用电监测", businessType = BusinessType.INSERT)
    @ApiOperation("新增用电监测")
    @PostMapping
    public AjaxResult add(@RequestBody ElectricityMonitoring electricityMonitoring)
    {
        return toAjax(electricityMonitoringService.insertElectricityMonitoring(electricityMonitoring));
    }

    /**
     * 修改用电监测
     */
//    @PreAuthorize("@ss.hasPermi('system:monitoring:edit')")
    @Log(title = "用电监测", businessType = BusinessType.UPDATE)
    @ApiOperation("修改用电监测")
    @PutMapping
    public AjaxResult edit(@RequestBody ElectricityMonitoring electricityMonitoring)
    {
        return toAjax(electricityMonitoringService.updateElectricityMonitoring(electricityMonitoring));
    }

    /**
     * 删除用电监测
     */
//    @PreAuthorize("@ss.hasPermi('system:monitoring:remove')")
    @Log(title = "用电监测", businessType = BusinessType.DELETE)
    @ApiOperation("删除用电监测")
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(name = "ids", value = "用电监测ids参数", required = true)
            @PathVariable Long[] ids)
    {
        return toAjax(electricityMonitoringService.deleteElectricityMonitoringByIds(ids));
    }

    /**
     * 用电监测最新一条数据
     */
    @GetMapping("/getLatestElectricityMonitoring")
    public AjaxResult getLatestElectricityMonitoring()
    {
        QueryWrapper<ElectricityMonitoring> electricityMonitoringQueryWrapper = new QueryWrapper<>();
        electricityMonitoringQueryWrapper.orderByDesc("id").last("limit 1");
        ElectricityMonitoring latestElectricityMonitoring = electricityMonitoringMapper.selectOne(electricityMonitoringQueryWrapper);
        return success(latestElectricityMonitoring);
    }


}
