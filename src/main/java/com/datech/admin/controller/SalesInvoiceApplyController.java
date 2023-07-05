package com.datech.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datech.admin.entity.SalesInvoiceApplyEntity;
import com.datech.admin.entity.SysUserEntity;
import com.datech.admin.mapper.SalesInvoiceApplyMapper;
import com.datech.admin.service.SysLogServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 发票申请
 */
@Controller
public class SalesInvoiceApplyController {
    @Resource
    public SysLogServiceImpl sysLogService;
    @Resource
    public SalesInvoiceApplyMapper salesInvoiceApplyMapper;

    @ResponseBody
    @GetMapping(value = "/salesInvoiceApply/{id}")
    public Object getOne(@PathVariable("id") Integer id) {
        return salesInvoiceApplyMapper.selectById(id);
    }

    @GetMapping("/salesInvoiceApply")
    public String getList(HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        LambdaQueryWrapper<SalesInvoiceApplyEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(SalesInvoiceApplyEntity::getContractNo);
        List<SalesInvoiceApplyEntity> dataList = new ArrayList<>();
        if ("DK-BJ".equals(loginUser.getCompany()) && "caiwu".equals(loginUser.getDepartment()) && loginUser.getRole() == 1) {
            dataList = salesInvoiceApplyMapper.selectList(queryWrapper);

        } else if ("shangwu".equals(loginUser.getDepartment())) {
            if (loginUser.getRole() != 1) {
                queryWrapper.eq(SalesInvoiceApplyEntity::getCreateUserId, loginUser.getId());
            }
            dataList = salesInvoiceApplyMapper.selectList(queryWrapper);
        }
        model.addAttribute("dataList", dataList);
        return "sales_invoice_apply";
    }

    @ResponseBody
    @PostMapping("/updateSalesInvoiceApply")
    public String updateSalesInvoiceApply(SalesInvoiceApplyEntity info, String method, HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        switch (method) {
            case "save":
                info.setStatus(0);//待提交
                info.setCreateUserId(loginUser.getId());
                info.setCreateUserName(loginUser.getRealName());
                info.setCreateTime(new Date());
                if (info.getId() == null) {
                    salesInvoiceApplyMapper.insert(info);
                } else {
                    salesInvoiceApplyMapper.updateById(info);
                }
                break;
            case "submit":
                info.setStatus(1);//待商务审核
                if (info.getId() == null) {
                    salesInvoiceApplyMapper.insert(info);
                } else {
                    salesInvoiceApplyMapper.updateById(info);
                }
                sysLogService.addLog("salesInvoiceApply", info.getId(), "提交审核", loginUser.getRealName());
                break;
            case "approved":
                info = salesInvoiceApplyMapper.selectById(info.getId());
                SalesInvoiceApplyEntity entity = new SalesInvoiceApplyEntity();
                entity.setId(info.getId());
                if (info.getStatus() == 1) {
                    entity.setStatus(4);//待财务审核
                    entity.setAuditShangwuId(loginUser.getId());
                    entity.setAuditShangwuName(loginUser.getRealName());
                    entity.setRejectionContent("");
                    salesInvoiceApplyMapper.updateById(entity);
                    sysLogService.addLog("salesInvoiceApply", info.getId(), "商务审核通过", loginUser.getRealName());
                }
                if (info.getStatus() == 4) {
                    entity.setStatus(2);//审核通过
                    entity.setAuditCaiwuId(loginUser.getId());
                    entity.setAuditCaiwuName(loginUser.getRealName());
                    entity.setRejectionContent("");
                    salesInvoiceApplyMapper.updateById(entity);
                    sysLogService.addLog("salesInvoiceApply", info.getId(), "财务审核通过", loginUser.getRealName());
                }
                break;
            case "reject":
                SalesInvoiceApplyEntity entity2 = new SalesInvoiceApplyEntity();
                entity2.setId(info.getId());
                entity2.setStatus(3);//驳回,重新编辑
                entity2.setAuditShangwuId(loginUser.getId());
                entity2.setAuditShangwuName(loginUser.getRealName());
                entity2.setRejectionContent(info.getRejectionContent());
                salesInvoiceApplyMapper.updateById(entity2);
                sysLogService.addLog("salesInvoiceApply", info.getId(), "审核驳回。驳回原因：" + info.getRejectionContent(), loginUser.getRealName());
                break;
            case "rollback":
                info = salesInvoiceApplyMapper.selectById(info.getId());
                SalesInvoiceApplyEntity entity3 = new SalesInvoiceApplyEntity();
                entity3.setId(info.getId());
                entity3.setStatus(3);//驳回,重新编辑
                entity3.setRejectionContent(info.getRejectionContent());
                if (info.getStatus() == 4) {
                    entity3.setAuditShangwuId(loginUser.getId());
                    entity3.setAuditShangwuName(loginUser.getRealName());
                    salesInvoiceApplyMapper.updateById(entity3);
                    sysLogService.addLog("salesInvoiceApply", info.getId(), "商务取消审核", loginUser.getRealName());
                }
                if (info.getStatus() == 2) {
                    entity3.setAuditCaiwuId(loginUser.getId());
                    entity3.setAuditCaiwuName(loginUser.getRealName());
                    salesInvoiceApplyMapper.updateById(entity3);
                    sysLogService.addLog("salesInvoiceApply", info.getId(), "财务取消审核", loginUser.getRealName());
                }
                break;
            default:
        }

        return "{\"msg\":\"操作成功\"}";
    }

    @ResponseBody
    @GetMapping("/salesInvoiceSelect")
    public Object getListStatus2() {
        QueryWrapper<SalesInvoiceApplyEntity> query = new QueryWrapper<>();
        query.select(" DISTINCT contract_no, project_name, company ");
        query.eq("status", 2);
        return salesInvoiceApplyMapper.selectList(query);
    }

}
