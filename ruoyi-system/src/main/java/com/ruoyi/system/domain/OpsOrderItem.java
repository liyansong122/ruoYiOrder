package com.ruoyi.system.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 运营订单明细表 ops_order_item
 */
public class OpsOrderItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long itemId;

    private Long orderId;

    private Long productId;

    private String productName;

    private BigDecimal unitPrice;

    private Integer quantity;

    private Integer paidQuantity;

    private BigDecimal paidAmount;

    private BigDecimal amount;

    private String remark;

    /** 商品编码（关联ops_product，非持久化字段） */
    private String productCode;

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
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

    public Integer getPaidQuantity()
    {
        return paidQuantity;
    }

    public void setPaidQuantity(Integer paidQuantity)
    {
        this.paidQuantity = paidQuantity;
    }

    public BigDecimal getPaidAmount()
    {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount)
    {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("orderId", getOrderId())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("unitPrice", getUnitPrice())
            .append("quantity", getQuantity())
            .append("paidQuantity", getPaidQuantity())
            .append("paidAmount", getPaidAmount())
            .append("amount", getAmount())
            .append("remark", getRemark())
            .toString();
    }
}
