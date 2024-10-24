package com.ruoyi.iot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.iot.domain.QReceive;

import java.util.List;

/**
 * 收料Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-08
 */
public interface QReceiveMapper extends BaseMapper<QReceive>
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
     * 删除收料
     * 
     * @param orgId 收料主键
     * @return 结果
     */
    public int deleteQReceiveByOrgId(String orgId);

    /**
     * 批量删除收料
     * 
     * @param orgIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQReceiveByOrgIds(String[] orgIds);
}
