package com.datech.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售合同收入确认明细表
 */
@Data
@TableName("sales_income_confirm")
public class SalesIncomeConfirmEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "contract_no")
    private String contractNo;
    private String projectName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    private String invoiceCode;
    private String invoiceNo;
    private String companyName;
    private BigDecimal noTaxAmount;
    private BigDecimal taxAmount;
    private String content;
    private String managerName;
    private Integer createUserId;
    private String createUserName;
    private String company;
    private Integer auditUserId;
    private String auditUserName;
    private Date createTime;
    private Integer status;
    private String rejectionContent;
}
