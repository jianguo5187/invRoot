package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品库存对象 product_inventory
 * 
 * @author ruoyi
 * @date 2025-07-03
 */
public class ProductInventory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品库存ID */
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

    /** 库存数量 */
    @Excel(name = "库存数量")
    private Double quantity;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("robotId", getRobotId())
            .append("chatId", getChatId())
            .append("isGroup", getIsGroup())
            .append("productName", getProductName())
            .append("quantity", getQuantity())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
