package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ProductPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品定价Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
public interface ProductPriceMapper 
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
     * 删除商品定价
     * 
     * @param id 商品定价主键
     * @return 结果
     */
    public int deleteProductPriceById(Long id);

    /**
     * 批量删除商品定价
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductPriceByIds(Long[] ids);

    public ProductPrice selectByChatAndProduct(@Param("chatId")Long chatId, @Param("productName")String productName);

    public int deleteByChatId(Long id);
}
