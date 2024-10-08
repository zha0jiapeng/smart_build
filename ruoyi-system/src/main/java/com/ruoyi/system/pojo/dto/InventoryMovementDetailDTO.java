package com.ruoyi.system.pojo.dto;

import com.ruoyi.common.core.domain.BaseAudit;
import lombok.Data;

import java.math.BigDecimal;
/**
 * 库存移动详情 DTO 对象
 *
 * @author zcc
 */
@Data
public class InventoryMovementDetailDTO extends BaseAudit {
    private Long id;
    private Long inventoryMovementId;
    private Long itemId;
    private BigDecimal planQuantity;
    private BigDecimal realQuantity;
    private String remark;
    private Long sourceRackId;
    private Long sourceWarehouseId;
    private Long sourceAreaId;
    private Integer moveStatus;
    private Long targetRackId;
    private Long targetWarehouseId;
    private Long targetAreaId;
}
