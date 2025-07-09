package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ProductPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ProductPriceMapper;
import com.ruoyi.system.service.IProductPriceService;

/**
 * 商品定价Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
@Service
public class ProductPriceServiceImpl implements IProductPriceService 
{
    @Autowired
    private ProductPriceMapper productPriceMapper;

    /**
     * 查询商品定价
     * 
     * @param id 商品定价主键
     * @return 商品定价
     */
    @Override
    public ProductPrice selectProductPriceById(Long id)
    {
        return productPriceMapper.selectProductPriceById(id);
    }

    /**
     * 查询商品定价列表
     * 
     * @param productPrice 商品定价
     * @return 商品定价
     */
    @Override
    public List<ProductPrice> selectProductPriceList(ProductPrice productPrice)
    {
        return productPriceMapper.selectProductPriceList(productPrice);
    }

    /**
     * 新增商品定价
     * 
     * @param productPrice 商品定价
     * @return 结果
     */
    @Override
    public int insertProductPrice(ProductPrice productPrice)
    {
        productPrice.setCreateTime(DateUtils.getNowDate());
        return productPriceMapper.insertProductPrice(productPrice);
    }

    /**
     * 修改商品定价
     * 
     * @param productPrice 商品定价
     * @return 结果
     */
    @Override
    public int updateProductPrice(ProductPrice productPrice)
    {
        productPrice.setUpdateTime(DateUtils.getNowDate());
        return productPriceMapper.updateProductPrice(productPrice);
    }

    /**
     * 批量删除商品定价
     * 
     * @param ids 需要删除的商品定价主键
     * @return 结果
     */
    @Override
    public int deleteProductPriceByIds(Long[] ids)
    {
        return productPriceMapper.deleteProductPriceByIds(ids);
    }

    /**
     * 删除商品定价信息
     * 
     * @param id 商品定价主键
     * @return 结果
     */
    @Override
    public int deleteProductPriceById(Long id)
    {
        return productPriceMapper.deleteProductPriceById(id);
    }
}
