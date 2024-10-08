package com.ruoyi.assessment.modules.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.assessment.modules.exam.dto.ExamDTO;
import com.ruoyi.assessment.modules.exam.dto.response.ExamOnlineRespDTO;
import com.ruoyi.assessment.modules.exam.dto.response.ExamReviewRespDTO;
import com.ruoyi.assessment.modules.exam.entity.Exam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>
* 考试Mapper
* </p>
*
* @author 聪明笨狗
* @since 2020-07-25 16:18
*/
public interface ExamMapper extends BaseMapper<Exam> {

    /**
     * 查找分页内容
     * @param page
     * @param query
     * @return
     */
    IPage<ExamDTO> paging(Page page, @Param("query") ExamDTO query);

    /**
     * 查找分页内容
     * @param page
     * @param query
     * @return
     */
    IPage<ExamReviewRespDTO> reviewPaging(Page page, @Param("query") ExamDTO query);

    /**
     * 在线考试分页响应类-考生视角
     * @param page
     * @param query
     * @return
     */
    IPage<ExamOnlineRespDTO> online(Page page, @Param("query") ExamDTO query);

    /**
     * 在线考试分页响应类-考生视角 (根据id精确查询)
     * @param query 参数;
     * @return 结果;
     */
    List<ExamOnlineRespDTO> onLineOne(@Param("query") ExamDTO query);

}
