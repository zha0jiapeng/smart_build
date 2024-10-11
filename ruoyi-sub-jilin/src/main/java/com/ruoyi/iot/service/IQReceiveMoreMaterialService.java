package com.ruoyi.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.iot.domain.QReceive;
import com.ruoyi.iot.domain.QReceiveMoreMaterial;

import java.util.List;
import java.util.Map;


/**
 * 收料材料明细Service接口
 * 
 * @author ruoyi
 * @date 2024-10-09
 */
public interface IQReceiveMoreMaterialService  extends IService<QReceiveMoreMaterial>
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
     * 批量删除收料材料明细
     * 
     * @param orgIds 需要删除的收料材料明细主键集合
     * @return 结果
     */
    public int deleteQReceiveMoreMaterialByOrgIds(String[] orgIds);

    /**
     * 删除收料材料明细信息
     * 
     * @param orgId 收料材料明细主键
     * @return 结果
     */
    public int deleteQReceiveMoreMaterialByOrgId(String orgId);

    Map<String,List<QReceiveMoreMaterial>> selectQReceiveMoreMaterialList(Map<String, List<QReceive>> qreceiveMap);

    @DataSource(value = DataSourceType.SLAVE)
    List<QReceiveMoreMaterial> selectQReceiveMoreMaterialListSLAVE(List<String> idList);

    @DataSource(value = DataSourceType.SLAVEDATA)
    List<QReceiveMoreMaterial> selectQReceiveMoreMaterialListSLAVEDATA(List<String> idList);
}
