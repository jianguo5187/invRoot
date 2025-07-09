package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ProductInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ProductInventoryMapper;
import com.ruoyi.system.service.IProductInventoryService;

/**
 * 商品库存Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
@Service
public class ProductInventoryServiceImpl implements IProductInventoryService 
{
    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    /**
     * 查询商品库存
     * 
     * @param id 商品库存主键
     * @return 商品库存
     */
    @Override
    public ProductInventory selectProductInventoryById(Long id)
    {
        return productInventoryMapper.selectProductInventoryById(id);
    }

    /**
     * 查询商品库存列表
     * 
     * @param productInventory 商品库存
     * @return 商品库存
     */
    @Override
    public List<ProductInventory> selectProductInventoryList(ProductInventory productInventory)
    {
        return productInventoryMapper.selectProductInventoryList(productInventory);
    }

    /**
     * 新增商品库存
     * 
     * @param productInventory 商品库存
     * @return 结果
     */
    @Override
    public int insertProductInventory(ProductInventory productInventory)
    {
        productInventory.setCreateTime(DateUtils.getNowDate());
        return productInventoryMapper.insertProductInventory(productInventory);
    }

    /**
     * 修改商品库存
     * 
     * @param productInventory 商品库存
     * @return 结果
     */
    @Override
    public int updateProductInventory(ProductInventory productInventory)
    {
        productInventory.setUpdateTime(DateUtils.getNowDate());
        return productInventoryMapper.updateProductInventory(productInventory);
    }

    /**
     * 批量删除商品库存
     * 
     * @param ids 需要删除的商品库存主键
     * @return 结果
     */
    @Override
    public int deleteProductInventoryByIds(Long[] ids)
    {
        return productInventoryMapper.deleteProductInventoryByIds(ids);
    }

    /**
     * 删除商品库存信息
     * 
     * @param id 商品库存主键
     * @return 结果
     */
    @Override
    public int deleteProductInventoryById(Long id)
    {
        return productInventoryMapper.deleteProductInventoryById(id);
    }
}
