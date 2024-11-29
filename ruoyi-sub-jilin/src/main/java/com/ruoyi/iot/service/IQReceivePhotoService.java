package com.ruoyi.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.iot.domain.QReceiveMoreMaterial;
import com.ruoyi.iot.domain.QReceivePhoto;

/**
 * 收料照片Service接口
 * 
 * @author ruoyi
 * @date 2024-11-13
 */
public interface IQReceivePhotoService  extends IService<QReceivePhoto>
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
     * 批量删除收料照片
     * 
     * @param orgIds 需要删除的收料照片主键集合
     * @return 结果
     */
    public int deleteQReceivePhotoByOrgIds(String[] orgIds);

    /**
     * 删除收料照片信息
     * 
     * @param orgId 收料照片主键
     * @return 结果
     */
    public int deleteQReceivePhotoByOrgId(String orgId);


    @DataSource(value = DataSourceType.SLAVE)
    Map<String, String>  selectQReceivePhotoOrderIdSLAVE(String orderId);

    @DataSource(value = DataSourceType.SLAVEDATA)
    Map<String, String> selectQReceivePhotoOrderIdSLAVEDATA(String orderId);
}
