package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.Supplier;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商Mapper接口
 * 
 * @author zcc
 */
public interface SupplierMapper extends BaseMapper<Supplier> {
    /**
     * 查询供应商列表
     *
     * @param supplier 供应商
     * @return 供应商集合
     */
    List<Supplier> selectByEntity(Supplier supplier);

    /**
     * 批量软删除
     * @param ids
     * @return
    */
    int updateDelFlagByIds(@Param("ids") Long[] ids);
}
