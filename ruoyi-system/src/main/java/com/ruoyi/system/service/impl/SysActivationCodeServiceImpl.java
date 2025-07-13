package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.UUID;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysActivationCodeMapper;
import com.ruoyi.system.domain.SysActivationCode;
import com.ruoyi.system.service.ISysActivationCodeService;

/**
 * 系统激活码Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
@Service
public class SysActivationCodeServiceImpl implements ISysActivationCodeService 
{
    @Autowired
    private SysActivationCodeMapper sysActivationCodeMapper;

    /**
     * 查询系统激活码
     * 
     * @param id 系统激活码主键
     * @return 系统激活码
     */
    @Override
    public SysActivationCode selectSysActivationCodeById(Long id)
    {
        return sysActivationCodeMapper.selectSysActivationCodeById(id);
    }

    /**
     * 查询系统激活码列表
     * 
     * @param sysActivationCode 系统激活码
     * @return 系统激活码
     */
    @Override
    public List<SysActivationCode> selectSysActivationCodeList(SysActivationCode sysActivationCode)
    {
        return sysActivationCodeMapper.selectSysActivationCodeList(sysActivationCode);
    }

    /**
     * 新增系统激活码
     * 
     * @param sysActivationCode 系统激活码
     * @return 结果
     */
    @Override
    public int insertSysActivationCode(SysActivationCode sysActivationCode)
    {
        sysActivationCode.setCreateTime(DateUtils.getNowDate());
        return sysActivationCodeMapper.insertSysActivationCode(sysActivationCode);
    }

    /**
     * 修改系统激活码
     * 
     * @param sysActivationCode 系统激活码
     * @return 结果
     */
    @Override
    public int updateSysActivationCode(SysActivationCode sysActivationCode)
    {
        sysActivationCode.setUpdateTime(DateUtils.getNowDate());
        return sysActivationCodeMapper.updateSysActivationCode(sysActivationCode);
    }

    /**
     * 批量删除系统激活码
     * 
     * @param ids 需要删除的系统激活码主键
     * @return 结果
     */
    @Override
    public int deleteSysActivationCodeByIds(Long[] ids)
    {
        return sysActivationCodeMapper.deleteSysActivationCodeByIds(ids);
    }

    /**
     * 删除系统激活码信息
     * 
     * @param id 系统激活码主键
     * @return 结果
     */
    @Override
    public int deleteSysActivationCodeById(Long id)
    {
        return sysActivationCodeMapper.deleteSysActivationCodeById(id);
    }

    @Override
    public SysActivationCode selectByCode(String code) {
        return sysActivationCodeMapper.selectByCode(code);
    }

    @Override
    public SysActivationCode selectByTelegramId(Long telegramId) {
        return sysActivationCodeMapper.selectByTelegramId(telegramId);
    }

    @Override
    public int updateActivationCode(SysActivationCode code) {
        code.setUpdateTime(DateUtils.getNowDate());
        return sysActivationCodeMapper.updateActivationCode(code);
    }

    @Override
    public List<SysActivationCode> selectCodeList(SysActivationCode code) {
        return sysActivationCodeMapper.selectCodeList(code);
    }

    @Override
    public int insertActivationCode(SysActivationCode code) {
        code.setCreateTime(DateUtils.getNowDate());
        return sysActivationCodeMapper.insertActivationCode(code);
    }

    @Override
    public int batchInsert(List<SysActivationCode> codes) {
        codes.forEach(code -> {
            code.setCreateTime(DateUtils.getNowDate());
            code.setStatus("0");
        });
        return sysActivationCodeMapper.batchInsert(codes);
    }

    @Override
    public void batchGenerateActivationCode(String userName) {
        for(int i=0;i<100;i++){
            SysActivationCode sysActivationCode = new SysActivationCode();
            sysActivationCode.setCode(generateUnusedCode());
            sysActivationCode.setCreateBy(userName);
            sysActivationCode.setCreateTime(DateUtils.getNowDate());
            sysActivationCodeMapper.insertSysActivationCode(sysActivationCode);
        }
    }

    @Override
    public int generateActivationCode(String userName) {
        SysActivationCode sysActivationCode = new SysActivationCode();
        sysActivationCode.setCode(generateUnusedCode());
        sysActivationCode.setCreateBy(userName);
        sysActivationCode.setCreateTime(DateUtils.getNowDate());
        return sysActivationCodeMapper.insertSysActivationCode(sysActivationCode);
    }

    public String generateUnusedCode() {
        // 实现生成唯一激活码的逻辑
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        // 检查是否已存在，如果存在则递归生成新的
        if (checkCodeExists(code)) {
            return generateUnusedCode();
        }
        return code;
    }
    private boolean checkCodeExists(String code) {
        SysActivationCode searchActivationCode = new SysActivationCode();
        searchActivationCode.setCode(code);

        return sysActivationCodeMapper.selectCodeList(searchActivationCode).size()>0;
    }
}
