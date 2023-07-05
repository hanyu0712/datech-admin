package com.datech.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datech.admin.entity.PurchaseInvoiceEntity;
import com.datech.admin.entity.SysUserEntity;
import com.datech.admin.mapper.PurchaseInvoiceMapper;
import com.datech.admin.service.SysLogServiceImpl;
import com.datech.admin.util.BeanCopierUtil;
import com.datech.admin.vo.PurchaseInvoiceVo;
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
 * 采购合同进项发票统计
 */
@Controller
public class PurchaseInvoiceController {
    @Resource
    public SysLogServiceImpl sysLogService;
    @Resource
    public PurchaseInvoiceMapper purchaseInvoiceMapper;

    @ResponseBody
    @GetMapping(value = "/purchaseInvoice/{id}")
    public Object getOne(@PathVariable("id") Integer id) {
        PurchaseInvoiceEntity entity = purchaseInvoiceMapper.selectById(id);
        PurchaseInvoiceVo vo = BeanCopierUtil.copyBean(entity, PurchaseInvoiceVo.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (entity.getInvoiceDate() != null) {
            vo.setInvoiceDateStr(sdf.format(entity.getInvoiceDate()));
        }
        if (entity.getReceiveDate() != null) {
            vo.setReceiveDateStr(sdf.format(entity.getReceiveDate()));
        }
        return vo;
    }

    @GetMapping("/purchaseInvoice")
    public String getList(HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        LambdaQueryWrapper<PurchaseInvoiceEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(PurchaseInvoiceEntity::getContractNo);
        List<PurchaseInvoiceEntity> dataList = new ArrayList<>();
        if ("DK-BJ".equals(loginUser.getCompany()) && "caiwu".equals(loginUser.getDepartment()) && loginUser.getRole() == 1) {
            dataList = purchaseInvoiceMapper.selectList(queryWrapper);

        } else if ("shangwu".equals(loginUser.getDepartment())) {
            if (loginUser.getRole() != 1) {
                queryWrapper.eq(PurchaseInvoiceEntity::getCreateUserId, loginUser.getId());
            }
            dataList = purchaseInvoiceMapper.selectList(queryWrapper);
        }
        //        dataList.forEach(salesContractInfo -> {});
        model.addAttribute("dataList", dataList);
        return "purchase_invoice";
    }

    @ResponseBody
    @PostMapping("/updatePurchaseInvoice")
    public String updateSalesCollection(PurchaseInvoiceEntity info, String method, HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        switch (method) {
            case "save":
                info.setStatus(0);//待提交
                if (info.getId() == null) {
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    purchaseInvoiceMapper.insert(info);
                } else {
                    purchaseInvoiceMapper.updateById(info);
                }
                break;
            case "submit":
                info.setStatus(1);//待审核
                if (info.getId() == null) {
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    purchaseInvoiceMapper.insert(info);
                } else {
                    purchaseInvoiceMapper.updateById(info);
                }
                sysLogService.addLog("purchaseInvoice", info.getId(), "提交审核", loginUser.getRealName());
                break;
            case "approved":
                PurchaseInvoiceEntity entity = new PurchaseInvoiceEntity();
                entity.setId(info.getId());
                entity.setStatus(2);//审核通过
                entity.setAuditUserId(loginUser.getId());
                entity.setAuditUserName(loginUser.getRealName());
                entity.setRejectionContent("");
                purchaseInvoiceMapper.updateById(entity);
                sysLogService.addLog("purchaseInvoice", info.getId(), "审核通过", loginUser.getRealName());
                break;
            case "reject":
                PurchaseInvoiceEntity entity2 = new PurchaseInvoiceEntity();
                entity2.setId(info.getId());
                entity2.setStatus(3);//审核驳回
                entity2.setAuditUserId(loginUser.getId());
                entity2.setAuditUserName(loginUser.getRealName());
                entity2.setRejectionContent(info.getRejectionContent());
                purchaseInvoiceMapper.updateById(entity2);
                sysLogService.addLog("purchaseInvoice", info.getId(), "审核驳回。驳回原因：" + info.getRejectionContent(), loginUser.getRealName());
                break;
            case "rollback":
                PurchaseInvoiceEntity entity3 = new PurchaseInvoiceEntity();
                entity3.setId(info.getId());
                entity3.setStatus(3);//待审核
                entity3.setAuditUserId(loginUser.getId());
                entity3.setAuditUserName(loginUser.getRealName());
                entity3.setRejectionContent(info.getRejectionContent());
                purchaseInvoiceMapper.updateById(entity3);
                sysLogService.addLog("purchaseInvoice", info.getId(), "取消审核", loginUser.getRealName());
                break;
            default:
        }

        return "{\"msg\":\"操作成功\"}";
    }


}
