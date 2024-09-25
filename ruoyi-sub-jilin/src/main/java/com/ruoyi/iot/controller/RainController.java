package com.ruoyi.iot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.basic.Rain;
import com.ruoyi.system.mapper.RainMapper;
import com.ruoyi.system.service.IRainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 雨量计Controller
 *
 * @author ruoyi
 * @date 2024-08-19
 */
@RestController
@RequestMapping("/system/rain")
@Api(tags = {"雨量计 Controller"})
public class RainController extends BaseController {
    @Autowired
    private IRainService rainService;

    @Autowired(required = false)
    private RainMapper rainMapper;

    /**
     * 查询雨量计列表
     */
//    @PreAuthorize("@ss.hasPermi('system:rain:list')")
    @GetMapping("/list")
    @ApiOperation("查询雨量计列表")
    public TableDataInfo list(Rain rain) {
        startPage();
        List<Rain> list = rainService.selectRainList(rain);
        return getDataTable(list);
    }

    /**
     * 导出雨量计列表
     */
//    @PreAuthorize("@ss.hasPermi('system:rain:export')")
    @Log(title = "雨量计", businessType = BusinessType.EXPORT)
    @ApiOperation("导出雨量计列表Excel")
    @PostMapping("/export")
    public void export(HttpServletResponse response, Rain rain) {
        List<Rain> list = rainService.selectRainList(rain);
        ExcelUtil<Rain> util = new ExcelUtil<Rain>(Rain.class);
        util.exportExcel(response, list, "雨量计数据");
    }

    /**
     * 获取雨量计详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:rain:query')")
    @ApiOperation("获取雨量计详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(name = "id", value = "雨量计参数", required = true)
                              @PathVariable("id") Long id) {
        return success(rainService.selectRainById(id));
    }

    /**
     * 新增雨量计
     */
//    @PreAuthorize("@ss.hasPermi('system:rain:add')")
    @Log(title = "雨量计", businessType = BusinessType.INSERT)
    @ApiOperation("新增雨量计")
    @PostMapping
    public AjaxResult add(@RequestBody Rain rain) {
        return toAjax(rainService.insertRain(rain));
    }

    /**
     * 修改雨量计
     */
//    @PreAuthorize("@ss.hasPermi('system:rain:edit')")
    @Log(title = "雨量计", businessType = BusinessType.UPDATE)
    @ApiOperation("修改雨量计")
    @PutMapping
    public AjaxResult edit(@RequestBody Rain rain) {
        return toAjax(rainService.updateRain(rain));
    }

    /**
     * 删除雨量计
     */
//    @PreAuthorize("@ss.hasPermi('system:rain:remove')")
    @Log(title = "雨量计", businessType = BusinessType.DELETE)
    @ApiOperation("删除雨量计")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(name = "ids", value = "雨量计ids参数", required = true)
                             @PathVariable Long[] ids) {
        return toAjax(rainService.deleteRainByIds(ids));
    }


    @GetMapping("/getLatestRain/14")
    public AjaxResult getLatestRain14() {
        QueryWrapper<Rain> rainQueryWrapper = new QueryWrapper<>();
        rainQueryWrapper.eq("device_code", "2407052002LXY-02");
        rainQueryWrapper.orderByDesc("id").last("limit 1");
        Rain latestRain = rainMapper.selectOne(rainQueryWrapper);
        return success(latestRain);
    }

    @GetMapping("/getLatestRain/15")
    public AjaxResult getLatestRain15() {
        QueryWrapper<Rain> rainQueryWrapper = new QueryWrapper<>();
        rainQueryWrapper.eq("device_code", "2407052002LXY-01");
        rainQueryWrapper.orderByDesc("id").last("limit 1");
        Rain latestRain = rainMapper.selectOne(rainQueryWrapper);
        return success(latestRain);
    }
}
