package com.datech.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datech.admin.entity.PurchasePaymentEntity;
import com.datech.admin.entity.SysUserEntity;
import com.datech.admin.mapper.PurchasePaymentMapper;
import com.datech.admin.service.SysLogServiceImpl;
import com.datech.admin.util.BeanCopierUtil;
import com.datech.admin.vo.PurchasePaymentVo;
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
 * 销售合同收款明细
 */
@Controller
public class PurchasePaymentController {
    @Resource
    public SysLogServiceImpl sysLogService;
    @Resource
    public PurchasePaymentMapper purchasePaymentMapper;

    @ResponseBody
    @GetMapping(value = "/purchasePayment/{id}")
    public Object getOne(@PathVariable("id") Integer id) {
        PurchasePaymentEntity entity = purchasePaymentMapper.selectById(id);
        PurchasePaymentVo vo = BeanCopierUtil.copyBean(entity, PurchasePaymentVo.class);
        if (entity.getPaymentDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            vo.setPaymentDateStr(sdf.format(entity.getPaymentDate()));
        }
        return vo;
    }

    @GetMapping("/purchasePayment")
    public String getList(HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        LambdaQueryWrapper<PurchasePaymentEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(PurchasePaymentEntity::getContractNo);
        List<PurchasePaymentEntity> dataList = new ArrayList<>();
        if ("DK-BJ".equals(loginUser.getCompany()) && "caiwu".equals(loginUser.getDepartment()) && loginUser.getRole() == 1) {
            dataList = purchasePaymentMapper.selectList(queryWrapper);

        } else if ("caiwu".equals(loginUser.getDepartment())) {
            if (loginUser.getRole() != 1) {
                queryWrapper.eq(PurchasePaymentEntity::getCreateUserId, loginUser.getId());
            }
            dataList = purchasePaymentMapper.selectList(queryWrapper);
        }
        //        dataList.forEach(salesContractInfo -> {});
        model.addAttribute("dataList", dataList);
        return "purchase_payment";
    }

    @ResponseBody
    @PostMapping("/updatePurchasePayment")
    public String updateSalesCollection(PurchasePaymentEntity info, String method, HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        switch (method) {
            case "save":
                info.setStatus(0);//待提交
                if (info.getId() == null) {
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    purchasePaymentMapper.insert(info);
                } else {
                    purchasePaymentMapper.updateById(info);
                }
                break;
            case "submit":
                info.setStatus(1);//待审核
                if (info.getId() == null) {
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    purchasePaymentMapper.insert(info);
                } else {
                    purchasePaymentMapper.updateById(info);
                }
                sysLogService.addLog("purchasePayment", info.getId(), "提交审核", loginUser.getRealName());
                break;
            case "approved":
                PurchasePaymentEntity entity = new PurchasePaymentEntity();
                entity.setId(info.getId());
                entity.setStatus(2);//审核通过
                entity.setAuditUserId(loginUser.getId());
                entity.setAuditUserName(loginUser.getRealName());
                entity.setRejectionContent("");
                purchasePaymentMapper.updateById(entity);
                sysLogService.addLog("purchasePayment", info.getId(), "审核通过", loginUser.getRealName());
                break;
            case "reject":
                PurchasePaymentEntity entity2 = new PurchasePaymentEntity();
                entity2.setId(info.getId());
                entity2.setStatus(3);//审核驳回
                entity2.setAuditUserId(loginUser.getId());
                entity2.setAuditUserName(loginUser.getRealName());
                entity2.setRejectionContent(info.getRejectionContent());
                purchasePaymentMapper.updateById(entity2);
                sysLogService.addLog("purchasePayment", info.getId(), "审核驳回。驳回原因：" + info.getRejectionContent(), loginUser.getRealName());
                break;
            case "rollback":
                PurchasePaymentEntity entity3 = new PurchasePaymentEntity();
                entity3.setId(info.getId());
                entity3.setStatus(3);//待审核
                entity3.setAuditUserId(loginUser.getId());
                entity3.setAuditUserName(loginUser.getRealName());
                entity3.setRejectionContent(info.getRejectionContent());
                purchasePaymentMapper.updateById(entity3);
                sysLogService.addLog("purchasePayment", info.getId(), "取消审核", loginUser.getRealName());
                break;
            default:
        }

        return "{\"msg\":\"操作成功\"}";
    }


}
