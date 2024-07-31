package com.ruoyi.system.mapper;

import com.ruoyi.system.entity.InspectionPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (InspectionPlan)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-25 17:09:56
 */
public interface InspectionPlanMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    InspectionPlan queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param inspectionPlan 查询条件
     * @return 对象列表
     */
    List<InspectionPlan> queryAllByLimit(InspectionPlan inspectionPlan);

    /**
     * 统计总行数
     *
     * @param inspectionPlan 查询条件
     * @return 总行数
     */
    long count(InspectionPlan inspectionPlan);

    /**
     * 新增数据
     *
     * @param inspectionPlan 实例对象
     * @return 影响行数
     */
    int insert(InspectionPlan inspectionPlan);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<InspectionPlan> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<InspectionPlan> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<InspectionPlan> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<InspectionPlan> entities);

    /**
     * 修改数据
     *
     * @param inspectionPlan 实例对象
     * @return 影响行数
     */
    int update(InspectionPlan inspectionPlan);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}

