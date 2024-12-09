package com.ruoyi.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.iot.domain.Alarm;

import java.util.List;


/**
 * 报警Mapper接口
 * 
 * @author liang
 * @date 2024-08-21
 */
public interface AlarmMapper extends BaseMapper<Alarm>
{
    /**
     * 查询报警
     * 
     * @param id 报警主键
     * @return 报警
     */
    public Alarm selectAlarmById(Long id);

    /**
     * 查询报警列表
     * 
     * @param alarm 报警
     * @return 报警集合
     */
    public List<Alarm> selectAlarmList(Alarm alarm);

    /**
     * 新增报警
     * 
     * @param alarm 报警
     * @return 结果
     */
    public int insertAlarm(Alarm alarm);

    /**
     * 修改报警
     * 
     * @param alarm 报警
     * @return 结果
     */
    public int updateAlarm(Alarm alarm);

    /**
     * 删除报警
     * 
     * @param id 报警主键
     * @return 结果
     */
    public int deleteAlarmById(Long id);

    /**
     * 批量删除报警
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAlarmByIds(Long[] ids);
}
