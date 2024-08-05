package com.ruoyi.iot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.DustMonitoringDevice;
import com.ruoyi.iot.mapper.DustMonitoringDeviceMapper;
import com.ruoyi.iot.service.IDustMonitoringDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 扬尘监测仪Service业务层处理
 * 
 * @author liang
 * @date 2024-08-05
 */
@Service
public class DustMonitoringDeviceServiceImpl extends ServiceImpl<DustMonitoringDeviceMapper, DustMonitoringDevice> implements IDustMonitoringDeviceService
{
    @Autowired(required = false)
    private DustMonitoringDeviceMapper dustMonitoringDeviceMapper;

    /**
     * 查询扬尘监测仪
     * 
     * @param id 扬尘监测仪主键
     * @return 扬尘监测仪
     */
    @Override
    public DustMonitoringDevice selectDustMonitoringDeviceById(Long id)
    {
        return dustMonitoringDeviceMapper.selectDustMonitoringDeviceById(id);
    }

    /**
     * 查询扬尘监测仪列表
     * 
     * @param dustMonitoringDevice 扬尘监测仪
     * @return 扬尘监测仪
     */
    @Override
    public List<DustMonitoringDevice> selectDustMonitoringDeviceList(DustMonitoringDevice dustMonitoringDevice)
    {
        return dustMonitoringDeviceMapper.selectDustMonitoringDeviceList(dustMonitoringDevice);
    }

    /**
     * 新增扬尘监测仪
     * 
     * @param dustMonitoringDevice 扬尘监测仪
     * @return 结果
     */
    @Override
    public int insertDustMonitoringDevice(DustMonitoringDevice dustMonitoringDevice)
    {
        dustMonitoringDevice.setCreateTime(DateUtils.getNowDate());
        return dustMonitoringDeviceMapper.insertDustMonitoringDevice(dustMonitoringDevice);
    }

    /**
     * 修改扬尘监测仪
     * 
     * @param dustMonitoringDevice 扬尘监测仪
     * @return 结果
     */
    @Override
    public int updateDustMonitoringDevice(DustMonitoringDevice dustMonitoringDevice)
    {
        dustMonitoringDevice.setUpdateTime(DateUtils.getNowDate());
        return dustMonitoringDeviceMapper.updateDustMonitoringDevice(dustMonitoringDevice);
    }

    /**
     * 批量删除扬尘监测仪
     * 
     * @param ids 需要删除的扬尘监测仪主键
     * @return 结果
     */
    @Override
    public int deleteDustMonitoringDeviceByIds(Long[] ids)
    {
        return dustMonitoringDeviceMapper.deleteDustMonitoringDeviceByIds(ids);
    }

    /**
     * 删除扬尘监测仪信息
     * 
     * @param id 扬尘监测仪主键
     * @return 结果
     */
    @Override
    public int deleteDustMonitoringDeviceById(Long id)
    {
        return dustMonitoringDeviceMapper.deleteDustMonitoringDeviceById(id);
    }
}
