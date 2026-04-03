package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.OpsOrder;
import com.ruoyi.system.domain.OpsProduct;
import com.ruoyi.system.mapper.OpsOrderMapper;
import com.ruoyi.system.mapper.OpsProductMapper;
import com.ruoyi.system.service.IOpsOrderService;

@Service
public class OpsOrderServiceImpl implements IOpsOrderService
{
    private static final Logger log = LoggerFactory.getLogger(OpsOrderServiceImpl.class);

    @Autowired
    private OpsOrderMapper opsOrderMapper;

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
        // 查询当天最大的订单号
        String maxOrderNo = opsOrderMapper.selectMaxOrderNoByPrefix(prefix);
        int seq = 1;
        if (StringUtils.isNotEmpty(maxOrderNo))
        {
            try
            {
                // 提取序号部分并加1
                String seqStr = maxOrderNo.substring(prefix.length());
                seq = Integer.parseInt(seqStr) + 1;
            }
            catch (NumberFormatException e)
            {
                seq = 1;
            }
        }
        // 格式化为4位序号
        return prefix + String.format("%04d", seq);
    }

    @Override
    public int insertOpsOrder(OpsOrder order)
    {
        // 如果订单号为空，自动生成
        if (StringUtils.isEmpty(order.getOrderNo()))
        {
            order.setOrderNo(generateOrderNo());
        }
        // 如果下单日期为空，默认为当前日期
        if (order.getOrderDate() == null)
        {
            order.setOrderDate(new Date());
        }
        // 计算总金额（单价 * 数量）
        if (order.getUnitPrice() != null && order.getQuantity() != null)
        {
            BigDecimal amount = order.getUnitPrice().multiply(new BigDecimal(order.getQuantity()));
            order.setAmount(amount);
        }
        return opsOrderMapper.insertOpsOrder(order);
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
        return opsOrderMapper.selectOpsOrderById(orderId);
    }

    @Override
    public int updateOpsOrder(OpsOrder order)
    {
        // 计算总金额（单价 * 数量）
        if (order.getUnitPrice() != null && order.getQuantity() != null)
        {
            BigDecimal amount = order.getUnitPrice().multiply(new BigDecimal(order.getQuantity()));
            order.setAmount(amount);
        }
        return opsOrderMapper.updateOpsOrder(order);
    }

    @Override
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
                if (StringUtils.isEmpty(order.getProductName()))
                {
                    throw new IllegalArgumentException("商品名称不能为空");
                }
                OpsProduct p = opsProductMapper.selectOpsProductByProductName(order.getProductName().trim());
                if (p == null)
                {
                    throw new IllegalArgumentException("未找到商品：" + order.getProductName());
                }
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
                order.setProductId(p.getProductId());
                OpsOrder exist = opsOrderMapper.checkOrderNoUnique(order.getOrderNo());
                if (StringUtils.isNull(exist))
                {
                    opsOrderMapper.insertOpsOrder(order);
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
