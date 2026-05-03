package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OpsOrder;

/**
 * 运营订单 服务层
 */
public interface IOpsOrderService
{
    List<OpsOrder> selectOpsOrderList(OpsOrder order);

    int insertOpsOrder(OpsOrder order);

    boolean checkOrderNoUnique(OpsOrder order);

    /**
     * 根据订单ID查询订单（含明细）
     */
    OpsOrder selectOpsOrderById(Long orderId);

    /**
     * 更新订单（含明细）
     */
    int updateOpsOrder(OpsOrder order);

    /**
     * 导入订单
     */
    String importOrder(List<OpsOrder> orderList, boolean updateSupport);
}
