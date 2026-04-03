package com.ruoyi.web.controller.operation;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.OpsProduct;
import com.ruoyi.system.service.IOpsProductService;

/**
 * 运营-商品维护
 */
@Controller
@RequestMapping("/operation/product")
public class OperationProductController extends BaseController
{
    private String prefix = "operation/product";

    @Autowired
    private IOpsProductService opsProductService;

    @RequiresPermissions("operation:product:view")
    @GetMapping()
    public String product()
    {
        return prefix + "/product";
    }

    @RequiresPermissions("operation:product:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OpsProduct product)
    {
        startPage();
        List<OpsProduct> list = opsProductService.selectOpsProductList(product);
        return getDataTable(list);
    }

    @Log(title = "商品维护", businessType = BusinessType.EXPORT)
    @RequiresPermissions("operation:product:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OpsProduct product)
    {
        List<OpsProduct> list = opsProductService.selectOpsProductList(product);
        ExcelUtil<OpsProduct> util = new ExcelUtil<>(OpsProduct.class);
        return util.exportExcel(list, "商品数据");
    }

    @RequiresPermissions("operation:product:remove")
    @Log(title = "商品维护", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(opsProductService.deleteOpsProductByIds(ids));
    }

    @RequiresPermissions("operation:product:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    @RequiresPermissions("operation:product:add")
    @Log(title = "商品维护", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated OpsProduct product)
    {
        if (!opsProductService.checkProductCodeUnique(product))
        {
            return error("新增商品失败，商品编码已存在");
        }
        product.setCreateBy(getLoginName());
        return toAjax(opsProductService.insertOpsProduct(product));
    }

    @RequiresPermissions("operation:product:edit")
    @GetMapping("/edit/{productId}")
    public String edit(@PathVariable("productId") Long productId, ModelMap mmap)
    {
        mmap.put("product", opsProductService.selectOpsProductById(productId));
        return prefix + "/edit";
    }

    @RequiresPermissions("operation:product:edit")
    @Log(title = "商品维护", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated OpsProduct product)
    {
        if (!opsProductService.checkProductCodeUnique(product))
        {
            return error("修改商品失败，商品编码已存在");
        }
        product.setUpdateBy(getLoginName());
        return toAjax(opsProductService.updateOpsProduct(product));
    }

    @PostMapping("/checkProductCodeUnique")
    @ResponseBody
    public boolean checkProductCodeUnique(OpsProduct product)
    {
        return opsProductService.checkProductCodeUnique(product);
    }
}
