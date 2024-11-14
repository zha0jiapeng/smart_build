package com.ruoyi.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.ruoyi.iot.domain.QReceivePhoto;

/**
 * 收料照片Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-13
 */
public interface QReceivePhotoMapper extends BaseMapper<QReceivePhoto>
{
    /**
     * 查询收料照片
     * 
     * @param orgId 收料照片主键
     * @return 收料照片
     */
    public QReceivePhoto selectQReceivePhotoByOrgId(String orgId);

    /**
     * 查询收料照片列表
     * 
     * @param qReceivePhoto 收料照片
     * @return 收料照片集合
     */
    public List<QReceivePhoto> selectQReceivePhotoList(QReceivePhoto qReceivePhoto);

    /**
     * 新增收料照片
     * 
     * @param qReceivePhoto 收料照片
     * @return 结果
     */
    public int insertQReceivePhoto(QReceivePhoto qReceivePhoto);

    /**
     * 修改收料照片
     * 
     * @param qReceivePhoto 收料照片
     * @return 结果
     */
    public int updateQReceivePhoto(QReceivePhoto qReceivePhoto);

    /**
     * 删除收料照片
     * 
     * @param orgId 收料照片主键
     * @return 结果
     */
    public int deleteQReceivePhotoByOrgId(String orgId);

    /**
     * 批量删除收料照片
     * 
     * @param orgIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQReceivePhotoByOrgIds(String[] orgIds);
}
