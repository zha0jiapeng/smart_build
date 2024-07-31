package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.ShipmentOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出库单Mapper接口
 * 
 * @author zcc
 */
public interface ShipmentOrderMapper extends BaseMapper<ShipmentOrder> {
    /**
     * 查询出库单列表
     *
     * @param shipmentOrder 出库单
     * @return 出库单集合
     */
    List<ShipmentOrder> selectByEntity(ShipmentOrder shipmentOrder);

    /**
     * 批量软删除
     * @param ids
     * @return
    */
    int updateDelFlagByIds(@Param("ids") Long[] ids);
}
