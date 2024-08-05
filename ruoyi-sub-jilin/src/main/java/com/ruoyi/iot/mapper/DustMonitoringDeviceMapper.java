package com.ruoyi.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.iot.domain.DustMonitoringDevice;

import java.util.List;


/**
 * 扬尘监测仪Mapper接口
 * 
 * @author liang
 * @date 2024-08-05
 */
public interface DustMonitoringDeviceMapper extends BaseMapper<DustMonitoringDevice>
{
    /**
     * 查询扬尘监测仪
     * 
     * @param id 扬尘监测仪主键
     * @return 扬尘监测仪
     */
    public DustMonitoringDevice selectDustMonitoringDeviceById(Long id);

    /**
     * 查询扬尘监测仪列表
     * 
     * @param dustMonitoringDevice 扬尘监测仪
     * @return 扬尘监测仪集合
     */
    public List<DustMonitoringDevice> selectDustMonitoringDeviceList(DustMonitoringDevice dustMonitoringDevice);

    /**
     * 新增扬尘监测仪
     * 
     * @param dustMonitoringDevice 扬尘监测仪
     * @return 结果
     */
    public int insertDustMonitoringDevice(DustMonitoringDevice dustMonitoringDevice);

    /**
     * 修改扬尘监测仪
     * 
     * @param dustMonitoringDevice 扬尘监测仪
     * @return 结果
     */
    public int updateDustMonitoringDevice(DustMonitoringDevice dustMonitoringDevice);

    /**
     * 删除扬尘监测仪
     * 
     * @param id 扬尘监测仪主键
     * @return 结果
     */
    public int deleteDustMonitoringDeviceById(Long id);

    /**
     * 批量删除扬尘监测仪
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDustMonitoringDeviceByIds(Long[] ids);
}
