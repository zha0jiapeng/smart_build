package com.ruoyi.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.iot.domain.QReceive;
import com.ruoyi.iot.domain.QReceiveMoreMaterial;
import com.ruoyi.iot.mapper.QReceiveMoreMaterialMapper;
import com.ruoyi.iot.service.IQReceiveMoreMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 收料材料明细Service业务层处理
 *
 * @author ruoyi
 * @date 2024-10-09
 */
@Service
public class QReceiveMoreMaterialServiceImpl extends ServiceImpl<QReceiveMoreMaterialMapper, QReceiveMoreMaterial> implements IQReceiveMoreMaterialService {
    @Autowired(required = false)
    private QReceiveMoreMaterialMapper qReceiveMoreMaterialMapper;

    @Autowired
    private IQReceiveMoreMaterialService qReceiveMoreMaterialService;

    /**
     * 查询收料材料明细
     *
     * @param orgId 收料材料明细主键
     * @return 收料材料明细
     */
    @Override
    public QReceiveMoreMaterial selectQReceiveMoreMaterialByOrgId(String orgId) {
        return qReceiveMoreMaterialMapper.selectQReceiveMoreMaterialByOrgId(orgId);
    }

    /**
     * 查询收料材料明细列表
     *
     * @param qReceiveMoreMaterial 收料材料明细
     * @return 收料材料明细
     */
    @Override
    public List<QReceiveMoreMaterial> selectQReceiveMoreMaterialList(QReceiveMoreMaterial qReceiveMoreMaterial) {
        return qReceiveMoreMaterialMapper.selectQReceiveMoreMaterialList(qReceiveMoreMaterial);
    }

    /**
     * 新增收料材料明细
     *
     * @param qReceiveMoreMaterial 收料材料明细
     * @return 结果
     */
    @Override
    public int insertQReceiveMoreMaterial(QReceiveMoreMaterial qReceiveMoreMaterial) {
        return qReceiveMoreMaterialMapper.insertQReceiveMoreMaterial(qReceiveMoreMaterial);
    }

    /**
     * 修改收料材料明细
     *
     * @param qReceiveMoreMaterial 收料材料明细
     * @return 结果
     */
    @Override
    public int updateQReceiveMoreMaterial(QReceiveMoreMaterial qReceiveMoreMaterial) {
        return qReceiveMoreMaterialMapper.updateQReceiveMoreMaterial(qReceiveMoreMaterial);
    }

    /**
     * 批量删除收料材料明细
     *
     * @param orgIds 需要删除的收料材料明细主键
     * @return 结果
     */
    @Override
    public int deleteQReceiveMoreMaterialByOrgIds(String[] orgIds) {
        return qReceiveMoreMaterialMapper.deleteQReceiveMoreMaterialByOrgIds(orgIds);
    }

    /**
     * 删除收料材料明细信息
     *
     * @param orgId 收料材料明细主键
     * @return 结果
     */
    @Override
    public int deleteQReceiveMoreMaterialByOrgId(String orgId) {
        return qReceiveMoreMaterialMapper.deleteQReceiveMoreMaterialByOrgId(orgId);
    }

    /**
     * 查询地磅信息
     *
     * @return 结果
     */
    @Override
    public Map<String, List<QReceiveMoreMaterial>> selectQReceiveMoreMaterialList(Map<String, List<QReceive>> qreceiveMap) {
        Map<String, List<QReceiveMoreMaterial>> listMap = new HashMap<>();
        List<QReceive> qReceiveSLAVEList = qreceiveMap.get("SLAVE");
        if (qReceiveSLAVEList.size() != 0) {
            List<String> idSLAVEList = qReceiveSLAVEList.stream()
                    .map(QReceive::getOrderId)
                    .collect(Collectors.toList());
            listMap.put("SLAVE", qReceiveMoreMaterialService.selectQReceiveMoreMaterialListSLAVE(idSLAVEList));
        }else {
            listMap.put("SLAVE",new ArrayList<>());
        }
        List<QReceive> qReceiveSLAVEDATAList = qreceiveMap.get("SLAVEDATA");
        if (qReceiveSLAVEDATAList.size() != 0) {
            List<String> idSLAVEDATAList = qReceiveSLAVEDATAList.stream()
                    .map(QReceive::getOrderId)
                    .collect(Collectors.toList());
            listMap.put("SLAVEDATA", qReceiveMoreMaterialService.selectQReceiveMoreMaterialListSLAVEDATA(idSLAVEDATAList));
        }else {
            listMap.put("SLAVEDATA",new ArrayList<>());
        }
        return listMap;
    }

    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public List<QReceiveMoreMaterial> selectQReceiveMoreMaterialListSLAVE(List<String> idList) {
        QueryWrapper<QReceiveMoreMaterial> queryWrapper = new QueryWrapper<>();
        // 使用in方法查询ID在列表中的记录
        queryWrapper.in("order_id", idList);
        return qReceiveMoreMaterialMapper.selectList(queryWrapper);
    }

    @Override
    @DataSource(value = DataSourceType.SLAVEDATA)
    public List<QReceiveMoreMaterial> selectQReceiveMoreMaterialListSLAVEDATA(List<String> idList) {
        QueryWrapper<QReceiveMoreMaterial> queryWrapper = new QueryWrapper<>();
        // 使用in方法查询ID在列表中的记录
        queryWrapper.in("order_id", idList);
        return qReceiveMoreMaterialMapper.selectList(queryWrapper);
    }
}
