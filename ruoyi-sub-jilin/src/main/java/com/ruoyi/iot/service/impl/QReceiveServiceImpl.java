package com.ruoyi.iot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.iot.domain.QReceive;
import com.ruoyi.iot.mapper.QReceiveMapper;
import com.ruoyi.iot.service.IQReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收料Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-08
 */
@Service
@DataSource(value = DataSourceType.SLAVE)
public class QReceiveServiceImpl extends ServiceImpl<QReceiveMapper, QReceive> implements IQReceiveService
{
    @Autowired(required = false)
    private QReceiveMapper qReceiveMapper;

    /**
     * 查询收料
     * 
     * @param orgId 收料主键
     * @return 收料
     */
    @Override
    public QReceive selectQReceiveByOrgId(String orgId)
    {
        return qReceiveMapper.selectQReceiveByOrgId(orgId);
    }

    /**
     * 查询收料列表
     * 
     * @param qReceive 收料
     * @return 收料
     */
    @Override
    public List<QReceive> selectQReceiveList(QReceive qReceive)
    {
        return qReceiveMapper.selectQReceiveList(qReceive);
    }

    /**
     * 新增收料
     * 
     * @param qReceive 收料
     * @return 结果
     */
    @Override
    public int insertQReceive(QReceive qReceive)
    {
        return qReceiveMapper.insertQReceive(qReceive);
    }

    /**
     * 修改收料
     * 
     * @param qReceive 收料
     * @return 结果
     */
    @Override
    public int updateQReceive(QReceive qReceive)
    {
        return qReceiveMapper.updateQReceive(qReceive);
    }

    /**
     * 批量删除收料
     * 
     * @param orgIds 需要删除的收料主键
     * @return 结果
     */
    @Override
    public int deleteQReceiveByOrgIds(String[] orgIds)
    {
        return qReceiveMapper.deleteQReceiveByOrgIds(orgIds);
    }

    /**
     * 删除收料信息
     * 
     * @param orgId 收料主键
     * @return 结果
     */
    @Override
    public int deleteQReceiveByOrgId(String orgId)
    {
        return qReceiveMapper.deleteQReceiveByOrgId(orgId);
    }
}
