package com.ruoyi.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.iot.domain.IpBroadcast;

import java.util.List;

/**
 * IP广播Service接口
 * 
 * @author liang
 * @date 2024-12-06
 */
public interface IIpBroadcastService  extends IService<IpBroadcast>
{
    /**
     * 查询IP广播
     * 
     * @param id IP广播主键
     * @return IP广播
     */
    public IpBroadcast selectIpBroadcastById(Long id);

    /**
     * 查询IP广播列表
     * 
     * @param ipBroadcast IP广播
     * @return IP广播集合
     */
    public List<IpBroadcast> selectIpBroadcastList(IpBroadcast ipBroadcast);

    /**
     * 新增IP广播
     * 
     * @param ipBroadcast IP广播
     * @return 结果
     */
    public int insertIpBroadcast(IpBroadcast ipBroadcast);

    /**
     * 修改IP广播
     * 
     * @param ipBroadcast IP广播
     * @return 结果
     */
    public int updateIpBroadcast(IpBroadcast ipBroadcast);

    /**
     * 批量删除IP广播
     * 
     * @param ids 需要删除的IP广播主键集合
     * @return 结果
     */
    public int deleteIpBroadcastByIds(Long[] ids);

    /**
     * 删除IP广播信息
     * 
     * @param id IP广播主键
     * @return 结果
     */
    public int deleteIpBroadcastById(Long id);
}
