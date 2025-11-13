package com.ruoyi.iot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.iot.domain.QReceive;


import java.util.List;
import java.util.Map;


/**
 * 收料Service接口
 * 
 * @author ruoyi
 * @date 2024-10-08
 */
public interface IQReceiveService extends IService<QReceive>
{
    /**
     * 查询收料
     * 
     * @param orgId 收料主键
     * @return 收料
     */
    public QReceive selectQReceiveByOrgId(String orgId);

    /**
     * 查询收料列表
     * 
     * @param qReceive 收料
     * @return 收料集合
     */
    public List<QReceive> selectQReceiveList(QReceive qReceive);

    /**
     * 新增收料
     * 
     * @param qReceive 收料
     * @return 结果
     */
    public int insertQReceive(QReceive qReceive);

    /**
     * 修改收料
     * 
     * @param qReceive 收料
     * @return 结果
     */
    public int updateQReceive(QReceive qReceive);

    /**
     * 批量删除收料
     * 
     * @param orgIds 需要删除的收料主键集合
     * @return 结果
     */
    public int deleteQReceiveByOrgIds(String[] orgIds);

    /**
     * 删除收料信息
     * 
     * @param orgId 收料主键
     * @return 结果
     */
    public int deleteQReceiveByOrgId(String orgId);

    Map<String,List<QReceive>> selectQReceiveList();

    @DataSource(value = DataSourceType.SLAVE)
    List<QReceive> selectQReceiveListSLAVE(QueryWrapper<QReceive> queryWrapper);

    @DataSource(value = DataSourceType.SLAVEDATA)
    List<QReceive> selectQReceiveListSLAVEDATA(QueryWrapper<QReceive> queryWrapper);

    @DataSource(value = DataSourceType.SLAVEDATA13)
    List<QReceive> selectQReceiveListSLAVEDATA13(QueryWrapper<QReceive> queryWrapper);
}
