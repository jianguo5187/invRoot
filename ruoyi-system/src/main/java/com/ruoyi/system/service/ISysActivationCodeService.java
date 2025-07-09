package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysActivationCode;

/**
 * 系统激活码Service接口
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
public interface ISysActivationCodeService 
{
    /**
     * 查询系统激活码
     * 
     * @param id 系统激活码主键
     * @return 系统激活码
     */
    public SysActivationCode selectSysActivationCodeById(Long id);

    /**
     * 查询系统激活码列表
     * 
     * @param sysActivationCode 系统激活码
     * @return 系统激活码集合
     */
    public List<SysActivationCode> selectSysActivationCodeList(SysActivationCode sysActivationCode);

    /**
     * 新增系统激活码
     * 
     * @param sysActivationCode 系统激活码
     * @return 结果
     */
    public int insertSysActivationCode(SysActivationCode sysActivationCode);

    /**
     * 修改系统激活码
     * 
     * @param sysActivationCode 系统激活码
     * @return 结果
     */
    public int updateSysActivationCode(SysActivationCode sysActivationCode);

    /**
     * 批量删除系统激活码
     * 
     * @param ids 需要删除的系统激活码主键集合
     * @return 结果
     */
    public int deleteSysActivationCodeByIds(Long[] ids);

    /**
     * 删除系统激活码信息
     * 
     * @param id 系统激活码主键
     * @return 结果
     */
    public int deleteSysActivationCodeById(Long id);

    SysActivationCode selectByCode(String code);
    SysActivationCode selectByTelegramId(Long telegramId);
    int updateActivationCode(SysActivationCode code);
    List<SysActivationCode> selectCodeList(SysActivationCode code);
    int insertActivationCode(SysActivationCode code);
    int batchInsert(List<SysActivationCode> codes);
    public int generateActivationCode(String userName);
}
