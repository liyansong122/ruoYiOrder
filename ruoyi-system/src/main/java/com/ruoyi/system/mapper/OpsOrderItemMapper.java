package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OpsOrderItem;

/**
 * 运营订单明细 数据层
 */
public interface OpsOrderItemMapper
{
    /**
     * 根据订单ID查询明细列表
     */
    List<OpsOrderItem> selectItemsByOrderId(Long orderId);

    /**
     * 新增订单明细
     */
    int insertOpsOrderItem(OpsOrderItem item);

    /**
     * 批量新增订单明细
     */
    int batchInsertOpsOrderItem(List<OpsOrderItem> items);

    /**
     * 根据订单ID删除明细
     */
    int deleteItemsByOrderId(Long orderId);

    /**
     * 根据订单ID批量删除明细
     */
    int deleteItemsByOrderIds(Long[] orderIds);
}
