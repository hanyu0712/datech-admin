package com.datech.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datech.admin.entity.SalesCollectionEntity;
import com.datech.admin.entity.SysUserEntity;
import com.datech.admin.mapper.SalesCollectionMapper;
import com.datech.admin.service.SysLogServiceImpl;
import com.datech.admin.util.BeanCopierUtil;
import com.datech.admin.vo.SalesCollectionVo;
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
public class SalesCollectionController {
    @Resource
    public SysLogServiceImpl sysLogService;
    @Resource
    public SalesCollectionMapper salesCollectionMapper;

    @ResponseBody
    @GetMapping(value = "/salesCollection/{id}")
    public Object getOne(@PathVariable("id") Integer id) {
        SalesCollectionEntity entity = salesCollectionMapper.selectById(id);
        SalesCollectionVo vo = BeanCopierUtil.copyBean(entity, SalesCollectionVo.class);
        if (entity.getCollectionDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            vo.setCollectionDateStr(sdf.format(entity.getCollectionDate()));
        }
        return vo;
    }

    @GetMapping("/salesCollection")
    public String getList(HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        LambdaQueryWrapper<SalesCollectionEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(SalesCollectionEntity::getContractNo);
        List<SalesCollectionEntity> dataList = new ArrayList<>();
        if ("DK-BJ".equals(loginUser.getCompany()) && "caiwu".equals(loginUser.getDepartment()) && loginUser.getRole() == 1) {
            dataList = salesCollectionMapper.selectList(queryWrapper);

        } else if ("caiwu".equals(loginUser.getDepartment())) {
            if (loginUser.getRole() != 1) {
                queryWrapper.eq(SalesCollectionEntity::getCreateUserId, loginUser.getId());
            }
            dataList = salesCollectionMapper.selectList(queryWrapper);
        }
        //        dataList.forEach(salesContractInfo -> {});
        model.addAttribute("dataList", dataList);
        return "sales_collection";
    }

    @ResponseBody
    @PostMapping("/updateSalesCollection")
    public String updateSalesCollection(SalesCollectionEntity info, String method, HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        switch (method) {
            case "save":
                info.setStatus(0);//待提交
                if (info.getId() == null) {
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    salesCollectionMapper.insert(info);
                } else {
                    salesCollectionMapper.updateById(info);
                }
                break;
            case "submit":
                info.setStatus(1);//待审核
                if (info.getId() == null) {
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    salesCollectionMapper.insert(info);
                } else {
                    salesCollectionMapper.updateById(info);
                }
                sysLogService.addLog("salesCollection", info.getId(), "提交审核", loginUser.getRealName());
                break;
            case "approved":
                SalesCollectionEntity entity = new SalesCollectionEntity();
                entity.setId(info.getId());
                entity.setStatus(2);//审核通过
                entity.setAuditUserId(loginUser.getId());
                entity.setAuditUserName(loginUser.getRealName());
                entity.setRejectionContent("");
                salesCollectionMapper.updateById(entity);
                sysLogService.addLog("salesCollection", info.getId(), "审核通过", loginUser.getRealName());
                break;
            case "reject":
                SalesCollectionEntity entity2 = new SalesCollectionEntity();
                entity2.setId(info.getId());
                entity2.setStatus(3);//审核驳回
                entity2.setAuditUserId(loginUser.getId());
                entity2.setAuditUserName(loginUser.getRealName());
                entity2.setRejectionContent(info.getRejectionContent());
                salesCollectionMapper.updateById(entity2);
                sysLogService.addLog("salesCollection", info.getId(), "审核驳回。驳回原因：" + info.getRejectionContent(), loginUser.getRealName());
                break;
            case "rollback":
                SalesCollectionEntity entity3 = new SalesCollectionEntity();
                entity3.setId(info.getId());
                entity3.setStatus(3);//待审核
                entity3.setAuditUserId(loginUser.getId());
                entity3.setAuditUserName(loginUser.getRealName());
                entity3.setRejectionContent(info.getRejectionContent());
                salesCollectionMapper.updateById(entity3);
                sysLogService.addLog("salesCollection", info.getId(), "取消审核", loginUser.getRealName());
                break;
            default:
        }

        return "{\"msg\":\"操作成功\"}";
    }


}
