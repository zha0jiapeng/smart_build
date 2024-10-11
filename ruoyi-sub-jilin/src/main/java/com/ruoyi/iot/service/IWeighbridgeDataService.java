package com.ruoyi.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.ruoyi.iot.domain.WeighbridgeData;

/**
 * 地磅数据处理Service接口
 * 
 * @author ruoyi
 * @date 2024-10-09
 */
public interface IWeighbridgeDataService  extends IService<WeighbridgeData>
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
     * 批量删除地磅数据处理
     * 
     * @param ids 需要删除的地磅数据处理主键集合
     * @return 结果
     */
    public int deleteWeighbridgeDataByIds(Long[] ids);

    /**
     * 删除地磅数据处理信息
     * 
     * @param id 地磅数据处理主键
     * @return 结果
     */
    public int deleteWeighbridgeDataById(Long id);
}
