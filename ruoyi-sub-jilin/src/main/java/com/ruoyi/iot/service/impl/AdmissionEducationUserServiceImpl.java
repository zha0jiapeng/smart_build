package com.ruoyi.iot.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.AdmissionEducationUser;
import com.ruoyi.iot.mapper.AdmissionEducationUserMapper;
import com.ruoyi.iot.service.IAdmissionEducationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


/**
 * 入场三级教育用户Service业务层处理
 *
 * @author mashir0
 * @date 2024-07-16
 */
@Service
public class AdmissionEducationUserServiceImpl extends ServiceImpl<AdmissionEducationUserMapper, AdmissionEducationUser> implements IAdmissionEducationUserService {

    @Autowired
    private AdmissionEducationUserMapper admissionEducationUserMapper;

    /**
     * 查询入场三级教育用户
     *
     * @param id 入场三级教育用户主键
     * @return 入场三级教育用户
     */
    @Override
    public AdmissionEducationUser selectAdmissionEducationUserById(Long id) {
        return admissionEducationUserMapper.selectById(id);
    }

    /**
     * 查询入场三级教育用户列表
     *
     * @param admissionEducationUser 入场三级教育用户
     * @return 入场三级教育用户
     */
    @Override
    public List<AdmissionEducationUser> selectAdmissionEducationUserList(AdmissionEducationUser admissionEducationUser) {
        return admissionEducationUserMapper.selectList(new LambdaQueryWrapper<>());
    }

    /**
     * 新增入场三级教育用户
     *
     * @param admissionEducationUser 入场三级教育用户
     * @return 结果
     */
    @Override
    public int insertAdmissionEducationUser(AdmissionEducationUser admissionEducationUser) {
        admissionEducationUser.setCreateTime(DateUtils.getNowDate());
        return admissionEducationUserMapper.insert(admissionEducationUser);
    }

    /**
     * 修改入场三级教育用户
     *
     * @param admissionEducationUser 入场三级教育用户
     * @return 结果
     */
    @Override
    public int updateAdmissionEducationUser(AdmissionEducationUser admissionEducationUser) {
        admissionEducationUser.setUpdateTime(DateUtils.getNowDate());
        return admissionEducationUserMapper.updateById(admissionEducationUser);
    }

    /**
     * 批量删除入场三级教育用户
     *
     * @param ids 需要删除的入场三级教育用户主键
     * @return 结果
     */
    @Override
    public int deleteAdmissionEducationUserByIds(Long[] ids) {
        return admissionEducationUserMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除入场三级教育用户信息
     *
     * @param id 入场三级教育用户主键
     * @return 结果
     */
    @Override
    public int deleteAdmissionEducationUserById(Long id) {
        return admissionEducationUserMapper.deleteById(id);
    }
}
