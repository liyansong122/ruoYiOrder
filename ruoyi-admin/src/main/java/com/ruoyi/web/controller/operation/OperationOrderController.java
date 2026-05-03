package com.ruoyi.web.controller.operation;

import java.math.BigDecimal;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.OpsOrder;
import com.ruoyi.system.domain.OpsProduct;
import com.ruoyi.system.service.IOpsOrderService;
import com.ruoyi.system.service.IOpsProductService;

/**
 * 运营-订单查询
 */
@Controller
@RequestMapping("/operation/order")
public class OperationOrderController extends BaseController
{
    private String prefix = "operation/order";

    @Autowired
    private IOpsOrderService opsOrderService;

    @Autowired
    private IOpsProductService opsProductService;

    @RequiresPermissions("operation:order:view")
    @GetMapping()
    public String order(ModelMap mmap)
    {
        mmap.put("isAdmin", ShiroUtils.isAdmin());
        return prefix + "/order";
    }

    @RequiresPermissions("operation:order:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(OpsOrder order)
    {
        startPage();
        List<OpsOrder> list = opsOrderService.selectOpsOrderList(order);
        return getDataTable(list);
    }

    @Log(title = "订单查询", businessType = BusinessType.EXPORT)
    @RequiresPermissions("operation:order:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(OpsOrder order)
    {
        List<OpsOrder> list = opsOrderService.selectOpsOrderList(order);
        // 计算待付金额
        for (OpsOrder o : list)
        {
            BigDecimal total = o.getAmount() != null ? o.getAmount() : BigDecimal.ZERO;
            BigDecimal paid = o.getPaidAmount() != null ? o.getPaidAmount() : BigDecimal.ZERO;
            o.setUnpaidAmount(total.subtract(paid));
        }
        ExcelUtil<OpsOrder> util = new ExcelUtil<>(OpsOrder.class);
        return util.exportExcel(list, "订单数据");
    }

    @Log(title = "订单查询", businessType = BusinessType.IMPORT)
    @RequiresPermissions("operation:order:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<OpsOrder> util = new ExcelUtil<>(OpsOrder.class);
        List<OpsOrder> list = util.importExcel(file.getInputStream());
        String message = opsOrderService.importOrder(list, updateSupport);
        return AjaxResult.success(message);
    }

    @RequiresPermissions("operation:order:import")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<OpsOrder> util = new ExcelUtil<>(OpsOrder.class);
        return util.importTemplateExcel("订单数据");
    }

    /**
     * 商品搜索API（供Select2使用）- 返回库存信息
     */
    @PostMapping("/searchProduct")
    @ResponseBody
    public AjaxResult searchProduct(String productName)
    {
        OpsProduct q = new OpsProduct();
        q.setStatus("0");
        if (StringUtils.isNotEmpty(productName))
        {
            q.setProductName(productName);
        }
        List<OpsProduct> list = opsProductService.selectOpsProductList(q);
        // 转换为包含库存信息的简单对象
        List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for (OpsProduct p : list)
        {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("productId", p.getProductId());
            map.put("productName", p.getProductName());
            map.put("productCode", p.getProductCode());
            map.put("stock", p.getStock());
            result.add(map);
        }
        return AjaxResult.success(result);
    }

    @RequiresPermissions("operation:order:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    @RequiresPermissions("operation:order:add")
    @Log(title = "订单录入", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add", produces = "application/json")
    @ResponseBody
    public AjaxResult addSave(@RequestBody OpsOrder order)
    {
        if (order.getItems() == null || order.getItems().isEmpty())
        {
            return error("请至少添加一个商品");
        }
        if (StringUtils.isEmpty(order.getBuyerName()))
        {
            return error("购买人不能为空");
        }
        if (StringUtils.isEmpty(order.getOrderStatus()))
        {
            order.setOrderStatus("0");
        }
        if (!opsOrderService.checkOrderNoUnique(order))
        {
            return error("订单号已存在");
        }
        return toAjax(opsOrderService.insertOpsOrder(order));
    }

    @PostMapping("/checkOrderNoUnique")
    @ResponseBody
    public boolean checkOrderNoUnique(OpsOrder order)
    {
        return opsOrderService.checkOrderNoUnique(order);
    }

    /**
     * 编辑订单页面 - 仅超级管理员可访问
     */
    @GetMapping("/edit/{orderId}")
    public String edit(@PathVariable("orderId") Long orderId, ModelMap mmap)
    {
        if (!ShiroUtils.isAdmin())
        {
            return "redirect:/operation/order";
        }
        OpsOrder order = opsOrderService.selectOpsOrderById(orderId);
        mmap.put("order", order);
        return prefix + "/edit";
    }

    /**
     * 编辑订单保存 - 仅超级管理员可操作
     */
    @Log(title = "订单编辑", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/edit", produces = "application/json")
    @ResponseBody
    public AjaxResult editSave(@RequestBody OpsOrder order)
    {
        if (!ShiroUtils.isAdmin())
        {
            return error("无权限操作");
        }
        if (order.getOrderId() == null)
        {
            return error("订单ID不能为空");
        }
        if (order.getItems() == null || order.getItems().isEmpty())
        {
            return error("请至少添加一个商品");
        }
        if (StringUtils.isEmpty(order.getBuyerName()))
        {
            return error("购买人不能为空");
        }
        return toAjax(opsOrderService.updateOpsOrder(order));
    }
}
