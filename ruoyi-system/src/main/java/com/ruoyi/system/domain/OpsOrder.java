package com.ruoyi.system.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;

/**
 * 运营订单表 ops_order（列表展示含商品名称）
 */
public class OpsOrder implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long orderId;

    @Excel(name = "订单号")
    private String orderNo;

    private Long productId;

    @Excel(name = "商品名称")
    private String productName;

    @Excel(name = "购买人")
    private String buyerName;

    @Excel(name = "订单金额", cellType = ColumnType.NUMERIC)
    private BigDecimal amount;

    @Excel(name = "单价", cellType = ColumnType.NUMERIC)
    private BigDecimal unitPrice;

    @Excel(name = "数量")
    private Integer quantity;

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

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
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

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
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

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("productId", getProductId())
            .append("buyerName", getBuyerName())
            .append("amount", getAmount())
            .append("unitPrice", getUnitPrice())
            .append("quantity", getQuantity())
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
