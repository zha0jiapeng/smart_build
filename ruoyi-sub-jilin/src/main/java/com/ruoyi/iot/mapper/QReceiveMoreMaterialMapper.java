package com.ruoyi.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.ruoyi.iot.domain.QReceiveMoreMaterial;

/**
 * 收料材料明细Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-09
 */
public interface QReceiveMoreMaterialMapper extends BaseMapper<QReceiveMoreMaterial>
{
    /**
     * 查询收料材料明细
     * 
     * @param orgId 收料材料明细主键
     * @return 收料材料明细
     */
    public QReceiveMoreMaterial selectQReceiveMoreMaterialByOrgId(String orgId);

    /**
     * 查询收料材料明细列表
     * 
     * @param qReceiveMoreMaterial 收料材料明细
     * @return 收料材料明细集合
     */
    public List<QReceiveMoreMaterial> selectQReceiveMoreMaterialList(QReceiveMoreMaterial qReceiveMoreMaterial);

    /**
     * 新增收料材料明细
     * 
     * @param qReceiveMoreMaterial 收料材料明细
     * @return 结果
     */
    public int insertQReceiveMoreMaterial(QReceiveMoreMaterial qReceiveMoreMaterial);

    /**
     * 修改收料材料明细
     * 
     * @param qReceiveMoreMaterial 收料材料明细
     * @return 结果
     */
    public int updateQReceiveMoreMaterial(QReceiveMoreMaterial qReceiveMoreMaterial);

    /**
     * 删除收料材料明细
     * 
     * @param orgId 收料材料明细主键
     * @return 结果
     */
    public int deleteQReceiveMoreMaterialByOrgId(String orgId);

    /**
     * 批量删除收料材料明细
     * 
     * @param orgIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQReceiveMoreMaterialByOrgIds(String[] orgIds);
}
