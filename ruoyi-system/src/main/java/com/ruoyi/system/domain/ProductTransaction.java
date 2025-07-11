package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品出入库记录对象 product_transaction
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
public class ProductTransaction extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品出入库记录ID */
    private Long id;

    /** 机器人ID */
    @Excel(name = "机器人ID")
    private String robotId;

    /** 群组或私聊ID */
    @Excel(name = "群组或私聊ID")
    private Long chatId;

    /** 是否群组 */
    @Excel(name = "是否群组")
    private Boolean isGroup;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String productName;

    /** 数量(正数入库，负数出库) */
    @Excel(name = "数量(正数入库，负数出库)")
    private Double quantity;

    /** 商品价格 */
    @Excel(name = "商品价格")
    private Double price;

    /** 交易时间(-12小时)
     * 例如：2025/07/04 12：00~2025/07/05 11：59 算2025/07/04的数据
     * */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "交易时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date transactionTime;

    /** 操作人 */
    @Excel(name = "操作人")
    private String operator;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    public Boolean getGroup() {
        return isGroup;
    }

    public void setGroup(Boolean group) {
        isGroup = group;
    }

    public void setChatId(Long chatId)
    {
        this.chatId = chatId;
    }

    public Long getChatId() 
    {
        return chatId;
    }

    public void setIsGroup(Boolean isGroup) 
    {
        this.isGroup = isGroup;
    }

    public Boolean getIsGroup() 
    {
        return isGroup;
    }

    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }

    public void setQuantity(Double quantity)
    {
        this.quantity = quantity;
    }

    public Double getQuantity()
    {
        return quantity;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setTransactionTime(Date transactionTime) 
    {
        this.transactionTime = transactionTime;
    }

    public Date getTransactionTime() 
    {
        return transactionTime;
    }

    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("robotId", getRobotId())
            .append("chatId", getChatId())
            .append("isGroup", getIsGroup())
            .append("productName", getProductName())
            .append("quantity", getQuantity())
            .append("price", getPrice())
            .append("transactionTime", getTransactionTime())
            .append("operator", getOperator())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
