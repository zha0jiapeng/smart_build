package com.ruoyi.iot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.ElectricityMonitoring;
import com.ruoyi.iot.mapper.ElectricityMonitoringMapper;
import com.ruoyi.iot.service.IElectricityMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用电监测Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
@Service
public class ElectricityMonitoringServiceImpl extends ServiceImpl<ElectricityMonitoringMapper, ElectricityMonitoring> implements IElectricityMonitoringService
{
    @Autowired(required = false)
    private ElectricityMonitoringMapper electricityMonitoringMapper;

    /**
     * 查询用电监测
     * 
     * @param id 用电监测主键
     * @return 用电监测
     */
    @Override
    public ElectricityMonitoring selectElectricityMonitoringById(Long id)
    {
        return electricityMonitoringMapper.selectElectricityMonitoringById(id);
    }

    /**
     * 查询用电监测列表
     * 
     * @param electricityMonitoring 用电监测
     * @return 用电监测
     */
    @Override
    public List<ElectricityMonitoring> selectElectricityMonitoringList(ElectricityMonitoring electricityMonitoring)
    {
        return electricityMonitoringMapper.selectElectricityMonitoringList(electricityMonitoring);
    }

    /**
     * 新增用电监测
     * 
     * @param electricityMonitoring 用电监测
     * @return 结果
     */
    @Override
    public int insertElectricityMonitoring(ElectricityMonitoring electricityMonitoring)
    {
        electricityMonitoring.setCreateTime(DateUtils.getNowDate());
        return electricityMonitoringMapper.insertElectricityMonitoring(electricityMonitoring);
    }

    /**
     * 修改用电监测
     * 
     * @param electricityMonitoring 用电监测
     * @return 结果
     */
    @Override
    public int updateElectricityMonitoring(ElectricityMonitoring electricityMonitoring)
    {
        electricityMonitoring.setUpdateTime(DateUtils.getNowDate());
        return electricityMonitoringMapper.updateElectricityMonitoring(electricityMonitoring);
    }

    /**
     * 批量删除用电监测
     * 
     * @param ids 需要删除的用电监测主键
     * @return 结果
     */
    @Override
    public int deleteElectricityMonitoringByIds(Long[] ids)
    {
        return electricityMonitoringMapper.deleteElectricityMonitoringByIds(ids);
    }

    /**
     * 删除用电监测信息
     * 
     * @param id 用电监测主键
     * @return 结果
     */
    @Override
    public int deleteElectricityMonitoringById(Long id)
    {
        return electricityMonitoringMapper.deleteElectricityMonitoringById(id);
    }
}
