package com.ruoyi.iot.controller;


import com.ruoyi.iot.domain.DustMonitoringDevice;
import com.ruoyi.iot.service.IDustMonitoringDeviceService;
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
 * 扬尘监测仪Controller
 * 
 * @author liang
 * @date 2024-08-05
 */
@RestController
@RequestMapping("/system/dustmonitoringdevice")
@Api(tags={"扬尘监测仪 Controller"})
public class DustMonitoringDeviceController extends BaseController
{
    @Autowired
    private IDustMonitoringDeviceService dustMonitoringDeviceService;

    /**
     * 查询扬尘监测仪列表
     */
//    @PreAuthorize("@ss.hasPermi('system:dustmonitoringdevice:list')")
    @GetMapping("/list")
    @ApiOperation("查询扬尘监测仪列表")
    public TableDataInfo list(DustMonitoringDevice dustMonitoringDevice)
    {
        startPage();
        List<DustMonitoringDevice> list = dustMonitoringDeviceService.selectDustMonitoringDeviceList(dustMonitoringDevice);
        return getDataTable(list);
    }

    /**
     * 导出扬尘监测仪列表
     */
    @PreAuthorize("@ss.hasPermi('system:dustmonitoringdevice:export')")
    @Log(title = "扬尘监测仪", businessType = BusinessType.EXPORT)
    @ApiOperation("导出扬尘监测仪列表Excel")
    @PostMapping("/export")
    public void export(HttpServletResponse response, DustMonitoringDevice dustMonitoringDevice)
    {
        List<DustMonitoringDevice> list = dustMonitoringDeviceService.selectDustMonitoringDeviceList(dustMonitoringDevice);
        ExcelUtil<DustMonitoringDevice> util = new ExcelUtil<DustMonitoringDevice>(DustMonitoringDevice.class);
        util.exportExcel(response, list, "扬尘监测仪数据");
    }

    /**
     * 获取扬尘监测仪详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:dustmonitoringdevice:query')")
    @ApiOperation("获取扬尘监测仪详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(name = "id", value = "扬尘监测仪参数", required = true)
            @PathVariable("id") Long id)
    {
        return success(dustMonitoringDeviceService.selectDustMonitoringDeviceById(id));
    }

    /**
     * 新增扬尘监测仪
     */
//    @PreAuthorize("@ss.hasPermi('system:dustmonitoringdevice:add')")
    @Log(title = "扬尘监测仪", businessType = BusinessType.INSERT)
    @ApiOperation("新增扬尘监测仪")
    @PostMapping
    public AjaxResult add(@RequestBody DustMonitoringDevice dustMonitoringDevice)
    {
        return toAjax(dustMonitoringDeviceService.insertDustMonitoringDevice(dustMonitoringDevice));
    }

    /**
     * 修改扬尘监测仪
     */
//    @PreAuthorize("@ss.hasPermi('system:dustmonitoringdevice:edit')")
    @Log(title = "扬尘监测仪", businessType = BusinessType.UPDATE)
    @ApiOperation("修改扬尘监测仪")
    @PutMapping
    public AjaxResult edit(@RequestBody DustMonitoringDevice dustMonitoringDevice)
    {
        return toAjax(dustMonitoringDeviceService.updateDustMonitoringDevice(dustMonitoringDevice));
    }

    /**
     * 删除扬尘监测仪
     */
    @PreAuthorize("@ss.hasPermi('system:dustmonitoringdevice:remove')")
    @Log(title = "扬尘监测仪", businessType = BusinessType.DELETE)
    @ApiOperation("删除扬尘监测仪")
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(name = "ids", value = "扬尘监测仪ids参数", required = true)
            @PathVariable Long[] ids)
    {
        return toAjax(dustMonitoringDeviceService.deleteDustMonitoringDeviceByIds(ids));
    }


    /**
     * 查询最新一条记录扬尘监测仪
     */
    @GetMapping("/latestRecord")
    @ApiOperation("查询最新一条记录扬尘监测仪")
    public AjaxResult latestRecord()
    {
        return success(dustMonitoringDeviceService.selectDustMonitoringDeviceNow());
    }
}
