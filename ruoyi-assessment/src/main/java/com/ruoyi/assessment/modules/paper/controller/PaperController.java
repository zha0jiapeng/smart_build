package com.ruoyi.assessment.modules.paper.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.assessment.core.api.ApiRest;
import com.ruoyi.assessment.core.api.controller.BaseController;
import com.ruoyi.assessment.core.api.dto.BaseIdReqDTO;
import com.ruoyi.assessment.core.api.dto.BaseIdRespDTO;
import com.ruoyi.assessment.core.api.dto.BaseIdsReqDTO;
import com.ruoyi.assessment.core.api.dto.PagingReqDTO;
import com.ruoyi.assessment.core.utils.BeanMapper;
import com.ruoyi.assessment.core.utils.StringUtils;
import com.ruoyi.assessment.modules.paper.dto.PaperDTO;
import com.ruoyi.assessment.modules.paper.dto.ext.PaperQuDetailDTO;
import com.ruoyi.assessment.modules.paper.dto.request.PaperAnswerDTO;
import com.ruoyi.assessment.modules.paper.dto.request.PaperCreateReqDTO;
import com.ruoyi.assessment.modules.paper.dto.request.PaperListReqDTO;
import com.ruoyi.assessment.modules.paper.dto.request.PaperQuQueryDTO;
import com.ruoyi.assessment.modules.paper.dto.response.ExamDetailRespDTO;
import com.ruoyi.assessment.modules.paper.dto.response.ExamResultRespDTO;
import com.ruoyi.assessment.modules.paper.dto.response.PaperListRespDTO;
import com.ruoyi.assessment.modules.paper.entity.Paper;
import com.ruoyi.assessment.modules.paper.service.PaperService;
import com.ruoyi.common.enums.ExamTypeEnum;
import com.ruoyi.common.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 试卷控制器
 * </p>
 *
 * @author 聪明笨狗
 * @since 2020-05-25 16:33
 */
@Api(tags = {"试卷"})
@RestController
@RequestMapping("/exam/api/paper/paper")
public class PaperController extends BaseController {

    @Autowired
    private PaperService baseService;

    /**
     * 添加或修改
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "添加或修改")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ApiRest<BaseIdRespDTO> save(@RequestBody PaperDTO reqDTO) {
        //复制参数
        Paper entity = new Paper();
        BeanMapper.copy(reqDTO, entity);
        baseService.saveOrUpdate(entity);
        return super.success(new BaseIdRespDTO(entity.getId()));
    }

    /**
     * 批量删除
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "批量删除")
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ApiRest edit(@RequestBody BaseIdsReqDTO reqDTO) {
        //根据ID删除
        baseService.removeByIds(reqDTO.getIds());
        return super.success();
    }

    /**
     * 查找详情
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "查找详情")
    @RequestMapping(value = "/detail", method = {RequestMethod.POST})
    public ApiRest<PaperDTO> find(@RequestBody BaseIdReqDTO reqDTO) {
        Paper entity = baseService.getById(reqDTO.getId());
        PaperDTO dto = new PaperDTO();
        BeanUtils.copyProperties(entity, dto);
        return super.success(dto);
    }


    /**
     * 分页查找
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "分页查找")
    @RequestMapping(value = "/paging", method = {RequestMethod.POST})
    public ApiRest<IPage<PaperListRespDTO>> paging(@RequestBody PagingReqDTO<PaperListReqDTO> reqDTO) {

        //分页查询并转换
        IPage<PaperListRespDTO> page = baseService.paging(reqDTO);

        return super.success(page);
    }


    /**
     * 创建试卷
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @ApiOperation(value = "创建试卷")
    @RequestMapping(value = "/create-paper", method = {RequestMethod.POST})
    public ApiRest<BaseIdRespDTO> save(@RequestBody PaperCreateReqDTO reqDTO) {
        String userId = "";
        Integer type = ExamTypeEnum.PLATFORM.getCode();
        if (StringUtils.isBlank(reqDTO.getCard())) {
            userId = SecurityUtils.getUserId().toString();
        } else {
            userId = reqDTO.getCard();
            type = ExamTypeEnum.STAFF.getCode();
        }
        String paperId = baseService.createPaper(userId, reqDTO.getExamId(), type);
        return super.success(new BaseIdRespDTO(paperId));
    }

    /**
     * 批量删除
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "试卷详情")
    @RequestMapping(value = "/paper-detail", method = {RequestMethod.POST})
    public ApiRest<ExamDetailRespDTO> paperDetail(@RequestBody BaseIdReqDTO reqDTO) {
        //根据ID删除
        ExamDetailRespDTO respDTO = baseService.paperDetail(reqDTO.getId());
        return super.success(respDTO);
    }

    /**
     * 批量删除
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "试题详情")
    @RequestMapping(value = "/qu-detail", method = {RequestMethod.POST})
    public ApiRest<PaperQuDetailDTO> quDetail(@RequestBody PaperQuQueryDTO reqDTO) {
        //根据ID删除
        PaperQuDetailDTO respDTO = baseService.findQuDetail(reqDTO.getPaperId(), reqDTO.getQuId());
        return super.success(respDTO);
    }

    /**
     * 填充答案
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "填充答案")
    @RequestMapping(value = "/fill-answer", method = {RequestMethod.POST})
    public ApiRest<PaperQuDetailDTO> fillAnswer(@RequestBody PaperAnswerDTO reqDTO) {
        //根据ID删除
        baseService.fillAnswer(reqDTO);
        return super.success();
    }


    /**
     * 交卷操作
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "交卷操作")
    @RequestMapping(value = "/hand-exam", method = {RequestMethod.POST})
    public ApiRest<PaperQuDetailDTO> handleExam(@RequestBody BaseIdReqDTO reqDTO) {
        //根据ID删除
        baseService.handExam(reqDTO.getId());
        return super.success();
    }


    /**
     * 批量删除
     *
     * @param reqDTO
     * @return
     */
    @ApiOperation(value = "试卷详情")
    @RequestMapping(value = "/paper-result", method = {RequestMethod.POST})
    public ApiRest<ExamResultRespDTO> paperResult(@RequestBody BaseIdReqDTO reqDTO) {
        //根据ID删除
        ExamResultRespDTO respDTO = baseService.paperResult(reqDTO.getId());
        return super.success(respDTO);
    }


    /**
     * 检测用户有没有中断的考试
     *
     * @return
     */
    @ApiOperation(value = "检测进行中的考试")
    @RequestMapping(value = "/check-process", method = {RequestMethod.POST})
    public ApiRest<PaperDTO> checkProcess(@RequestBody PaperCreateReqDTO reqDTO) {
        String userId = "";
        if (StringUtils.isBlank(reqDTO.getCard())) {
            userId = SecurityUtils.getUserId().toString();
        } else {
            userId = reqDTO.getCard();
        }
        //复制参数
        PaperDTO dto = baseService.checkProcess(userId);
        return super.success(dto);
    }
}
