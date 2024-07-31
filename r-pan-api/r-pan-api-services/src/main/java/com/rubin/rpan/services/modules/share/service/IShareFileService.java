package com.rubin.rpan.services.modules.share.service;

import com.rubin.rpan.services.modules.file.vo.RPanUserFileVO;

import java.util.List;

/**
 * 项目分享文件业务处理接口
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
public interface IShareFileService {

    void saveBatch(Long shareId, String shareFileIds, Long userId);

    void cancelShareFiles(String shareIds);

    List<RPanUserFileVO> getShareFileInfos(Long shareId);

    List<Long> getShareIdByFileIds(String fileIds);

    List<RPanUserFileVO> getAllShareFileInfos(Long shareId);

    List<Long> getFileIdsByShareId(Long shareId);

}
