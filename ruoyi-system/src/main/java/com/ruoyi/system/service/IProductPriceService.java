package com.ruoyi.system.service;

import com.ruoyi.system.domain.ProductPrice;

import java.util.List;

/**
 * 商品定价Service接口
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
public interface IProductPriceService 
{
    /**
     * 查询商品定价
     * 
     * @param id 商品定价主键
     * @return 商品定价
     */
    public ProductPrice selectProductPriceById(Long id);

    /**
     * 查询商品定价列表
     * 
     * @param productPrice 商品定价
     * @return 商品定价集合
     */
    public List<ProductPrice> selectProductPriceList(ProductPrice productPrice);

    /**
     * 新增商品定价
     * 
     * @param productPrice 商品定价
     * @return 结果
     */
    public int insertProductPrice(ProductPrice productPrice);

    /**
     * 修改商品定价
     * 
     * @param productPrice 商品定价
     * @return 结果
     */
    public int updateProductPrice(ProductPrice productPrice);

    /**
     * 批量删除商品定价
     * 
     * @param ids 需要删除的商品定价主键集合
     * @return 结果
     */
    public int deleteProductPriceByIds(Long[] ids);

    /**
     * 删除商品定价信息
     * 
     * @param id 商品定价主键
     * @return 结果
     */
    public int deleteProductPriceById(Long id);
}
