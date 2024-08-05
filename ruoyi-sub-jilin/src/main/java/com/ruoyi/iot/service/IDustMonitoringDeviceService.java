package com.ruoyi.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.ruoyi.iot.domain.DustMonitoringDevice;

/**
 * 扬尘监测仪Service接口
 * 
 * @author liang
 * @date 2024-08-05
 */
public interface IDustMonitoringDeviceService  extends IService<DustMonitoringDevice>
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
     * 批量删除扬尘监测仪
     * 
     * @param ids 需要删除的扬尘监测仪主键集合
     * @return 结果
     */
    public int deleteDustMonitoringDeviceByIds(Long[] ids);

    /**
     * 删除扬尘监测仪信息
     * 
     * @param id 扬尘监测仪主键
     * @return 结果
     */
    public int deleteDustMonitoringDeviceById(Long id);


    public DustMonitoringDevice selectDustMonitoringDeviceNow();
}
