package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.vo.CurrentInventoryRespVo;
import com.ruoyi.system.domain.vo.TodayProductTransactionRespVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductPriceMapper priceMapper;

    @Autowired
    private ProductInventoryMapper inventoryMapper;

    @Autowired
    private ProductTransactionMapper transactionMapper;

    @Override
    @Transactional
    public boolean setProductPrice(Long chatId, Boolean isGroup, String productName, Double price) {
        ProductPrice priceRecord = new ProductPrice();
        priceRecord.setChatId(chatId);
        priceRecord.setIsGroup(isGroup);
        priceRecord.setProductName(productName);
        priceRecord.setPrice(price);
        priceRecord.setCreateTime(new Date());
        priceRecord.setUpdateTime(new Date());

        // 先检查是否已存在
        ProductPrice existing = priceMapper.selectByChatAndProduct(chatId, productName);
        if (existing != null) {
            existing.setPrice(price);
            existing.setUpdateTime(new Date());
            return priceMapper.updateProductPrice(existing) > 0;
        } else {
            return priceMapper.insertProductPrice(priceRecord) > 0;
        }
    }

    @Override
    @Transactional
    public boolean processInventory(Long chatId, Boolean isGroup, String productName, Integer quantity, String operator) {
        // 1. 更新库存
        ProductInventory inventory = inventoryMapper.selectByChatAndProduct(chatId, productName);
        if (inventory == null) {
            inventory = new ProductInventory();
            inventory.setChatId(chatId);
            inventory.setIsGroup(isGroup);
            inventory.setProductName(productName);
            inventory.setQuantity(quantity);
            inventory.setCreateBy(operator);
            inventory.setCreateTime(new Date());
            inventoryMapper.insertProductInventory(inventory);
        } else {
            inventory.setQuantity(inventory.getQuantity() + quantity);
            inventory.setUpdateTime(new Date());
            inventory.setUpdateBy(operator);
            inventoryMapper.updateProductInventory(inventory);
        }

        // 2. 记录交易
        ProductTransaction transaction = new ProductTransaction();
        transaction.setChatId(chatId);
        transaction.setIsGroup(isGroup);
        transaction.setProductName(productName);
        transaction.setQuantity(quantity);
        transaction.setTransactionTime(new Date());
        transaction.setOperator(operator);
        transactionMapper.insertProductTransaction(transaction);

        return true;
    }

    @Override
    public List<TodayProductTransactionRespVo> getTodayTransactions(Long chatId, Boolean isGroup) {
        return transactionMapper.selectTodayProductTransactionList(chatId,isGroup);
    }

    @Override
    public List<TodayProductTransactionRespVo> getTransactionHistory(Long chatId, Boolean isGroup, String historyDate) {
//        ProductTransaction searchProductTransaction =  new ProductTransaction();
//        searchProductTransaction.setChatId(chatId);
//        searchProductTransaction.setIsGroup(isGroup);
//        searchProductTransaction.setProductName(productName);
        return transactionMapper.selectProductTransactionHistoryList(chatId,isGroup,historyDate);
    }

    @Override
    public List<CurrentInventoryRespVo> getCurrentInventory(Long chatId, Boolean isGroup) {
//        ProductInventory searchProductInventory = new ProductInventory();
//        searchProductInventory.setChatId(chatId);
//        searchProductInventory.setIsGroup(isGroup);
        return inventoryMapper.getCurrentInventory(chatId,isGroup);
    }

    @Override
    @Transactional
    public boolean deleteAllData(Long chatId, Boolean isGroup) {
        transactionMapper.deleteByChatId(chatId);
        inventoryMapper.deleteByChatId(chatId);
        priceMapper.deleteByChatId(chatId);
        return true;
    }

    @Override
    public Double getProductPrice(Long chatId, Boolean isGroup, String productName) {
        ProductPrice price = priceMapper.selectByChatAndProduct(chatId, productName);
        return price != null ? price.getPrice() : null;
    }

    @Override
    public String inventoryPreCheck(Long chatId, Boolean isGroup, String productName, Integer quantity){
        String checkResult = "";

        // 先检查商品是否已存在
        ProductPrice existing = priceMapper.selectByChatAndProduct(chatId, productName);
        if (existing == null) {
            checkResult = "商品不存在，请先给商品定价";
            return checkResult;
        }

        // 获取当前库存
        Integer nowInvQty = 0;
        ProductInventory inventory = inventoryMapper.selectByChatAndProduct(chatId, productName);
        if(inventory != null){
            nowInvQty = inventory.getQuantity();
        }
        if((nowInvQty + quantity) < 0){
            checkResult = "出库所需库存不足，当前在库数量：" + nowInvQty;
            return checkResult;
        }

        return checkResult;
    }
}