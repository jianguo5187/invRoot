package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ProductInventory;
import com.ruoyi.system.domain.vo.CurrentInventoryRespVo;
import com.ruoyi.system.domain.vo.TodayProductTransactionRespVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
public interface ProductInventoryMapper 
{
    /**
     * 查询商品库存
     * 
     * @param id 商品库存主键
     * @return 商品库存
     */
    public ProductInventory selectProductInventoryById(Long id);

    /**
     * 查询商品库存列表
     * 
     * @param productInventory 商品库存
     * @return 商品库存集合
     */
    public List<ProductInventory> selectProductInventoryList(ProductInventory productInventory);

    /**
     * 新增商品库存
     * 
     * @param productInventory 商品库存
     * @return 结果
     */
    public int insertProductInventory(ProductInventory productInventory);

    /**
     * 修改商品库存
     * 
     * @param productInventory 商品库存
     * @return 结果
     */
    public int updateProductInventory(ProductInventory productInventory);

    /**
     * 删除商品库存
     * 
     * @param id 商品库存主键
     * @return 结果
     */
    public int deleteProductInventoryById(Long id);

    /**
     * 批量删除商品库存
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductInventoryByIds(Long[] ids);

    public ProductInventory selectByChatAndProduct(@Param("chatId")Long chatId, @Param("productName")String productName);
    public int deleteByChatId(Long id);

    public List<CurrentInventoryRespVo> getCurrentInventory(@Param("chatId")Long chatId, @Param("isGroup")Boolean isGroup);
}
