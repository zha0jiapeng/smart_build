package com.ruoyi.iot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.GenericDelTable;
import com.ruoyi.iot.mapper.GenericDelTableMapper;
import com.ruoyi.iot.service.IGenericDelTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 删除表Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-22
 */
@Service
public class GenericDelTableServiceImpl extends ServiceImpl<GenericDelTableMapper, GenericDelTable> implements IGenericDelTableService {
    @Autowired(required = false)
    private GenericDelTableMapper genericDelTableMapper;

    /**
     * 查询删除表
     *
     * @param id 删除表主键
     * @return 删除表
     */
    @Override
    public GenericDelTable selectGenericDelTableById(Long id) {
        return genericDelTableMapper.selectGenericDelTableById(id);
    }

    /**
     * 查询删除表列表
     *
     * @param genericDelTable 删除表
     * @return 删除表
     */
    @Override
    public List<GenericDelTable> selectGenericDelTableList(GenericDelTable genericDelTable) {
        return genericDelTableMapper.selectGenericDelTableList(genericDelTable);
    }

    /**
     * 新增删除表
     *
     * @param genericDelTable 删除表
     * @return 结果
     */
    @Override
    public int insertGenericDelTable(GenericDelTable genericDelTable) {
        genericDelTable.setCreateTime(DateUtils.getNowDate());
        return genericDelTableMapper.insertGenericDelTable(genericDelTable);
    }

    /**
     * 修改删除表
     *
     * @param genericDelTable 删除表
     * @return 结果
     */
    @Override
    public int updateGenericDelTable(GenericDelTable genericDelTable) {
        genericDelTable.setUpdateTime(DateUtils.getNowDate());
        return genericDelTableMapper.updateGenericDelTable(genericDelTable);
    }

    /**
     * 批量删除删除表
     *
     * @param ids 需要删除的删除表主键
     * @return 结果
     */
    @Override
    public int deleteGenericDelTableByIds(Long[] ids) {
        return genericDelTableMapper.deleteGenericDelTableByIds(ids);
    }

    /**
     * 删除删除表信息
     *
     * @param id 删除表主键
     * @return 结果
     */
    @Override
    public int deleteGenericDelTableById(Long id) {
        return genericDelTableMapper.deleteGenericDelTableById(id);
    }

    @Override
    public void deleteOldRecords(String tableName, String dateColumn, int intervalMonths) {
        genericDelTableMapper.deleteOldRecords(tableName, dateColumn, intervalMonths);
    }
}
