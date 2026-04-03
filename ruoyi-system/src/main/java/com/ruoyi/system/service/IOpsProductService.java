package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OpsProduct;

/**
 * 运营商品 服务层
 */
public interface IOpsProductService
{
    List<OpsProduct> selectOpsProductList(OpsProduct product);

    OpsProduct selectOpsProductById(Long productId);

    int insertOpsProduct(OpsProduct product);

    int updateOpsProduct(OpsProduct product);

    int deleteOpsProductByIds(String ids);

    boolean checkProductCodeUnique(OpsProduct product);
}
