package com.ruoyi.system.mapper.convert;

import com.ruoyi.system.domain.InventoryHistory;
import com.ruoyi.system.domain.InventoryMovementDetail;
import com.ruoyi.system.pojo.dto.InventoryMovementDetailDTO;
import com.ruoyi.system.pojo.vo.InventoryMovementDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 库存移动详情  DO <=> DTO <=> VO / BO / Query
 *
 * @author zcc
 */
@Mapper(componentModel = "spring")
public interface InventoryMovementDetailConvert {

  /**
   * @param source DO
   * @return DTO
   */
  InventoryMovementDetailDTO do2dto(InventoryMovementDetail source);

  /**
   * @param source DTO
   * @return DO
   */
  InventoryMovementDetail dto2do(InventoryMovementDetailDTO source);

  List<InventoryMovementDetailVO> dos2vos(List<InventoryMovementDetail> list);

  List<InventoryMovementDetail> vos2dos(List<InventoryMovementDetailVO> details);

  @Mapping(target = "quantity", source = "realQuantity")
  InventoryHistory do2InventoryHistory(InventoryMovementDetailVO it);
}
