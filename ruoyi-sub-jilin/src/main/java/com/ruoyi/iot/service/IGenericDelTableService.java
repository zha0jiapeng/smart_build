package com.ruoyi.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

import com.ruoyi.iot.domain.GenericDelTable;

/**
 * 删除表Service接口
 * 
 * @author ruoyi
 * @date 2024-09-22
 */
public interface IGenericDelTableService  extends IService<GenericDelTable>
{
    /**
     * 查询删除表
     * 
     * @param id 删除表主键
     * @return 删除表
     */
    public GenericDelTable selectGenericDelTableById(Long id);

    /**
     * 查询删除表列表
     * 
     * @param genericDelTable 删除表
     * @return 删除表集合
     */
    public List<GenericDelTable> selectGenericDelTableList(GenericDelTable genericDelTable);

    /**
     * 新增删除表
     * 
     * @param genericDelTable 删除表
     * @return 结果
     */
    public int insertGenericDelTable(GenericDelTable genericDelTable);

    /**
     * 修改删除表
     * 
     * @param genericDelTable 删除表
     * @return 结果
     */
    public int updateGenericDelTable(GenericDelTable genericDelTable);

    /**
     * 批量删除删除表
     * 
     * @param ids 需要删除的删除表主键集合
     * @return 结果
     */
    public int deleteGenericDelTableByIds(Long[] ids);

    /**
     * 删除删除表信息
     * 
     * @param id 删除表主键
     * @return 结果
     */
    public int deleteGenericDelTableById(Long id);


    public void deleteOldRecords(String tableName, String dateColumn, int intervalMonths);
}
