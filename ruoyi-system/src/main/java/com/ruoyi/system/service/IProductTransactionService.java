package com.ruoyi.system.service;

import com.ruoyi.system.domain.ProductTransaction;

import java.util.List;

/**
 * 商品出入库记录Service接口
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
public interface IProductTransactionService 
{
    /**
     * 查询商品出入库记录
     * 
     * @param id 商品出入库记录主键
     * @return 商品出入库记录
     */
    public ProductTransaction selectProductTransactionById(Long id);

    /**
     * 查询商品出入库记录列表
     * 
     * @param productTransaction 商品出入库记录
     * @return 商品出入库记录集合
     */
    public List<ProductTransaction> selectProductTransactionList(ProductTransaction productTransaction);

    /**
     * 新增商品出入库记录
     * 
     * @param productTransaction 商品出入库记录
     * @return 结果
     */
    public int insertProductTransaction(ProductTransaction productTransaction);

    /**
     * 修改商品出入库记录
     * 
     * @param productTransaction 商品出入库记录
     * @return 结果
     */
    public int updateProductTransaction(ProductTransaction productTransaction);

    /**
     * 批量删除商品出入库记录
     * 
     * @param ids 需要删除的商品出入库记录主键集合
     * @return 结果
     */
    public int deleteProductTransactionByIds(Long[] ids);

    /**
     * 删除商品出入库记录信息
     * 
     * @param id 商品出入库记录主键
     * @return 结果
     */
    public int deleteProductTransactionById(Long id);
}
