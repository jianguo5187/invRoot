package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.ProductTransaction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.IProductTransactionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品出入库记录Controller
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
@RestController
@RequestMapping("/system/transaction")
public class ProductTransactionController extends BaseController
{
    @Autowired
    private IProductTransactionService productTransactionService;

    /**
     * 查询商品出入库记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:transaction:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductTransaction productTransaction)
    {
        startPage();
        List<ProductTransaction> list = productTransactionService.selectProductTransactionList(productTransaction);
        return getDataTable(list);
    }

    /**
     * 导出商品出入库记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:transaction:export')")
    @Log(title = "商品出入库记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductTransaction productTransaction)
    {
        List<ProductTransaction> list = productTransactionService.selectProductTransactionList(productTransaction);
        ExcelUtil<ProductTransaction> util = new ExcelUtil<ProductTransaction>(ProductTransaction.class);
        util.exportExcel(response, list, "商品出入库记录数据");
    }

    /**
     * 获取商品出入库记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:transaction:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(productTransactionService.selectProductTransactionById(id));
    }

    /**
     * 新增商品出入库记录
     */
    @PreAuthorize("@ss.hasPermi('system:transaction:add')")
    @Log(title = "商品出入库记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductTransaction productTransaction)
    {
        return toAjax(productTransactionService.insertProductTransaction(productTransaction));
    }

    /**
     * 修改商品出入库记录
     */
    @PreAuthorize("@ss.hasPermi('system:transaction:edit')")
    @Log(title = "商品出入库记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductTransaction productTransaction)
    {
        return toAjax(productTransactionService.updateProductTransaction(productTransaction));
    }

    /**
     * 删除商品出入库记录
     */
    @PreAuthorize("@ss.hasPermi('system:transaction:remove')")
    @Log(title = "商品出入库记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(productTransactionService.deleteProductTransactionByIds(ids));
    }
}
