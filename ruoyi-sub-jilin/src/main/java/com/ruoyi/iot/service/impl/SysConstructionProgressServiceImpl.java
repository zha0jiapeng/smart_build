package com.ruoyi.iot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.iot.domain.SysConstructionProgress;
import com.ruoyi.iot.mapper.SysConstructionProgressMapper;
import com.ruoyi.iot.service.ISysConstructionProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 施工Service业务层处理
 * 
 * @author mashir0
 * @date 2024-08-21
 */
@Service
public class SysConstructionProgressServiceImpl extends ServiceImpl<SysConstructionProgressMapper, SysConstructionProgress> implements ISysConstructionProgressService
{
    @Autowired(required = false)
    private SysConstructionProgressMapper sysConstructionProgressMapper;

}
