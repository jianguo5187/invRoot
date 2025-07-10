package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ProductTransaction;
import com.ruoyi.system.domain.vo.TodayProductTransactionRespVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品出入库记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
public interface ProductTransactionMapper 
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
     * 删除商品出入库记录
     * 
     * @param id 商品出入库记录主键
     * @return 结果
     */
    public int deleteProductTransactionById(Long id);

    /**
     * 批量删除商品出入库记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductTransactionByIds(Long[] ids);

    public int deleteByChatId(@Param("robotId")String robotId, @Param("chatId")Long chatId);

    public List<TodayProductTransactionRespVo> selectTodayProductTransactionList(@Param("robotId")String robotId, @Param("chatId")Long chatId, @Param("isGroup")Boolean isGroup);

    public List<TodayProductTransactionRespVo> selectProductTransactionHistoryList(@Param("robotId")String robotId, @Param("chatId")Long chatId, @Param("isGroup")Boolean isGroup, @Param("transactionDate")String transactionDate);
}
