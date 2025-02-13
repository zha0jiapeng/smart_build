package com.ruoyi.system.mapper;

import com.ruoyi.system.entity.InspectionProject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (InspectionProject)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-25 17:10:49
 */
public interface InspectionProjectMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    InspectionProject queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param inspectionProject 查询条件
     * @return 对象列表
     */
    List<InspectionProject> queryAllByLimit(InspectionProject inspectionProject);

    /**
     * 统计总行数
     *
     * @param inspectionProject 查询条件
     * @return 总行数
     */
    long count(InspectionProject inspectionProject);

    /**
     * 新增数据
     *
     * @param inspectionProject 实例对象
     * @return 影响行数
     */
    int insert(InspectionProject inspectionProject);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<InspectionProject> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<InspectionProject> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<InspectionProject> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<InspectionProject> entities);

    /**
     * 修改数据
     *
     * @param inspectionProject 实例对象
     * @return 影响行数
     */
    int update(InspectionProject inspectionProject);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

