package com.ruoyi.iot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.config.MinioConfig;
import com.ruoyi.common.utils.MinioUtils;
import com.ruoyi.iot.domain.QReceive;
import com.ruoyi.iot.domain.QReceiveMoreMaterial;
import com.ruoyi.iot.domain.QReceivePhoto;
import com.ruoyi.iot.mapper.WeighbridgeDataMapper;
import com.ruoyi.iot.scheduling.access.ComprehensiveApp;
import com.ruoyi.iot.scheduling.access.FTPServerConfig;
import com.ruoyi.iot.service.IQReceiveMoreMaterialService;
import com.ruoyi.iot.service.IQReceivePhotoService;
import com.ruoyi.iot.service.IQReceiveService;
import com.ruoyi.iot.utils.HdyHttpUtils;
import com.ruoyi.system.domain.basic.Rain;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.iot.domain.WeighbridgeData;
import com.ruoyi.iot.service.IWeighbridgeDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 地磅数据处理Controller
 *
 * @author ruoyi
 * @date 2024-10-09
 */
@RestController
@RequestMapping("/system/data")
@Api(tags = {"地磅数据处理 Controller"})
public class WeighbridgeDataController extends BaseController {
    @Autowired
    private IWeighbridgeDataService weighbridgeDataService;

    @Autowired
    private IQReceiveMoreMaterialService qReceiveMoreMaterialService;

    @Autowired
    private IQReceivePhotoService iqReceivePhotoService;

    @Autowired
    private IQReceiveService qReceiveService;

    @Autowired
    private WeighbridgeDataMapper weighbridgeDataMapper;

    @Resource
    HdyHttpUtils hdyHttpUtils;

    @Resource
    MinioUtils minioUtils;

    @Resource
    MinioConfig minioConfig;

    /**
     * 查询地磅数据处理列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:list')")
    @GetMapping("/list")
    @ApiOperation("查询地磅数据处理列表")
    public TableDataInfo list(WeighbridgeData weighbridgeData) {
        startPage();
        List<WeighbridgeData> list = weighbridgeDataService.selectWeighbridgeDataList(weighbridgeData);
        return getDataTable(list);
    }

    /**
     * 导出地磅数据处理列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:export')")
    @Log(title = "地磅数据处理", businessType = BusinessType.EXPORT)
    @ApiOperation("导出地磅数据处理列表Excel")
    @PostMapping("/export")
    public void export(HttpServletResponse response, WeighbridgeData weighbridgeData) {
        List<WeighbridgeData> list = weighbridgeDataService.selectWeighbridgeDataList(weighbridgeData);
        ExcelUtil<WeighbridgeData> util = new ExcelUtil<WeighbridgeData>(WeighbridgeData.class);
        util.exportExcel(response, list, "地磅数据处理数据");
    }

    /**
     * 获取地磅数据处理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:data:query')")
    @ApiOperation("获取地磅数据处理详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(name = "id", value = "地磅数据处理参数", required = true)
                              @PathVariable("id") Long id) {
        return success(weighbridgeDataService.selectWeighbridgeDataById(id));
    }

    /**
     * 新增地磅数据处理
     */
    @PreAuthorize("@ss.hasPermi('system:data:add')")
    @Log(title = "地磅数据处理", businessType = BusinessType.INSERT)
    @ApiOperation("新增地磅数据处理")
    @PostMapping
    public AjaxResult add(@RequestBody WeighbridgeData weighbridgeData) {
        return toAjax(weighbridgeDataService.insertWeighbridgeData(weighbridgeData));
    }

    /**
     * 修改地磅数据处理
     */
    @PreAuthorize("@ss.hasPermi('system:data:edit')")
    @Log(title = "地磅数据处理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改地磅数据处理")
    @PutMapping
    public AjaxResult edit(@RequestBody WeighbridgeData weighbridgeData) {
        return toAjax(weighbridgeDataService.updateWeighbridgeData(weighbridgeData));
    }

    /**
     * 删除地磅数据处理
     */
    @PreAuthorize("@ss.hasPermi('system:data:remove')")
    @Log(title = "地磅数据处理", businessType = BusinessType.DELETE)
    @ApiOperation("删除地磅数据处理")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(name = "ids", value = "地磅数据处理ids参数", required = true)
                             @PathVariable Long[] ids) {
        return toAjax(weighbridgeDataService.deleteWeighbridgeDataByIds(ids));
    }


    @GetMapping("/upload")
    @ApiOperation("上传地磅数据")
    public void weighbridgeDataUpload() throws FileNotFoundException {
        Map<String, List<QReceive>> selectQReceiveList = qReceiveService.selectQReceiveList();
        Map<String, List<QReceive>> listMap = filterQReceiveList(selectQReceiveList);
        Map<String, List<QReceiveMoreMaterial>> selectQReceiveMoreMaterialList = qReceiveMoreMaterialService.selectQReceiveMoreMaterialList(listMap);
        List<QReceive> SLAVEselectQReceiveList = listMap.get("SLAVE");
        List<QReceive> SLAVEDATAselectQReceiveList = listMap.get("SLAVEDATA");
        List<QReceiveMoreMaterial> SLAVEselectQReceiveMoreMaterialList = selectQReceiveMoreMaterialList.get("SLAVE");
        List<QReceiveMoreMaterial> SLAVEDATAselectQReceiveMoreMaterialList = selectQReceiveMoreMaterialList.get("SLAVEDATA");
        if (SLAVEselectQReceiveList != null && SLAVEselectQReceiveList.size() != 0) {
            for (int i = 0; i < SLAVEselectQReceiveList.size(); i++) {
                QReceive qReceive = SLAVEselectQReceiveList.get(i);
                QReceiveMoreMaterial qReceiveMoreMaterial = SLAVEselectQReceiveMoreMaterialList.get(i);
                QReceivePhoto qReceivePhoto = iqReceivePhotoService.selectQReceivePhotoOrderIdSLAVE(qReceiveMoreMaterial.getOrderId());
                upload("SLAVE", qReceive, qReceiveMoreMaterial, qReceivePhoto);
            }
        }
        if (SLAVEDATAselectQReceiveList != null && SLAVEDATAselectQReceiveList.size() != 0) {
            for (int i = 0; i < SLAVEDATAselectQReceiveList.size(); i++) {
                QReceive qReceive = SLAVEDATAselectQReceiveList.get(i);
                QReceiveMoreMaterial qReceiveMoreMaterial = SLAVEDATAselectQReceiveMoreMaterialList.get(i);
                QReceivePhoto qReceivePhoto = iqReceivePhotoService.selectQReceivePhotoOrderIdSLAVEDATA(qReceiveMoreMaterial.getOrderId());
                upload("SLAVEDATA", qReceive, qReceiveMoreMaterial, qReceivePhoto);
            }
        }
    }

    public void upload(String region, QReceive qReceive, QReceiveMoreMaterial qReceiveMoreMaterial, QReceivePhoto qReceivePhoto) throws FileNotFoundException {
        Map<String, Object> valueMap = new HashMap<>();
        //门户ID  String
        valueMap.put("portal_id", "1751847977770553345");
        //子工程ID  String
        valueMap.put("sub_project_id", "1801194524869922817");
        WeighbridgeData weighbridgeData = new WeighbridgeData();
        //设备编号  String
        if (region.equals("SLAVE")) {
            valueMap.put("device_code", "DS-7804N-F1(D)0420240822CCRRFM5850847WVU");
            weighbridgeData.setDeviceCode("DS-7804N-F1(D)0420240822CCRRFM5850847WVU");
        } else {
            valueMap.put("device_code", "DS-7804N-F1(D)0420240416CCRRFD0327551WVU");
            weighbridgeData.setDeviceCode("DS-7804N-F1(D)0420240416CCRRFD0327551WVU");
        }


        //设备工作状态  String
        valueMap.put("work_status", "正常");
        valueMap.put("license_number", qReceive.getPlateNumber());
        weighbridgeData.setLicenseNumber(qReceive.getPlateNumber());
        valueMap.put("car_picture", "");
        valueMap.put("box_picture", "");
        valueMap.put("one_weight_time", qReceive.getEnterTime());
        weighbridgeData.setOneWeightTime(qReceive.getEnterTime());
        valueMap.put("two_weight_time", qReceive.getExitTime());
        weighbridgeData.setTwoWeightTime(qReceive.getExitTime());
        valueMap.put("goods_type", "");
        valueMap.put("actual_weight", qReceiveMoreMaterial.getMainNetQuantity());
        weighbridgeData.setActualWeight(qReceiveMoreMaterial.getMainNetQuantity().toString());
        valueMap.put("deduct_weight", qReceive.getDeductQuantity());
        weighbridgeData.setDeductWeight(qReceive.getDeductQuantity().toString());
        valueMap.put("suttle_weight", qReceive.getAuxiliaryNetQuantity());
        weighbridgeData.setSuttleWeight(qReceive.getAuxiliaryNetQuantity().toString());
        valueMap.put("rough_weight", qReceive.getRoughQuantity());
        weighbridgeData.setRoughWeight(qReceive.getRoughQuantity().toString());
        valueMap.put("tare", qReceive.getTareQuantity());
        weighbridgeData.setTare(qReceive.getTareQuantity().toString());
        valueMap.put("in_weight_time", qReceive.getEnterTime());
        weighbridgeData.setInWeightTime(qReceive.getEnterTime());
        valueMap.put("out_weight_time", qReceive.getExitTime());
        weighbridgeData.setOutWeightTime(qReceive.getExitTime());
        valueMap.put("order_code", qReceive.getOrderCode());
        weighbridgeData.setOrderCode(qReceive.getOrderCode());
        valueMap.put("goods_picture", "");
        valueMap.put("order_number", "");
        valueMap.put("deviation_number", "");
        valueMap.put("deviation_number_up", "");
        valueMap.put("deviation_number_down", "");
        valueMap.put("data_time", qReceive.getExitTime());
        String timestampStr = "";
        try {
            // 创建 Date 对象
            Date date = new Date();
            // 定义日期时间格式化器
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 格式化 Date 为字符串
            timestampStr = formatter.format(date);
        } catch (NumberFormatException e) {
            System.out.println("Invalid timestamp format: " + e.getMessage());
        }
        valueMap.put("push_time", timestampStr);
        valueMap.put("other", "");
        valueMap.put("weight_time", "");
        valueMap.put("weight_picture", getPicture(region, qReceivePhoto));
        valueMap.put("waring_type", "");
        valueMap.put("data_type", "0");
        valueMap.put("driver_name", "");
        valueMap.put("driver_phone", "");


        List<Map<String, Object>> values = new ArrayList<>();
        values.add(valueMap);
        Map<String, List<Map<String, Object>>> param = new HashMap<>();
        param.put("values", values);
        //开始插入数据库
        weighbridgeData.setRegion(region);
        weighbridgeData.setOrderId(qReceive.getOrderId());
        weighbridgeDataService.insertWeighbridgeData(weighbridgeData);
        hdyHttpUtils.pushIOT(param, "c65c1c39-2f36-43c1-b231-b7b8737cefcf");
    }

    public String getPicture(String region, QReceivePhoto qReceivePhoto) throws FileNotFoundException {
        if (qReceivePhoto == null) {
            return "空";
        }
        FTPServerConfig ftpServerConfig = null;
        String localUrl = qReceivePhoto.getLocalUrl();
        localUrl = extractCameraPath(localUrl);
        if (localUrl == null){
            return "空";
        }
        if (region.equals("SLAVE")) {
            ftpServerConfig = new FTPServerConfig("10.1.3.175", 21, "yuancheng15", "123456", localUrl, 1);
        } else {
            ftpServerConfig = new FTPServerConfig("10.1.3.181", 21, "yuancheng14", "123456", localUrl, 1);
        }
        //下载图片
        ComprehensiveApp comprehensiveApp = new ComprehensiveApp();
        String localFilePath = "/home/mashir0/images/picture.jpg";
        comprehensiveApp.processFTPServer(ftpServerConfig, localFilePath);

        //上传到minio
        InputStream inputStream = new FileInputStream(localFilePath);
        String filename = UUID.randomUUID().toString() + ".png";
        minioUtils.uploadFile(minioConfig.getWeighbridgeDataBucketName(), filename, inputStream);
        String imageFile = minioConfig.getEndpoint() + "/" + minioConfig.getWeighbridgeDataBucketName() + "/" + filename;
        System.out.println("地磅imageFile：" + imageFile);
        return hdyHttpUtils.pushPicture(imageFile);
    }

    public String extractCameraPath(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        // 将路径按 "/" 分隔
        String[] parts = path.split("/");

        // 查找 "camera" 并获取其后的部分
        for (int i = 0; i < parts.length; i++) {
            if ("camera".equals(parts[i])) {
                // 拼接 "camera" 后面的所有部分
                return "/" + String.join("/", java.util.Arrays.copyOfRange(parts, i + 1, parts.length));
            }
        }

        // 如果没有找到 "camera"，可以返回 null 或相应的消息
        return null;
    }

    //用来查询当前存储的数据库有没有重复的，根据orderId去重
    public Map<String, List<QReceive>> filterQReceiveList(Map<String, List<QReceive>> selectQReceiveList) {
        Map<String, List<QReceive>> listMap = selectQReceiveList;

        // 查询表中的 (orderId, ab) 组合
        Set<String> keysIn = getKeysFrom();
        // 过滤掉 aaa 中已经存在的 (orderId, ab)
        List<QReceive> filteredSlave = listMap.get("SLAVE").stream()
                .filter(qReceive -> !keysIn.contains(qReceive.getOrderId() + "_" + "SLAVE"))
                .collect(Collectors.toList());

        List<QReceive> filteredSlaveData = listMap.get("SLAVEDATA").stream()
                .filter(qReceive -> !keysIn.contains(qReceive.getOrderId() + "_" + "SLAVEDATA"))
                .collect(Collectors.toList());

        // 构建结果
        Map<String, List<QReceive>> filteredListMap = new HashMap<>();
        filteredListMap.put("SLAVE", filteredSlave);
        filteredListMap.put("SLAVEDATA", filteredSlaveData);


        return filteredListMap;
    }

    private Set<String> getKeysFrom() {
        Instant now = Instant.now();
        //每五分钟调用一次接口，每次查十分钟的数据
        Instant tenMinutesAgo = now.minusSeconds(600);
        QueryWrapper<WeighbridgeData> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("created_date", Timestamp.from(tenMinutesAgo), Timestamp.from(now));
        List<WeighbridgeData> list = weighbridgeDataMapper.selectList(queryWrapper);
        // 假设这里是从表中查询所有 (orderId, ab) 组合的逻辑
        Set<String> keys = list.stream()
                .map(record -> record.getOrderId() + "_" + record.getRegion())
                .collect(Collectors.toSet());
        return keys;
    }


    @GetMapping("/getLatestWeighbridgeData/15")
    public AjaxResult getLatestWeighbridgeData15() {
        QueryWrapper<WeighbridgeData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_code", "DS-7804N-F1(D)0420240822CCRRFM5850847WVU");
        queryWrapper.orderByDesc("id").last("limit 1");
        WeighbridgeData latestWeighbridgeData = weighbridgeDataMapper.selectOne(queryWrapper);
        return success(latestWeighbridgeData);
    }

    @GetMapping("/getLatestWeighbridgeData/14")
    public AjaxResult getLatestWeighbridgeData14() {
        QueryWrapper<WeighbridgeData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_code", "DS-7804N-F1(D)0420240416CCRRFD0327551WVU");
        queryWrapper.orderByDesc("id").last("limit 1");
        WeighbridgeData latestWeighbridgeData = weighbridgeDataMapper.selectOne(queryWrapper);
        return success(latestWeighbridgeData);
    }

}
