package com.ruoyi.iot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.WeighbridgeDataMapper;
import com.ruoyi.iot.domain.WeighbridgeData;
import com.ruoyi.iot.service.IWeighbridgeDataService;

/**
 * 地磅数据处理Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-09
 */
@Service
public class WeighbridgeDataServiceImpl extends ServiceImpl<WeighbridgeDataMapper, WeighbridgeData> implements IWeighbridgeDataService
{
    @Autowired(required = false)
    private WeighbridgeDataMapper weighbridgeDataMapper;

    /**
     * 查询地磅数据处理
     * 
     * @param id 地磅数据处理主键
     * @return 地磅数据处理
     */
    @Override
    public WeighbridgeData selectWeighbridgeDataById(Long id)
    {
        return weighbridgeDataMapper.selectWeighbridgeDataById(id);
    }

    /**
     * 查询地磅数据处理列表
     * 
     * @param weighbridgeData 地磅数据处理
     * @return 地磅数据处理
     */
    @Override
    public List<WeighbridgeData> selectWeighbridgeDataList(WeighbridgeData weighbridgeData)
    {
        return weighbridgeDataMapper.selectWeighbridgeDataList(weighbridgeData);
    }

    /**
     * 新增地磅数据处理
     * 
     * @param weighbridgeData 地磅数据处理
     * @return 结果
     */
    @Override
    public int insertWeighbridgeData(WeighbridgeData weighbridgeData)
    {
        weighbridgeData.setCreatedDate(DateUtils.getNowDate());
        return weighbridgeDataMapper.insertWeighbridgeData(weighbridgeData);
    }

    /**
     * 修改地磅数据处理
     * 
     * @param weighbridgeData 地磅数据处理
     * @return 结果
     */
    @Override
    public int updateWeighbridgeData(WeighbridgeData weighbridgeData)
    {
        return weighbridgeDataMapper.updateWeighbridgeData(weighbridgeData);
    }

    /**
     * 批量删除地磅数据处理
     * 
     * @param ids 需要删除的地磅数据处理主键
     * @return 结果
     */
    @Override
    public int deleteWeighbridgeDataByIds(Long[] ids)
    {
        return weighbridgeDataMapper.deleteWeighbridgeDataByIds(ids);
    }

    /**
     * 删除地磅数据处理信息
     * 
     * @param id 地磅数据处理主键
     * @return 结果
     */
    @Override
    public int deleteWeighbridgeDataById(Long id)
    {
        return weighbridgeDataMapper.deleteWeighbridgeDataById(id);
    }
}
