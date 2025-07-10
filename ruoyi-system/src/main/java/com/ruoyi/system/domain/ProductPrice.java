package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品定价对象 product_price
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
public class ProductPrice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品定价ID */
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

    /** 商品价格 */
    @Excel(name = "商品价格")
    private Double price;

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

    public void setPrice(Double price) 
    {
        this.price = price;
    }

    public Double getPrice() 
    {
        return price;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("robotId", getRobotId())
            .append("chatId", getChatId())
            .append("isGroup", getIsGroup())
            .append("productName", getProductName())
            .append("price", getPrice())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
