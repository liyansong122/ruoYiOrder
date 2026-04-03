package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OpsOrder;

/**
 * 运营订单 数据层
 */
public interface OpsOrderMapper {
    List<OpsOrder> selectOpsOrderList(OpsOrder order);

    int insertOpsOrder(OpsOrder order);

    int updateOpsOrder(OpsOrder order);

    OpsOrder checkOrderNoUnique(String orderNo);

    /**
     * 根据前缀查询当天最大的订单号
     */
    String selectMaxOrderNoByPrefix(String prefix);

    /**
     * 根据订单ID查询订单
     */
    OpsOrder selectOpsOrderById(Long orderId);
}