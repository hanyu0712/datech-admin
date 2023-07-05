package com.datech.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datech.admin.entity.SalesStatusEntity;
import com.datech.admin.entity.SysUserEntity;
import com.datech.admin.mapper.SalesStatusMapper;
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

@Controller
public class SalesStatusController {
    @Resource
    public SysLogServiceImpl sysLogService;
    @Resource
    public SalesStatusMapper salesContractStatusMapper;

    @ResponseBody
    @GetMapping(value = "/salesStatus/{id}")
    public Object getOne(@PathVariable("id") Integer id) {
        return salesContractStatusMapper.selectById(id);
    }

    @GetMapping("/salesStatus")
    public String getList(HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        LambdaQueryWrapper<SalesStatusEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(SalesStatusEntity::getContractNo);
        List<SalesStatusEntity> dataList = new ArrayList<>();
        if ("DK-BJ".equals(loginUser.getCompany()) && "caiwu".equals(loginUser.getDepartment()) && loginUser.getRole() == 1) {
            dataList = salesContractStatusMapper.selectList(queryWrapper);

        } else if ("jishu".equals(loginUser.getDepartment())) {
            if (loginUser.getRole() != 1) {
                queryWrapper.and(wq -> {
                    wq.eq(SalesStatusEntity::getCreateUserId, loginUser.getId());
                    wq.or().isNull(SalesStatusEntity::getCreateUserId);
                });
            }
            dataList = salesContractStatusMapper.selectList(queryWrapper);
        }
        model.addAttribute("dataList", dataList);
        return "sales_status";
    }

    @ResponseBody
    @PostMapping("/updateSalesStatus")
    public String addSalesContractStatus(SalesStatusEntity info, String method, HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        switch (method) {
            case "save":
                info.setStatus(0);//待提交
                info.setCreateUserId(loginUser.getId());
                info.setCreateUserName(loginUser.getRealName());
                info.setCreateTime(new Date());
                salesContractStatusMapper.updateById(info);
                break;
            case "submit":
                info.setStatus(1);//待审核
                info.setCreateUserId(loginUser.getId());
                info.setCreateUserName(loginUser.getRealName());
                info.setCreateTime(new Date());
                salesContractStatusMapper.updateById(info);
                sysLogService.addLog("salesStatus", info.getId(), "提交审核", loginUser.getRealName());
                break;
            case "approved":
                SalesStatusEntity entity = new SalesStatusEntity();
                entity.setId(info.getId());
                entity.setStatus(2);//审核通过
                entity.setAuditUserId(loginUser.getId());
                entity.setAuditUserName(loginUser.getRealName());
                entity.setRejectionContent("");
                salesContractStatusMapper.updateById(entity);
                sysLogService.addLog("salesStatus", info.getId(), "审核通过", loginUser.getRealName());
                break;
            case "reject":
                SalesStatusEntity entity2 = new SalesStatusEntity();
                entity2.setId(info.getId());
                entity2.setStatus(3);//审核驳回
                entity2.setAuditUserId(loginUser.getId());
                entity2.setAuditUserName(loginUser.getRealName());
                entity2.setRejectionContent(info.getRejectionContent());
                salesContractStatusMapper.updateById(entity2);
                sysLogService.addLog("salesStatus", info.getId(), "审核驳回。驳回原因：" + info.getRejectionContent(), loginUser.getRealName());
                break;
            case "rollback":
                SalesStatusEntity entity3 = new SalesStatusEntity();
                entity3.setId(info.getId());
                entity3.setStatus(3);//待审核
                entity3.setAuditUserId(loginUser.getId());
                entity3.setAuditUserName(loginUser.getRealName());
                entity3.setRejectionContent(info.getRejectionContent());
                salesContractStatusMapper.updateById(entity3);
                sysLogService.addLog("salesStatus", info.getId(), "取消审核", loginUser.getRealName());
                break;
            default:
        }

        return "{\"msg\":\"操作成功\"}";
    }


}
