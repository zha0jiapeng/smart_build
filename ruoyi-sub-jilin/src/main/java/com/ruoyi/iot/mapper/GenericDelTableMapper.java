package com.ruoyi.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.iot.domain.GenericDelTable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenericDelTableMapper  extends BaseMapper<GenericDelTable> {
    /**
     * @param table：表名
     * @param intervalMonths：月份
     * @return
     */
    @Delete("DELETE FROM ${tableName} WHERE DATE(${dateColumn}) < DATE_SUB(CURDATE(), INTERVAL #{intervalMonths} MONTH)")
    int deleteOldRecords(@Param("tableName") String table, @Param("dateColumn") String dateColumn, @Param("intervalMonths") int intervalMonths);

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
     * 删除删除表
     *
     * @param id 删除表主键
     * @return 结果
     */
    public int deleteGenericDelTableById(Long id);

    /**
     * 批量删除删除表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGenericDelTableByIds(Long[] ids);
}

