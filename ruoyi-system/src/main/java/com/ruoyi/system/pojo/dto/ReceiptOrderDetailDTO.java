package com.ruoyi.system.pojo.dto;

import com.ruoyi.common.core.domain.BaseAudit;
import lombok.Data;

import java.math.BigDecimal;
/**
 * 入库单详情 DTO 对象
 *
 * @author zcc
 */
@Data
public class ReceiptOrderDetailDTO extends BaseAudit {
    private Long id;
    private Long receiptOrderId;
    private Long itemId;
    private BigDecimal planQuantity;
    private BigDecimal realQuantity;
    private Long rackId;
    private String remark;
    private Long warehouseId;
    private Long areaId;
    private Integer receiptOrderStatus;
}
