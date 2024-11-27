package com.ruoyi.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.iot.domain.QReceiveMoreMaterial;
import com.ruoyi.iot.domain.QReceivePhoto;
import com.ruoyi.iot.mapper.QReceivePhotoMapper;
import com.ruoyi.iot.service.IQReceivePhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 收料照片Service业务层处理
 *
 * @author ruoyi
 * @date 2024-11-13
 */
@Service
public class QReceivePhotoServiceImpl extends ServiceImpl<QReceivePhotoMapper, QReceivePhoto> implements IQReceivePhotoService {
    @Autowired(required = false)
    private QReceivePhotoMapper qReceivePhotoMapper;

    /**
     * 查询收料照片
     *
     * @param orgId 收料照片主键
     * @return 收料照片
     */
    @Override
    public QReceivePhoto selectQReceivePhotoByOrgId(String orgId) {
        return qReceivePhotoMapper.selectQReceivePhotoByOrgId(orgId);
    }

    /**
     * 查询收料照片列表
     *
     * @param qReceivePhoto 收料照片
     * @return 收料照片
     */
    @Override
    public List<QReceivePhoto> selectQReceivePhotoList(QReceivePhoto qReceivePhoto) {
        return qReceivePhotoMapper.selectQReceivePhotoList(qReceivePhoto);
    }

    /**
     * 新增收料照片
     *
     * @param qReceivePhoto 收料照片
     * @return 结果
     */
    @Override
    public int insertQReceivePhoto(QReceivePhoto qReceivePhoto) {
        return qReceivePhotoMapper.insertQReceivePhoto(qReceivePhoto);
    }

    /**
     * 修改收料照片
     *
     * @param qReceivePhoto 收料照片
     * @return 结果
     */
    @Override
    public int updateQReceivePhoto(QReceivePhoto qReceivePhoto) {
        return qReceivePhotoMapper.updateQReceivePhoto(qReceivePhoto);
    }

    /**
     * 批量删除收料照片
     *
     * @param orgIds 需要删除的收料照片主键
     * @return 结果
     */
    @Override
    public int deleteQReceivePhotoByOrgIds(String[] orgIds) {
        return qReceivePhotoMapper.deleteQReceivePhotoByOrgIds(orgIds);
    }

    /**
     * 删除收料照片信息
     *
     * @param orgId 收料照片主键
     * @return 结果
     */
    @Override
    public int deleteQReceivePhotoByOrgId(String orgId) {
        return qReceivePhotoMapper.deleteQReceivePhotoByOrgId(orgId);
    }


    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public Map<String, String>  selectQReceivePhotoOrderIdSLAVE(String orderId) {
        QueryWrapper<QReceivePhoto> queryWrapper = new QueryWrapper<>();
        // 使用in方法查询ID在列表中的记录
        queryWrapper.in("order_id", orderId);
        List<QReceivePhoto> list = qReceivePhotoMapper.selectList(queryWrapper);
        Map<String, String> map = mapPicture(list);
        return map;
    }

    @Override
    @DataSource(value = DataSourceType.SLAVEDATA)
    public Map<String, String> selectQReceivePhotoOrderIdSLAVEDATA(String orderId) {
        QueryWrapper<QReceivePhoto> queryWrapper = new QueryWrapper<>();
        // 使用in方法查询ID在列表中的记录
        queryWrapper.in("order_id", orderId);
        List<QReceivePhoto> list = qReceivePhotoMapper.selectList(queryWrapper);
        Map<String, String> map = mapPicture(list);
        return map;
    }

    public Map<String, String> mapPicture(List<QReceivePhoto> list){
        Map<String, String> map = new HashMap<>();
        for (QReceivePhoto value : list) {
            String photoType = value.getPhotoType();
            String cameraPosition = value.getCameraPosition();
            String localUrl = value.getLocalUrl();

            if ("车前".equals(cameraPosition)) {
                switch (photoType) {
                    case "入场":
                        map.put("carPicture", localUrl);
                        break;
                    case "出场":
                        map.put("boxPicture", localUrl);
                        break;
                }
            } else if ("车顶".equals(cameraPosition) && "入场".equals(photoType)) {
                map.put("goodsPicture", localUrl);
            }
        }
        return map;
    }
}
