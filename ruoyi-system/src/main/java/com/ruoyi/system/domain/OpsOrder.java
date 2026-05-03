package com.ruoyi.system.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;

/**
 * 运营订单表 ops_order
 */
public class OpsOrder implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long orderId;

    @Excel(name = "订单号")
    private String orderNo;

    /** 订单明细列表 */
    private List<OpsOrderItem> items;

    /** 商品名称汇总（用于列表展示，非数据库字段） */
    @Excel(name = "商品")
    private String productName;

    @Excel(name = "购买人")
    private String buyerName;

    @Excel(name = "订单金额", cellType = ColumnType.NUMERIC)
    private BigDecimal amount;

    @Excel(name = "已付金额", cellType = ColumnType.NUMERIC)
    private BigDecimal paidAmount;

    /** 待付金额（非持久化，导出用） */
    @Excel(name = "待付金额", cellType = ColumnType.NUMERIC)
    private BigDecimal unpaidAmount;

    @Excel(name = "地区")
    private String region;

    @Excel(name = "电话")
    private String phone;

    @Excel(name = "发货物流")
    private String logistics;

    @Excel(name = "厂家")
    private String factory;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "下单日期", width = 20, dateFormat = "yyyy-MM-dd")
    private Date orderDate;

    /** 下单日期查询-开始 */
    private String orderDateBegin;

    /** 下单日期查询-结束 */
    private String orderDateEnd;

    @Excel(name = "订单状态", readConverterExp = "0=待支付,1=已支付,2=已取消")
    private String orderStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String remark;

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public List<OpsOrderItem> getItems()
    {
        return items;
    }

    public void setItems(List<OpsOrderItem> items)
    {
        this.items = items;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getBuyerName()
    {
        return buyerName;
    }

    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getPaidAmount()
    {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount)
    {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getUnpaidAmount()
    {
        return unpaidAmount;
    }

    public void setUnpaidAmount(BigDecimal unpaidAmount)
    {
        this.unpaidAmount = unpaidAmount;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getLogistics()
    {
        return logistics;
    }

    public void setLogistics(String logistics)
    {
        this.logistics = logistics;
    }

    public String getFactory()
    {
        return factory;
    }

    public void setFactory(String factory)
    {
        this.factory = factory;
    }

    public Date getOrderDate()
    {
        return orderDate;
    }

    public void setOrderDate(Date orderDate)
    {
        this.orderDate = orderDate;
    }

    public String getOrderDateBegin()
    {
        return orderDateBegin;
    }

    public void setOrderDateBegin(String orderDateBegin)
    {
        this.orderDateBegin = orderDateBegin;
    }

    public String getOrderDateEnd()
    {
        return orderDateEnd;
    }

    public void setOrderDateEnd(String orderDateEnd)
    {
        this.orderDateEnd = orderDateEnd;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("buyerName", getBuyerName())
            .append("amount", getAmount())
            .append("paidAmount", getPaidAmount())
            .append("region", getRegion())
            .append("phone", getPhone())
            .append("logistics", getLogistics())
            .append("factory", getFactory())
            .append("orderDate", getOrderDate())
            .append("orderStatus", getOrderStatus())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }
}
