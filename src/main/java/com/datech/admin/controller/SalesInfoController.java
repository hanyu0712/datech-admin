package com.datech.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datech.admin.entity.SalesInfoEntity;
import com.datech.admin.entity.SalesStatusEntity;
import com.datech.admin.entity.SysUserEntity;
import com.datech.admin.mapper.SalesInfoMapper;
import com.datech.admin.mapper.SalesStatusMapper;
import com.datech.admin.service.SysLogServiceImpl;
import com.datech.admin.util.BeanCopierUtil;
import com.datech.admin.vo.SalesInfoVo;
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

@Controller
public class SalesInfoController {
    @Resource
    public SysLogServiceImpl sysLogService;
    @Resource
    public SalesInfoMapper salesInfoMapper;
    @Resource
    public SalesStatusMapper salesContractStatusMapper;

    @ResponseBody
    @GetMapping(value = "/salesInfo/{id}")
    public Object getOne(@PathVariable("id") Integer id) {
        SalesInfoEntity entity = salesInfoMapper.selectById(id);
        SalesInfoVo vo = BeanCopierUtil.copyBean(entity, SalesInfoVo.class);
        if (entity.getSignDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            vo.setSignDateStr(sdf.format(entity.getSignDate()));
        }
        return vo;
    }

    @GetMapping("/salesInfo")
    public String getList(HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        LambdaQueryWrapper<SalesInfoEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(SalesInfoEntity::getContractNo);
        queryWrapper.ne(SalesInfoEntity::getIsDel, 1);
        List<SalesInfoEntity> dataList = new ArrayList<>();
        if ("DK-BJ".equals(loginUser.getCompany()) && "caiwu".equals(loginUser.getDepartment()) && loginUser.getRole() == 1) {
            dataList = salesInfoMapper.selectList(queryWrapper);

        } else if ("shangwu".equals(loginUser.getDepartment())) {
            if (loginUser.getRole() != 1) {
                queryWrapper.eq(SalesInfoEntity::getCreateUserId, loginUser.getId());
            }
            dataList = salesInfoMapper.selectList(queryWrapper);
        }
//        dataList.forEach(salesContractInfo -> {});
        model.addAttribute("dataList", dataList);
        return "sales_info";
    }

    @ResponseBody
    @PostMapping("/updateSalesInfo")
    public String updateSalesInfo(SalesInfoEntity info, String method, HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        switch (method) {
            case "save":
                info.setStatus(0);//待提交
                if (info.getId() == null) {
                    LambdaQueryWrapper<SalesInfoEntity> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(SalesInfoEntity::getContractNo, info.getContractNo());
                    List<SalesInfoEntity> entityList = salesInfoMapper.selectList(queryWrapper);
                    if (!entityList.isEmpty()) {
                        return "{\"msg\":\"操作失败，合同编号不能重复\", \"code\":500}";
                    }
                    info.setIsDel(0);
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    salesInfoMapper.insert(info);
                } else {
                    salesInfoMapper.updateById(info);
                }
                break;
            case "submit":
                info.setStatus(1);//待审核
                if (info.getId() == null) {
                    LambdaQueryWrapper<SalesInfoEntity> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(SalesInfoEntity::getContractNo, info.getContractNo());
                    List<SalesInfoEntity> entityList = salesInfoMapper.selectList(queryWrapper);
                    if (!entityList.isEmpty()) {
                        return "{\"msg\":\"操作失败，合同编号不能重复\", \"code\":500}";
                    }
                    info.setIsDel(0);
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    salesInfoMapper.insert(info);
                } else {
                    salesInfoMapper.updateById(info);
                }
                sysLogService.addLog("salesInfo", info.getId(), "提交审核", loginUser.getRealName());
                break;
            case "approved":
                SalesInfoEntity entity = new SalesInfoEntity();
                entity.setId(info.getId());
                entity.setStatus(2);//审核通过
                entity.setAuditUserId(loginUser.getId());
                entity.setAuditUserName(loginUser.getRealName());
                entity.setRejectionContent("");
                salesInfoMapper.updateById(entity);
                //生成销售合同执行状态数据
                addSalesContractStatus(salesInfoMapper.selectById(info.getId()));
                sysLogService.addLog("salesInfo", info.getId(), "审核通过", loginUser.getRealName());
                break;
            case "reject":
                SalesInfoEntity entity2 = new SalesInfoEntity();
                entity2.setId(info.getId());
                entity2.setStatus(3);//审核驳回
                entity2.setAuditUserId(loginUser.getId());
                entity2.setAuditUserName(loginUser.getRealName());
                entity2.setRejectionContent(info.getRejectionContent());
                salesInfoMapper.updateById(entity2);
                sysLogService.addLog("salesInfo", info.getId(), "审核驳回。驳回原因：" + info.getRejectionContent(), loginUser.getRealName());
                break;
            case "rollback":
                SalesInfoEntity entity3 = new SalesInfoEntity();
                entity3.setId(info.getId());
                entity3.setStatus(3);//待审核
                entity3.setAuditUserId(loginUser.getId());
                entity3.setAuditUserName(loginUser.getRealName());
                entity3.setRejectionContent(info.getRejectionContent());
                salesInfoMapper.updateById(entity3);
                sysLogService.addLog("salesInfo", info.getId(), "取消审核", loginUser.getRealName());
                break;
            default:
        }

        return "{\"msg\":\"操作成功\"}";
    }

    /**
     * 销售合同审批通过之后，生成销售合同执行状态数据，待技术部补充信息，提交审核
     *
     * @param info
     */
    private void addSalesContractStatus(SalesInfoEntity info) {
        LambdaQueryWrapper<SalesStatusEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SalesStatusEntity::getContractNo, info.getContractNo());
        List<SalesStatusEntity> list = salesContractStatusMapper.selectList(queryWrapper);
        if (list == null || list.isEmpty()) {
            SalesStatusEntity entity = new SalesStatusEntity();
            entity.setContractNo(info.getContractNo());
            entity.setCustomerName(info.getCustomerName());
            entity.setProjectName(info.getProjectName());
            entity.setCompany(info.getCompany());
            entity.setStatus(0);//待提交
            salesContractStatusMapper.insert(entity);
        } else {
            SalesStatusEntity entity = new SalesStatusEntity();
            entity.setCustomerName(info.getCustomerName());
            entity.setProjectName(info.getProjectName());
            salesContractStatusMapper.update(entity, queryWrapper);
        }
    }

    @ResponseBody
    @GetMapping("/salesInfoSelect")
    public Object getListStatus2() {
        LambdaQueryWrapper<SalesInfoEntity> contractWrapper = Wrappers.lambdaQuery();
        contractWrapper.eq(SalesInfoEntity::getStatus, 2);
        return salesInfoMapper.selectList(contractWrapper);
    }

    @ResponseBody
    @PostMapping("/delSalesInfo")
    public Object delSalesInfo(Long id) {
        SalesInfoEntity entity = salesInfoMapper.selectById(id);
        if (entity == null) {
            return "{\"msg\":\"操作失败,数据不存在操作成功\"}";
        }
        entity.setIsDel(1); //删除
        int i = salesInfoMapper.updateById(entity);
        if (i > 0) {
            return "{\"msg\":\"操作成功\"}";
        }
        return "{\"msg\":\"操作失败,数据库异常\"}";
    }

}
