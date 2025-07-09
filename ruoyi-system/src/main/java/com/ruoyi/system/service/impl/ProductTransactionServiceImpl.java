package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ProductTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ProductTransactionMapper;
import com.ruoyi.system.service.IProductTransactionService;

/**
 * 商品出入库记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
@Service
public class ProductTransactionServiceImpl implements IProductTransactionService 
{
    @Autowired
    private ProductTransactionMapper productTransactionMapper;

    /**
     * 查询商品出入库记录
     * 
     * @param id 商品出入库记录主键
     * @return 商品出入库记录
     */
    @Override
    public ProductTransaction selectProductTransactionById(Long id)
    {
        return productTransactionMapper.selectProductTransactionById(id);
    }

    /**
     * 查询商品出入库记录列表
     * 
     * @param productTransaction 商品出入库记录
     * @return 商品出入库记录
     */
    @Override
    public List<ProductTransaction> selectProductTransactionList(ProductTransaction productTransaction)
    {
        return productTransactionMapper.selectProductTransactionList(productTransaction);
    }

    /**
     * 新增商品出入库记录
     * 
     * @param productTransaction 商品出入库记录
     * @return 结果
     */
    @Override
    public int insertProductTransaction(ProductTransaction productTransaction)
    {
        productTransaction.setCreateTime(DateUtils.getNowDate());
        return productTransactionMapper.insertProductTransaction(productTransaction);
    }

    /**
     * 修改商品出入库记录
     * 
     * @param productTransaction 商品出入库记录
     * @return 结果
     */
    @Override
    public int updateProductTransaction(ProductTransaction productTransaction)
    {
        productTransaction.setUpdateTime(DateUtils.getNowDate());
        return productTransactionMapper.updateProductTransaction(productTransaction);
    }

    /**
     * 批量删除商品出入库记录
     * 
     * @param ids 需要删除的商品出入库记录主键
     * @return 结果
     */
    @Override
    public int deleteProductTransactionByIds(Long[] ids)
    {
        return productTransactionMapper.deleteProductTransactionByIds(ids);
    }

    /**
     * 删除商品出入库记录信息
     * 
     * @param id 商品出入库记录主键
     * @return 结果
     */
    @Override
    public int deleteProductTransactionById(Long id)
    {
        return productTransactionMapper.deleteProductTransactionById(id);
    }
}
