package com.ruoyi.iot.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.iot.domain.SysConstructionProgress;
import com.ruoyi.iot.service.ISysConstructionProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 施工Controller
 * 
 * @author mashir0
 * @date 2024-08-21
 */
@RestController
@RequestMapping("/construction/progress")
public class SysConstructionProgressController extends BaseController
{
    @Autowired
    private ISysConstructionProgressService sysConstructionProgressService;

    /**
     * 获取施工详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:progress:query')")
    @GetMapping(value = "")
    public AjaxResult getInfo()
    {
        SysConstructionProgress one = sysConstructionProgressService.getOne(new LambdaQueryWrapper<>(), false);
        if(one!=null) one.setConstructedDuration(DateUtil.betweenDay(one.getStartDate(), new Date(), true));
        return success(one);
    }

}
