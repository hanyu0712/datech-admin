package com.datech.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datech.admin.entity.SalesIncomeConfirmEntity;
import com.datech.admin.entity.SysUserEntity;
import com.datech.admin.mapper.SalesIncomeConfirmMapper;
import com.datech.admin.service.SysLogServiceImpl;
import com.datech.admin.util.BeanCopierUtil;
import com.datech.admin.vo.SalesIncomeConfirmVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 销售合同收入确认明细
 */
@Controller
public class SalesIncomeConfirmController {
    @Resource
    public SysLogServiceImpl sysLogService;
    @Resource
    public SalesIncomeConfirmMapper salesIncomeConfirmMapper;

    @ResponseBody
    @GetMapping(value = "/salesIncomeConfirm/{id}")
    public Object getOne(@PathVariable("id") Integer id) {
        SalesIncomeConfirmEntity entity = salesIncomeConfirmMapper.selectById(id);
        SalesIncomeConfirmVo vo = BeanCopierUtil.copyBean(entity, SalesIncomeConfirmVo.class);
        if (entity.getInvoiceDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            vo.setInvoiceDateStr(sdf.format(entity.getInvoiceDate()));
        }
        return vo;
    }

    @GetMapping("/salesIncomeConfirm")
    public String getList(HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        LambdaQueryWrapper<SalesIncomeConfirmEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(SalesIncomeConfirmEntity::getContractNo);
        List<SalesIncomeConfirmEntity> dataList = new ArrayList<>();
        if ("DK-BJ".equals(loginUser.getCompany()) && "caiwu".equals(loginUser.getDepartment()) && loginUser.getRole() == 1) {
            dataList = salesIncomeConfirmMapper.selectList(queryWrapper);

        } else if ("caiwu".equals(loginUser.getDepartment())) {
            if (loginUser.getRole() != 1) {
                queryWrapper.eq(SalesIncomeConfirmEntity::getCreateUserId, loginUser.getId());
            }
            dataList = salesIncomeConfirmMapper.selectList(queryWrapper);
        }
        //        dataList.forEach(salesContractInfo -> {});
        model.addAttribute("dataList", dataList);
        return "sales_income_confirm";
    }

    @ResponseBody
    @PostMapping("/updateSalesIncomeConfirm")
    public String updateSalesIncomeConfirm(SalesIncomeConfirmEntity info, String method, HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        switch (method) {
            case "save":
                info.setStatus(0);//待提交
                if (info.getId() == null) {
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    salesIncomeConfirmMapper.insert(info);
                } else {
                    salesIncomeConfirmMapper.updateById(info);
                }
                break;
            case "submit":
                info.setStatus(1);//待审核
                if (info.getId() == null) {
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    salesIncomeConfirmMapper.insert(info);
                } else {
                    salesIncomeConfirmMapper.updateById(info);
                }
                sysLogService.addLog("salesIncomeConfirm", info.getId(), "提交审核", loginUser.getRealName());
                break;
            case "approved":
                SalesIncomeConfirmEntity entity = new SalesIncomeConfirmEntity();
                entity.setId(info.getId());
                entity.setStatus(2);//审核通过
                entity.setAuditUserId(loginUser.getId());
                entity.setAuditUserName(loginUser.getRealName());
                entity.setRejectionContent("");
                salesIncomeConfirmMapper.updateById(entity);
                sysLogService.addLog("salesIncomeConfirm", info.getId(), "审核通过", loginUser.getRealName());
                break;
            case "reject":
                SalesIncomeConfirmEntity entity2 = new SalesIncomeConfirmEntity();
                entity2.setId(info.getId());
                entity2.setStatus(3);//审核驳回
                entity2.setAuditUserId(loginUser.getId());
                entity2.setAuditUserName(loginUser.getRealName());
                entity2.setRejectionContent(info.getRejectionContent());
                salesIncomeConfirmMapper.updateById(entity2);
                sysLogService.addLog("salesIncomeConfirm", info.getId(), "审核驳回。驳回原因：" + info.getRejectionContent(), loginUser.getRealName());
                break;
            case "rollback":
                SalesIncomeConfirmEntity entity3 = new SalesIncomeConfirmEntity();
                entity3.setId(info.getId());
                entity3.setStatus(3);//待审核
                entity3.setAuditUserId(loginUser.getId());
                entity3.setAuditUserName(loginUser.getRealName());
                entity3.setRejectionContent(info.getRejectionContent());
                salesIncomeConfirmMapper.updateById(entity3);
                sysLogService.addLog("salesIncomeConfirm", info.getId(), "取消审核", loginUser.getRealName());
                break;
            default:
        }

        return "{\"msg\":\"操作成功\"}";
    }


}
