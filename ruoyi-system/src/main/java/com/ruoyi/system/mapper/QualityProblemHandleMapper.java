package com.ruoyi.system.mapper;

import com.ruoyi.system.entity.QualityProblemHandle;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * (QualityProblemHandle)表数据库访问层
 *
 * @author makejava
 * @since 2023-01-19 17:08:13
 */
public interface QualityProblemHandleMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    QualityProblemHandle queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param qualityProblemHandle 查询条件
     * @return 对象列表
     */
    List<QualityProblemHandle> queryAllByLimit(QualityProblemHandle qualityProblemHandle);

    /**
     * 统计总行数
     *
     * @param qualityProblemHandle 查询条件
     * @return 总行数
     */
    long count(QualityProblemHandle qualityProblemHandle);

    /**
     * 新增数据
     *
     * @param qualityProblemHandle 实例对象
     * @return 影响行数
     */
    int insert(QualityProblemHandle qualityProblemHandle);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<QualityProblemHandle> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<QualityProblemHandle> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<QualityProblemHandle> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<QualityProblemHandle> entities);

    /**
     * 修改数据
     *
     * @param qualityProblemHandle 实例对象
     * @return 影响行数
     */
    int update(QualityProblemHandle qualityProblemHandle);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    List<QualityProblemHandle> queryByProblemId(Integer id);
}

