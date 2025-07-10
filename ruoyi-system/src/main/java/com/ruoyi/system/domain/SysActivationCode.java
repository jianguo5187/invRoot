package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 系统激活码对象 sys_activation_code
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
public class SysActivationCode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 激活码 */
    @Excel(name = "激活码")
    private String code;

    /** 绑定用户ID */
    @Excel(name = "绑定用户ID")
    private Long userId;

    /** Telegram用户ID */
    @Excel(name = "Telegram用户ID")
    private Long telegramId;

    /** Telegram用户名称 */
    @Excel(name = "Telegram用户名称")
    private String telegramName;

    /** 是否群组（0 私聊 1群组） */
    @Excel(name = "是否群组")
    private String isGroup;

    /** 状态（0未使用 1已使用） */
    @Excel(name = "状态", readConverterExp = "0=未使用,1=已使用")
    private String status;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setTelegramId(Long telegramId) 
    {
        this.telegramId = telegramId;
    }

    public Long getTelegramId() 
    {
        return telegramId;
    }

    public String getTelegramName() {
        return telegramName;
    }

    public void setTelegramName(String telegramName) {
        this.telegramName = telegramName;
    }

    public void setIsGroup(String isGroup)
    {
        this.isGroup = isGroup;
    }

    public String getIsGroup()
    {
        return isGroup;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setExpireTime(Date expireTime) 
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() 
    {
        return expireTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("userId", getUserId())
            .append("telegramId", getTelegramId())
            .append("telegramName", getTelegramName())
            .append("isGroup", getIsGroup())
            .append("status", getStatus())
            .append("expireTime", getExpireTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
