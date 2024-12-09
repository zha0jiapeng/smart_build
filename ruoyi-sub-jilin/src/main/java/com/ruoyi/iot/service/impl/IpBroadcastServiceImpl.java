package com.ruoyi.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.IpBroadcast;
import com.ruoyi.iot.mapper.IpBroadcastMapper;
import com.ruoyi.iot.service.IIpBroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * IP广播Service业务层处理
 * 
 * @author liang
 * @date 2024-12-06
 */
@Service
public class IpBroadcastServiceImpl extends ServiceImpl<IpBroadcastMapper, IpBroadcast> implements IIpBroadcastService
{
    @Autowired(required = false)
    private IpBroadcastMapper ipBroadcastMapper;

    /**
     * 查询IP广播
     * 
     * @param id IP广播主键
     * @return IP广播
     */
    @Override
    public IpBroadcast selectIpBroadcastById(Long id)
    {
        return ipBroadcastMapper.selectById(id);
    }

    /**
     * 查询IP广播列表
     * 
     * @param ipBroadcast IP广播
     * @return IP广播
     */
    @Override
    public List<IpBroadcast> selectIpBroadcastList(IpBroadcast ipBroadcast)
    {
        return ipBroadcastMapper.selectList(new LambdaQueryWrapper<>(ipBroadcast));
    }

    /**
     * 新增IP广播
     * 
     * @param ipBroadcast IP广播
     * @return 结果
     */
    @Override
    public int insertIpBroadcast(IpBroadcast ipBroadcast)
    {
        ipBroadcast.setCreateTime(DateUtils.getNowDate());
        return ipBroadcastMapper.insert(ipBroadcast);
    }

    /**
     * 修改IP广播
     * 
     * @param ipBroadcast IP广播
     * @return 结果
     */
    @Override
    public int updateIpBroadcast(IpBroadcast ipBroadcast)
    {
        ipBroadcast.setUpdateTime(DateUtils.getNowDate());
        return ipBroadcastMapper.updateById(ipBroadcast);
    }

    /**
     * 批量删除IP广播
     * 
     * @param ids 需要删除的IP广播主键
     * @return 结果
     */
    @Override
    public int deleteIpBroadcastByIds(Long[] ids)
    {
        return ipBroadcastMapper.deleteIpBroadcastByIds(ids);
    }

    /**
     * 删除IP广播信息
     * 
     * @param id IP广播主键
     * @return 结果
     */
    @Override
    public int deleteIpBroadcastById(Long id)
    {
        return ipBroadcastMapper.deleteIpBroadcastById(id);
    }
}
