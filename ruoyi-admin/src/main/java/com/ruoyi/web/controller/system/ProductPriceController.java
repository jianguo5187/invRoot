package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.ProductPrice;
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
import com.ruoyi.system.service.IProductPriceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品定价Controller
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
@RestController
@RequestMapping("/system/price")
public class ProductPriceController extends BaseController
{
    @Autowired
    private IProductPriceService productPriceService;

    /**
     * 查询商品定价列表
     */
    @PreAuthorize("@ss.hasPermi('system:price:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductPrice productPrice)
    {
        startPage();
        List<ProductPrice> list = productPriceService.selectProductPriceList(productPrice);
        return getDataTable(list);
    }

    /**
     * 导出商品定价列表
     */
    @PreAuthorize("@ss.hasPermi('system:price:export')")
    @Log(title = "商品定价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductPrice productPrice)
    {
        List<ProductPrice> list = productPriceService.selectProductPriceList(productPrice);
        ExcelUtil<ProductPrice> util = new ExcelUtil<ProductPrice>(ProductPrice.class);
        util.exportExcel(response, list, "商品定价数据");
    }

    /**
     * 获取商品定价详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:price:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(productPriceService.selectProductPriceById(id));
    }

    /**
     * 新增商品定价
     */
    @PreAuthorize("@ss.hasPermi('system:price:add')")
    @Log(title = "商品定价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductPrice productPrice)
    {
        return toAjax(productPriceService.insertProductPrice(productPrice));
    }

    /**
     * 修改商品定价
     */
    @PreAuthorize("@ss.hasPermi('system:price:edit')")
    @Log(title = "商品定价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductPrice productPrice)
    {
        return toAjax(productPriceService.updateProductPrice(productPrice));
    }

    /**
     * 删除商品定价
     */
    @PreAuthorize("@ss.hasPermi('system:price:remove')")
    @Log(title = "商品定价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(productPriceService.deleteProductPriceByIds(ids));
    }
}
