package com.ruoyi.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.ruoyi.iot.domain.WeighbridgeData;

/**
 * 地磅数据处理Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-09
 */
public interface WeighbridgeDataMapper extends BaseMapper<WeighbridgeData>
{
    /**
     * 查询地磅数据处理
     * 
     * @param id 地磅数据处理主键
     * @return 地磅数据处理
     */
    public WeighbridgeData selectWeighbridgeDataById(Long id);

    /**
     * 查询地磅数据处理列表
     * 
     * @param weighbridgeData 地磅数据处理
     * @return 地磅数据处理集合
     */
    public List<WeighbridgeData> selectWeighbridgeDataList(WeighbridgeData weighbridgeData);

    /**
     * 新增地磅数据处理
     * 
     * @param weighbridgeData 地磅数据处理
     * @return 结果
     */
    public int insertWeighbridgeData(WeighbridgeData weighbridgeData);

    /**
     * 修改地磅数据处理
     * 
     * @param weighbridgeData 地磅数据处理
     * @return 结果
     */
    public int updateWeighbridgeData(WeighbridgeData weighbridgeData);

    /**
     * 删除地磅数据处理
     * 
     * @param id 地磅数据处理主键
     * @return 结果
     */
    public int deleteWeighbridgeDataById(Long id);

    /**
     * 批量删除地磅数据处理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWeighbridgeDataByIds(Long[] ids);
}
