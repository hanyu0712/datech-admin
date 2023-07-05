package com.datech.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.datech.admin.entity.PurchaseInfoEntity;
import com.datech.admin.entity.SysUserEntity;
import com.datech.admin.mapper.PurchaseInfosMapper;
import com.datech.admin.service.PurchaseInfoServiceImpl;
import com.datech.admin.service.SysLogServiceImpl;
import com.datech.admin.util.BeanCopierUtil;
import com.datech.admin.vo.DataTablesJsonVO;
import com.datech.admin.vo.DataTablesResultJsonVO;
import com.datech.admin.vo.PurchaseInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 采购合同统计表
 */
@Slf4j
@Controller
public class PurchaseInfoController {
    @Resource
    public SysLogServiceImpl sysLogService;
    @Resource
    public PurchaseInfoServiceImpl purchaseInfoService;
    @Resource
    public PurchaseInfosMapper purchaseInfoMapper;

    @ResponseBody
    @GetMapping(value = "/purchaseInfo/{id}")
    public Object getOne(@PathVariable("id") Integer id) {
        PurchaseInfoEntity entity = purchaseInfoMapper.selectById(id);
        PurchaseInfoVo vo = BeanCopierUtil.copyBean(entity, PurchaseInfoVo.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (entity.getSignDate() != null) {
            vo.setSignDateStr(sdf.format(entity.getSignDate()));
        }
        return vo;
    }


    @ResponseBody
    @GetMapping(value = "/purchaseInfo2")
//    public DataTablesResultJsonVO purchaseInfo2(@RequestParam String contract) {
    public DataTablesResultJsonVO purchaseInfo2(DataTablesJsonVO vo) {
        DataTablesResultJsonVO dataTablesResultJsonVO = new DataTablesResultJsonVO();
        try {
//            String keyHump = vo.getColumns().get(vo.getOrder().get(0).getColumn().intValue()).getData();
//            String keyLine = StringUtil.humpToLine(keyHump);
//            String sortStr = vo.getOrder().get(0).getDir();
            LambdaQueryWrapper<PurchaseInfoEntity> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.eq(PurchaseInfoEntity::getProjectName, "小订单");
            Page<PurchaseInfoEntity> page = purchaseInfoService.page(new Page<>(1, 10), queryWrapper);
//            Page<PurchaseInfoEntity> page = purchaseInfoService.page(new Page<>(vo.getStart() / vo.getLength() + 1, vo.getLength()), queryWrapper);
            dataTablesResultJsonVO.setData(page.getRecords());
//            dataTablesResultJsonVO.setDraw(vo.getDraw());
            dataTablesResultJsonVO.setDraw(1L);
            dataTablesResultJsonVO.setError("error");
            dataTablesResultJsonVO.setRecordsFiltered(page.getTotal());
            dataTablesResultJsonVO.setRecordsTotal(page.getSize());
            return dataTablesResultJsonVO;
        } catch (Exception ex) {
            dataTablesResultJsonVO.setData(new ArrayList<PurchaseInfoEntity>());
            dataTablesResultJsonVO.setDraw(1L);
            dataTablesResultJsonVO.setError("error");
            dataTablesResultJsonVO.setRecordsFiltered(0L);
            dataTablesResultJsonVO.setRecordsTotal(0L);
            return dataTablesResultJsonVO;
        }

    }


    @GetMapping("/purchaseInfo")
    public String getList(HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        StringBuilder sql = new StringBuilder("SELECT id,sales_contract_no,contract_no,supplier_name,project_name,sign_subject,sign_date,contract_amount,budget_amount,purchase_category,purchase_material,pay_mode,receiving_date,payment_date,is_all,invoice_type,tax_rate,init_invoice_amount,init_payment_amount,huawei_amount,create_user_id,create_user_name,company,audit_user_id,audit_user_name,create_time,status,rejection_content FROM purchase_info where 1=1 ");
        if ("DK-BJ".equals(loginUser.getCompany()) && "caiwu".equals(loginUser.getDepartment()) && loginUser.getRole() == 1) {
            sql.append(" order by sales_contract_no, contract_no desc");
        } else if ("shangwu".equals(loginUser.getDepartment())) {
            /*sql.append(" and company = '");
            sql.append(loginUser.getCompany());
            sql.append("'");*/
            if (loginUser.getRole() != 1) {
                sql.append(" and create_user_id = ");
                sql.append(loginUser.getId());
            }
            sql.append(" order by sales_contract_no, contract_no desc");
        }
        model.addAttribute("dataList", SqlRunner.db().selectList(sql.toString()));
        return "purchase_info";
    }


    @ResponseBody
    @PostMapping("/updatePurchaseInfo")
    public String updateSalesCollection(PurchaseInfoEntity info, String method, HttpSession session, Model model) {
        SysUserEntity loginUser = (SysUserEntity) session.getAttribute("loginUser");
        switch (method) {
            case "save":
                info.setStatus(0);//待提交
                if (info.getId() == null) {
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    purchaseInfoMapper.insert(info);
                } else {
                    purchaseInfoMapper.updateById(info);
                }
                break;
            case "submit":
                info.setStatus(1);//待审核
                if (info.getId() == null) {
                    info.setCreateUserId(loginUser.getId());
                    info.setCreateUserName(loginUser.getRealName());
                    info.setCreateTime(new Date());
                    purchaseInfoMapper.insert(info);
                } else {
                    purchaseInfoMapper.updateById(info);
                }
                sysLogService.addLog("purchaseInfo", info.getId(), "提交审核", loginUser.getRealName());
                break;
            case "approved":
                PurchaseInfoEntity entity = new PurchaseInfoEntity();
                entity.setId(info.getId());
                entity.setStatus(2);//审核通过
                entity.setAuditUserId(loginUser.getId());
                entity.setAuditUserName(loginUser.getRealName());
                entity.setRejectionContent("");
                purchaseInfoMapper.updateById(entity);
                sysLogService.addLog("purchaseInfo", info.getId(), "审核通过", loginUser.getRealName());
                break;
            case "reject":
                PurchaseInfoEntity entity2 = new PurchaseInfoEntity();
                entity2.setId(info.getId());
                entity2.setStatus(3);//审核驳回
                entity2.setAuditUserId(loginUser.getId());
                entity2.setAuditUserName(loginUser.getRealName());
                entity2.setRejectionContent(info.getRejectionContent());
                purchaseInfoMapper.updateById(entity2);
                sysLogService.addLog("purchaseInfo", info.getId(), "审核驳回。驳回原因：" + info.getRejectionContent(), loginUser.getRealName());
                break;
            case "rollback":
                PurchaseInfoEntity entity3 = new PurchaseInfoEntity();
                entity3.setId(info.getId());
                entity3.setStatus(3);//待审核
                entity3.setAuditUserId(loginUser.getId());
                entity3.setAuditUserName(loginUser.getRealName());
                entity3.setRejectionContent(info.getRejectionContent());
                purchaseInfoMapper.updateById(entity3);
                sysLogService.addLog("purchaseInfo", info.getId(), "取消审核", loginUser.getRealName());
                break;
            default:
        }

        return "{\"msg\":\"操作成功\"}";
    }

    @ResponseBody
    @GetMapping("/purchaseInfoSelect")
    public Object getListStatus2() {
        LambdaQueryWrapper<PurchaseInfoEntity> contractWrapper = Wrappers.lambdaQuery();
        contractWrapper.eq(PurchaseInfoEntity::getStatus, 2);
        return purchaseInfoMapper.selectList(contractWrapper);
    }

}
