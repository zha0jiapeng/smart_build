package com.ruoyi.assessment.modules.user.book.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.assessment.core.api.dto.PagingReqDTO;
import com.ruoyi.assessment.modules.user.book.dto.UserBookDTO;
import com.ruoyi.assessment.modules.user.book.entity.UserBook;

/**
* <p>
* 错题本业务类
* </p>
*
* @author 聪明笨狗
* @since 2020-05-27 17:56
*/
public interface UserBookService extends IService<UserBook> {

    /**
    * 分页查询数据
    * @param reqDTO
    * @return
    */
    IPage<UserBookDTO> paging(PagingReqDTO<UserBookDTO> reqDTO);

    /**
     * 加入错题本
     * @param quId
     * @param examId
     */
    void addBook(String examId, String quId, String userId);

    /**
     * 查找第一个错题
     * @param quId
     * @param examId
     * @return
     */
    String findNext(String examId, String quId);
}
