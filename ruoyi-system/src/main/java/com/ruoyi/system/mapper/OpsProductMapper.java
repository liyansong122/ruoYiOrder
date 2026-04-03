package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OpsProduct;

/**
 * 运营商品 数据层
 */
public interface OpsProductMapper
{
    List<OpsProduct> selectOpsProductList(OpsProduct product);

    OpsProduct selectOpsProductById(Long productId);

    int insertOpsProduct(OpsProduct product);

    int updateOpsProduct(OpsProduct product);

    int deleteOpsProductByIds(Long[] productIds);

    OpsProduct checkProductCodeUnique(String productCode);

    /**
     * 按商品名称精确匹配（用于订单导入）
     */
    OpsProduct selectOpsProductByProductName(String productName);
}
