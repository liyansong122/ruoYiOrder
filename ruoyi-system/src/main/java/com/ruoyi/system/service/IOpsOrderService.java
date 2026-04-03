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
     * 根据订单ID查询订单
     */
    OpsOrder selectOpsOrderById(Long orderId);

    /**
     * 更新订单
     */
    int updateOpsOrder(OpsOrder order);

    /**
     * 导入订单（Excel 列与导出一致：订单号、商品名称、购买人、订单金额、订单状态、下单时间）
     *
     * @param orderList 解析后的订单列表
     * @param updateSupport 是否更新已存在的订单号
     * @return 结果说明
     */
    String importOrder(List<OpsOrder> orderList, boolean updateSupport);
}
