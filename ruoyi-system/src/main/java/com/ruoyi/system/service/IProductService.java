package com.ruoyi.system.service;

import com.ruoyi.system.domain.ProductInventory;
import com.ruoyi.system.domain.ProductTransaction;
import com.ruoyi.system.domain.vo.CurrentInventoryRespVo;
import com.ruoyi.system.domain.vo.TodayProductTransactionRespVo;

import java.util.List;

public interface IProductService {
    // 商品定价
    public boolean setProductPrice(Long chatId, Boolean isGroup, String productName, Double price);

    // 商品出入库
    public boolean processInventory(Long chatId, Boolean isGroup, String productName, Integer quantity, String operator);

    // 获取当日出入库列表
    public List<TodayProductTransactionRespVo> getTodayTransactions(Long chatId, Boolean isGroup);

    // 获取商品出入库历史
    public List<TodayProductTransactionRespVo> getTransactionHistory(Long chatId, Boolean isGroup, String historyDate);

    // 获取当前库存
    public List<CurrentInventoryRespVo> getCurrentInventory(Long chatId, Boolean isGroup);

    // 删除所有数据
    public boolean deleteAllData(Long chatId, Boolean isGroup);

    // 获取商品价格
    public Double getProductPrice(Long chatId, Boolean isGroup, String productName);

    // 商品出入库前处理
    public String inventoryPreCheck(Long chatId, Boolean isGroup, String productName, Integer quantity);
}