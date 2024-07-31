package com.ruoyi.web.controller.basic.view.wms;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.InventoryHistory;
import com.ruoyi.system.mapper.convert.InventoryHistoryConvert;
import com.ruoyi.system.pojo.query.InventoryHistoryQuery;
import com.ruoyi.system.pojo.vo.InventoryHistoryVO;
import com.ruoyi.system.service.impl.InventoryHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 库存记录Controller
 *
 * @author zcc
 * @date 2022-08-05
 */
@Slf4j
@Api(description = "库存记录接口列表")
@RestController
@RequestMapping("/wms/inventoryHistory")
public class InventoryHistoryController extends BaseController {
    @Autowired
    private InventoryHistoryService service;
    @Autowired
    private InventoryHistoryConvert convert;

    @ApiOperation("查询库存记录列表")
    @PreAuthorize("@ss.hasPermi('wms:inventoryHistory:list')")
    @PostMapping("/list")
    public ResponseEntity<Page<InventoryHistoryVO>> list(@RequestBody InventoryHistoryQuery query, Pageable page) {
        return ResponseEntity.ok(service.selectList(query, page));
    }

//    @ApiOperation("导出库存记录列表")
//    @PreAuthorize("@ss.hasPermi('wms:inventoryHistory:export')")
//    @Log(title = "库存记录", businessType = BusinessType.EXPORT)
//    @GetMapping("/export")
//    public ResponseEntity<String> export(InventoryHistoryQuery query) {
//        List<InventoryHistoryVO> list = service.selectList(query);
//        ExcelUtil<InventoryHistoryVO> util = new ExcelUtil<>(InventoryHistoryVO.class);
//        return ResponseEntity.ok(util.writeExcel(list, "库存记录数据"));
//    }

    @Log(title = "导出库存记录列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody InventoryHistoryQuery query) {
        List<InventoryHistoryVO> list = service.selectList(query);
        ExcelUtil<InventoryHistoryVO> util = new ExcelUtil<>(InventoryHistoryVO.class);
        log.info("导出库存记录列表 结果:{}", JSON.toJSON(list));
        util.exportExcels(response,list, "库存记录数据");
    }

    @ApiOperation("获取库存记录详细信息")
    @PreAuthorize("@ss.hasPermi('wms:inventoryHistory:query')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<InventoryHistory> getInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.selectById(id));
    }

    @ApiOperation("新增库存记录")
    @PreAuthorize("@ss.hasPermi('wms:inventoryHistory:add')")
    @Log(title = "库存记录", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<Integer> add(@RequestBody InventoryHistory inventoryHistory) {
        return ResponseEntity.ok(service.insert(inventoryHistory));
    }

    @ApiOperation("修改库存记录")
    @PreAuthorize("@ss.hasPermi('wms:inventoryHistory:edit')")
    @Log(title = "库存记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public ResponseEntity<Integer> edit(@RequestBody InventoryHistory inventoryHistory) {
        return ResponseEntity.ok(service.update(inventoryHistory));
    }

    @ApiOperation("删除库存记录")
    @PreAuthorize("@ss.hasPermi('wms:inventoryHistory:remove')")
    @Log(title = "库存记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public ResponseEntity<Integer> remove(@PathVariable Long[] ids) {
        return ResponseEntity.ok(service.deleteByIds(ids));
    }
}
