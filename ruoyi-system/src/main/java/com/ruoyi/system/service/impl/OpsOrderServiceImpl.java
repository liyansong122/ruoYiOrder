package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.OpsOrder;
import com.ruoyi.system.domain.OpsOrderItem;
import com.ruoyi.system.domain.OpsProduct;
import com.ruoyi.system.mapper.OpsOrderMapper;
import com.ruoyi.system.mapper.OpsOrderItemMapper;
import com.ruoyi.system.mapper.OpsProductMapper;
import com.ruoyi.system.service.IOpsOrderService;

@Service
public class OpsOrderServiceImpl implements IOpsOrderService
{
    private static final Logger log = LoggerFactory.getLogger(OpsOrderServiceImpl.class);

    @Autowired
    private OpsOrderMapper opsOrderMapper;

    @Autowired
    private OpsOrderItemMapper opsOrderItemMapper;

    @Autowired
    private OpsProductMapper opsProductMapper;

    @Override
    public List<OpsOrder> selectOpsOrderList(OpsOrder order)
    {
        return opsOrderMapper.selectOpsOrderList(order);
    }

    /**
     * 生成订单号：ORD + 年月日 + 4位自增序号
     */
    private synchronized String generateOrderNo()
    {
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String prefix = "ORD" + dateStr;
        String maxOrderNo = opsOrderMapper.selectMaxOrderNoByPrefix(prefix);
        int seq = 1;
        if (StringUtils.isNotEmpty(maxOrderNo))
        {
            try
            {
                String seqStr = maxOrderNo.substring(prefix.length());
                seq = Integer.parseInt(seqStr) + 1;
            }
            catch (NumberFormatException e)
            {
                seq = 1;
            }
        }
        return prefix + String.format("%04d", seq);
    }

    @Override
    @Transactional
    public int insertOpsOrder(OpsOrder order)
    {
        // 自动生成订单号
        if (StringUtils.isEmpty(order.getOrderNo()))
        {
            order.setOrderNo(generateOrderNo());
        }
        // 默认下单日期
        if (order.getOrderDate() == null)
        {
            order.setOrderDate(new Date());
        }
        // 计算总金额并处理明细
        calculateOrderAmount(order);
        // 检查并扣减库存
        checkAndDeductStock(order.getItems(), null);
        int rows = opsOrderMapper.insertOpsOrder(order);
        // 保存明细
        saveOrderItems(order);
        return rows;
    }

    @Override
    public boolean checkOrderNoUnique(OpsOrder order)
    {
        Long orderId = StringUtils.isNull(order.getOrderId()) ? -1L : order.getOrderId();
        OpsOrder info = opsOrderMapper.checkOrderNoUnique(order.getOrderNo());
        if (StringUtils.isNotNull(info) && info.getOrderId().longValue() != orderId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public OpsOrder selectOpsOrderById(Long orderId)
    {
        OpsOrder order = opsOrderMapper.selectOpsOrderById(orderId);
        if (order != null)
        {
            List<OpsOrderItem> items = opsOrderItemMapper.selectItemsByOrderId(orderId);
            order.setItems(items);
        }
        return order;
    }

    @Override
    @Transactional
    public int updateOpsOrder(OpsOrder order)
    {
        // 计算总金额并处理明细
        calculateOrderAmount(order);
        // 获取旧明细用于库存回滚
        List<OpsOrderItem> oldItems = opsOrderItemMapper.selectItemsByOrderId(order.getOrderId());
        // 先回滚旧库存，再删除旧明细（顺序很重要）
        checkAndDeductStock(order.getItems(), oldItems);
        // 删除旧明细
        opsOrderItemMapper.deleteItemsByOrderId(order.getOrderId());
        // 保存新明细
        saveOrderItems(order);
        return opsOrderMapper.updateOpsOrder(order);
    }

    /**
     * 计算订单金额（各明细小计）
     * 明细：amount = unitPrice * quantity
     * 订单：amount = 各明细amount之和
     * paidAmount：如果前端已传入则保留用户手动输入值，否则根据明细paidQuantity计算
     */
    private void calculateOrderAmount(OpsOrder order)
    {
        List<OpsOrderItem> items = order.getItems();
        if (items != null && !items.isEmpty())
        {
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal totalPaid = BigDecimal.ZERO;
            for (OpsOrderItem item : items)
            {
                // 计算每行小计
                if (item.getUnitPrice() != null && item.getQuantity() != null)
                {
                    BigDecimal itemAmount = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
                    item.setAmount(itemAmount);
                    total = total.add(itemAmount);

                    // 计算明细已付金额：已付数量 * 单价
                    int paidQty = (item.getPaidQuantity() != null) ? item.getPaidQuantity() : 0;
                    if (paidQty > item.getQuantity())
                    {
                        paidQty = item.getQuantity();
                        item.setPaidQuantity(paidQty);
                    }
                    BigDecimal itemPaid = item.getUnitPrice().multiply(new BigDecimal(paidQty));
                    item.setPaidAmount(itemPaid);
                    totalPaid = totalPaid.add(itemPaid);
                }
            }
            order.setAmount(total);
            // 保留前端传入的 paidAmount（用户手动输入），仅在未传入时才按明细计算
            if (order.getPaidAmount() == null)
            {
                order.setPaidAmount(totalPaid);
            }
        }
    }

    /**
     * 检查并扣减库存
     * @param newItems 新订单明细
     * @param oldItems 旧订单明细（修改时用，用于回滚库存）
     */
    private void checkAndDeductStock(List<OpsOrderItem> newItems, List<OpsOrderItem> oldItems)
    {
        if (newItems == null || newItems.isEmpty())
        {
            return;
        }
        
        // 先回滚旧库存（如果是修改操作）
        if (oldItems != null && !oldItems.isEmpty())
        {
            for (OpsOrderItem oldItem : oldItems)
            {
                if (oldItem.getProductId() != null && oldItem.getQuantity() != null)
                {
                    OpsProduct product = opsProductMapper.selectOpsProductById(oldItem.getProductId());
                    if (product != null && product.getStock() != null)
                    {
                        // 回滚库存：旧数量加回去
                        product.setStock(product.getStock() + oldItem.getQuantity());
                        opsProductMapper.updateOpsProduct(product);
                    }
                }
            }
        }
        
        // 检查新库存并扣减
        for (OpsOrderItem newItem : newItems)
        {
            if (newItem.getProductId() != null && newItem.getQuantity() != null)
            {
                OpsProduct product = opsProductMapper.selectOpsProductById(newItem.getProductId());
                if (product == null)
                {
                    throw new ServiceException("商品不存在，ID：" + newItem.getProductId());
                }
                if (product.getStock() == null)
                {
                    throw new ServiceException("商品库存信息异常：" + product.getProductName());
                }
                if (newItem.getQuantity() > product.getStock())
                {
                    throw new ServiceException("商品【" + product.getProductName() + "】库存不足，当前库存：" 
                            + product.getStock() + "，需要数量：" + newItem.getQuantity());
                }
                // 扣减库存
                product.setStock(product.getStock() - newItem.getQuantity());
                opsProductMapper.updateOpsProduct(product);
            }
        }
    }

    /**
     * 保存订单明细
     */
    private void saveOrderItems(OpsOrder order)
    {
        List<OpsOrderItem> items = order.getItems();
        if (items != null && !items.isEmpty())
        {
            for (OpsOrderItem item : items)
            {
                item.setOrderId(order.getOrderId());
                // 补充商品名称
                if (StringUtils.isEmpty(item.getProductName()) && item.getProductId() != null)
                {
                    OpsProduct p = opsProductMapper.selectOpsProductById(item.getProductId());
                    if (p != null)
                    {
                        item.setProductName(p.getProductName());
                    }
                }
            }
            opsOrderItemMapper.batchInsertOpsOrderItem(items);
        }
    }

    @Override
    @Transactional
    public String importOrder(List<OpsOrder> orderList, boolean updateSupport)
    {
        if (StringUtils.isNull(orderList) || orderList.isEmpty())
        {
            throw new ServiceException("导入订单数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (OpsOrder order : orderList)
        {
            try
            {
                if (StringUtils.isEmpty(order.getOrderNo()))
                {
                    throw new IllegalArgumentException("订单号不能为空");
                }
                order.setOrderNo(order.getOrderNo().trim());
                if (StringUtils.isEmpty(order.getBuyerName()))
                {
                    throw new IllegalArgumentException("购买人不能为空");
                }
                if (order.getAmount() == null)
                {
                    throw new IllegalArgumentException("订单金额不能为空");
                }
                String st = order.getOrderStatus();
                if (StringUtils.isEmpty(st))
                {
                    order.setOrderStatus("0");
                }
                else if (!StringUtils.equalsAny(st, "0", "1", "2"))
                {
                    throw new IllegalArgumentException("订单状态须为待支付、已支付、已取消（或 0/1/2）");
                }
                OpsOrder exist = opsOrderMapper.checkOrderNoUnique(order.getOrderNo());
                if (StringUtils.isNull(exist))
                {
                    if (order.getOrderDate() == null)
                    {
                        order.setOrderDate(new Date());
                    }
                    opsOrderMapper.insertOpsOrder(order);
                    // 导入时如有商品名称，自动创建一条明细
                    if (StringUtils.isNotEmpty(order.getProductName()))
                    {
                        OpsProduct p = opsProductMapper.selectOpsProductByProductName(order.getProductName().trim());
                        if (p != null)
                        {
                            OpsOrderItem item = new OpsOrderItem();
                            item.setOrderId(order.getOrderId());
                            item.setProductId(p.getProductId());
                            item.setProductName(p.getProductName());
                            item.setUnitPrice(order.getAmount());
                            item.setQuantity(1);
                            item.setAmount(order.getAmount());
                            opsOrderItemMapper.insertOpsOrderItem(item);
                        }
                    }
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、订单 ").append(order.getOrderNo()).append(" 导入成功");
                }
                else if (updateSupport)
                {
                    order.setOrderId(exist.getOrderId());
                    opsOrderMapper.updateOpsOrder(order);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、订单 ").append(order.getOrderNo()).append(" 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、订单 ").append(order.getOrderNo()).append(" 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String no = order.getOrderNo() != null ? order.getOrderNo() : "未知";
                failureMsg.append("<br/>").append(failureNum).append("、订单 ").append(no).append(" 导入失败：").append(e.getMessage());
                log.error("订单导入失败 {}", no, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据异常，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        successMsg.insert(0, "恭喜您，订单数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        return successMsg.toString();
    }
}
