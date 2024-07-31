package com.rubin.rpan.services.modules.recycle.controller;

import com.rubin.rpan.services.common.annotation.NeedLogin;
import com.rubin.rpan.services.common.response.R;
import com.rubin.rpan.util.UserIdUtil;
import com.rubin.rpan.services.modules.file.vo.RPanUserFileVO;
import com.rubin.rpan.services.modules.recycle.po.RecycleBinDeletePO;
import com.rubin.rpan.services.modules.recycle.po.RestorePO;
import com.rubin.rpan.services.modules.recycle.service.IRecycleBinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目回收站相关rest接口返回
 * Created by RubinChu on 2021/1/22 下午 4:11
 */
@RestController
@Validated
@Api(tags = "回收站")
public class RecycleBinController {

    @Autowired
    @Qualifier(value = "recycleBinService")
    private IRecycleBinService iRecycleBinService;

    /**
     * 获取回收站的文件列表
     *
     * @return
     */
    @ApiOperation(
            value = "获取回收站的文件列表",
            notes = "该接口提供了获取回收站的文件列表的功能",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @GetMapping("recycles")
    @NeedLogin
    public R<List<RPanUserFileVO>> list() {
        return R.data(iRecycleBinService.list(UserIdUtil.get()));
    }

    /**
     * 还原文件
     *
     * @param restorePO
     * @return
     */
    @ApiOperation(
            value = "还原文件",
            notes = "该接口提供了还原文件的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @PutMapping("recycle/restore")
    @NeedLogin
    public R restore(@Validated @RequestBody RestorePO restorePO) {
        iRecycleBinService.restore(restorePO.getFileIds(), UserIdUtil.get());
        return R.success();
    }

    /**
     * 回收站删除文件
     *
     * @param recycleBinDeletePO
     * @return
     */
    @ApiOperation(
            value = "回收站删除文件",
            notes = "该接口提供了回收站删除文件的功能",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    @DeleteMapping("recycle")
    @NeedLogin
    public R delete(@Validated @RequestBody RecycleBinDeletePO recycleBinDeletePO) {
        iRecycleBinService.delete(recycleBinDeletePO.getFileIds(), UserIdUtil.get());
        return R.success();
    }

}
