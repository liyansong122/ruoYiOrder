package com.ruoyi.system.domain;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 运营商品表 ops_product
 */
public class OpsProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long productId;

    @Excel(name = "商品编码")
    private String productCode;

    @Excel(name = "商品名称")
    private String productName;

    @Excel(name = "单价", cellType = ColumnType.NUMERIC)
    private BigDecimal price;

    @Excel(name = "库存", cellType = ColumnType.NUMERIC)
    private Integer stock;

    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    @NotBlank(message = "商品编码不能为空")
    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    @NotBlank(message = "商品名称不能为空")
    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    @NotNull(message = "单价不能为空")
    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    @NotNull(message = "库存不能为空")
    public Integer getStock()
    {
        return stock;
    }

    public void setStock(Integer stock)
    {
        this.stock = stock;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("productId", getProductId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("price", getPrice())
            .append("stock", getStock())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
