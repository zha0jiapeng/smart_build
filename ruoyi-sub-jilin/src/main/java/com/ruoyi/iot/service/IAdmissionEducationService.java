package com.ruoyi.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.iot.domain.AdmissionEducation;


import java.util.List;

/**
 * 入场三级教育Service接口
 * 
 * @author mashir0
 * @date 2024-07-16
 */
public interface IAdmissionEducationService   extends IService<AdmissionEducation>
{
    /**
     * 查询入场三级教育
     * 
     * @param id 入场三级教育主键
     * @return 入场三级教育
     */
    public AdmissionEducation selectAdmissionEducationById(Long id);

    /**
     * 查询入场三级教育列表
     * 
     * @param admissionEducation 入场三级教育
     * @return 入场三级教育集合
     */
    public List<AdmissionEducation> selectAdmissionEducationList(AdmissionEducation admissionEducation);

    /**
     * 新增入场三级教育
     * 
     * @param admissionEducation 入场三级教育
     * @return 结果
     */
    public int insertAdmissionEducation(AdmissionEducation admissionEducation);

    /**
     * 修改入场三级教育
     * 
     * @param admissionEducation 入场三级教育
     * @return 结果
     */
    public int updateAdmissionEducation(AdmissionEducation admissionEducation);

    /**
     * 批量删除入场三级教育
     * 
     * @param ids 需要删除的入场三级教育主键集合
     * @return 结果
     */
    public int deleteAdmissionEducationByIds(Long[] ids);

    /**
     * 删除入场三级教育信息
     * 
     * @param id 入场三级教育主键
     * @return 结果
     */
    public int deleteAdmissionEducationById(Long id);
}
