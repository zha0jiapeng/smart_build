package com.ruoyi.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.ruoyi.iot.domain.ElectricityMonitoring;

/**
 * 用电监测Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-23
 */
public interface ElectricityMonitoringMapper extends BaseMapper<ElectricityMonitoring>
{
    /**
     * 查询用电监测
     * 
     * @param id 用电监测主键
     * @return 用电监测
     */
    public ElectricityMonitoring selectElectricityMonitoringById(Long id);

    /**
     * 查询用电监测列表
     * 
     * @param electricityMonitoring 用电监测
     * @return 用电监测集合
     */
    public List<ElectricityMonitoring> selectElectricityMonitoringList(ElectricityMonitoring electricityMonitoring);

    /**
     * 新增用电监测
     * 
     * @param electricityMonitoring 用电监测
     * @return 结果
     */
    public int insertElectricityMonitoring(ElectricityMonitoring electricityMonitoring);

    /**
     * 修改用电监测
     * 
     * @param electricityMonitoring 用电监测
     * @return 结果
     */
    public int updateElectricityMonitoring(ElectricityMonitoring electricityMonitoring);

    /**
     * 删除用电监测
     * 
     * @param id 用电监测主键
     * @return 结果
     */
    public int deleteElectricityMonitoringById(Long id);

    /**
     * 批量删除用电监测
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteElectricityMonitoringByIds(Long[] ids);
}
