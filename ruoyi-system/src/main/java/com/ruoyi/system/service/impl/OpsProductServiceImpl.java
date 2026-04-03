package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.OpsProduct;
import com.ruoyi.system.mapper.OpsProductMapper;
import com.ruoyi.system.service.IOpsProductService;

@Service
public class OpsProductServiceImpl implements IOpsProductService
{
    @Autowired
    private OpsProductMapper opsProductMapper;

    @Override
    public List<OpsProduct> selectOpsProductList(OpsProduct product)
    {
        return opsProductMapper.selectOpsProductList(product);
    }

    @Override
    public OpsProduct selectOpsProductById(Long productId)
    {
        return opsProductMapper.selectOpsProductById(productId);
    }

    @Override
    public int insertOpsProduct(OpsProduct product)
    {
        return opsProductMapper.insertOpsProduct(product);
    }

    @Override
    public int updateOpsProduct(OpsProduct product)
    {
        return opsProductMapper.updateOpsProduct(product);
    }

    @Override
    public int deleteOpsProductByIds(String ids)
    {
        return opsProductMapper.deleteOpsProductByIds(Convert.toLongArray(ids));
    }

    @Override
    public boolean checkProductCodeUnique(OpsProduct product)
    {
        Long productId = StringUtils.isNull(product.getProductId()) ? -1L : product.getProductId();
        OpsProduct info = opsProductMapper.checkProductCodeUnique(product.getProductCode());
        if (StringUtils.isNotNull(info) && info.getProductId().longValue() != productId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
