package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysActivationCode;
import com.ruoyi.system.service.ISysActivationCodeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 系统激活码Controller
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
@RestController
@RequestMapping("/system/code")
public class SysActivationCodeController extends BaseController
{
    @Autowired
    private ISysActivationCodeService sysActivationCodeService;

    /**
     * 查询系统激活码列表
     */
    @PreAuthorize("@ss.hasPermi('system:code:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysActivationCode sysActivationCode)
    {
        startPage();
        List<SysActivationCode> list = sysActivationCodeService.selectSysActivationCodeList(sysActivationCode);
        return getDataTable(list);
    }

    /**
     * 导出系统激活码列表
     */
    @PreAuthorize("@ss.hasPermi('system:code:export')")
    @Log(title = "系统激活码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysActivationCode sysActivationCode)
    {
        List<SysActivationCode> list = sysActivationCodeService.selectSysActivationCodeList(sysActivationCode);
        ExcelUtil<SysActivationCode> util = new ExcelUtil<SysActivationCode>(SysActivationCode.class);
        util.exportExcel(response, list, "系统激活码数据");
    }

    /**
     * 获取系统激活码详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:code:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysActivationCodeService.selectSysActivationCodeById(id));
    }

    /**
     * 新增系统激活码
     */
    @PreAuthorize("@ss.hasPermi('system:code:add')")
    @Log(title = "系统激活码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysActivationCode sysActivationCode)
    {
        return toAjax(sysActivationCodeService.insertSysActivationCode(sysActivationCode));
    }

    /**
     * 修改系统激活码
     */
    @PreAuthorize("@ss.hasPermi('system:code:edit')")
    @Log(title = "系统激活码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysActivationCode sysActivationCode)
    {
        return toAjax(sysActivationCodeService.updateSysActivationCode(sysActivationCode));
    }

    /**
     * 删除系统激活码
     */
    @PreAuthorize("@ss.hasPermi('system:code:remove')")
    @Log(title = "系统激活码", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysActivationCodeService.deleteSysActivationCodeByIds(ids));
    }

    /**
     * 批量生成激活码
     */
    @PostMapping("/batchGenerateActivationCode")
    public AjaxResult batchGenerateActivationCode()
    {
        SysUser sessionUser = SecurityUtils.getLoginUser().getUser();
        sysActivationCodeService.batchGenerateActivationCode(sessionUser.getUserName());
        return success();
    }

    /**
     * 生成激活码
     */
    @PostMapping("/generateActivationCode")
    public AjaxResult generateActivationCode()
    {
        SysUser sessionUser = SecurityUtils.getLoginUser().getUser();
        return toAjax(sysActivationCodeService.generateActivationCode(sessionUser.getUserName()));
    }
}
