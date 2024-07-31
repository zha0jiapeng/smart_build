package com.ruoyi.iot.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.iot.domain.Device;
import com.ruoyi.iot.mapper.DeviceMapper;
import com.ruoyi.iot.service.IDeviceService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 设备Service业务层处理
 * 
 * @author mashir0
 * @date 2024-06-23
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService
{
    @Resource
    DeviceMapper deviceMapper;
}
