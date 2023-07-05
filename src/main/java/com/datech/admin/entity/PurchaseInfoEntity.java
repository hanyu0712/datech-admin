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
 * 采购合同进项发票表
 */
@Data
@TableName("purchase_info")
public class PurchaseInfoEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String salesContractNo;
    @TableField(value = "contract_no")
    private String contractNo;
    private String supplierName;
    private String projectName;
    private String signSubject;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date signDate;
    private BigDecimal contractAmount;
    private BigDecimal budgetAmount;
    private String purchaseCategory;
    private String purchaseMaterial;
    private String payMode;
    private String receivingDate;
    private String paymentDate;
    private String isAll;
    private String invoiceType;
    private String taxRate;
    private BigDecimal initInvoiceAmount;
    private BigDecimal initPaymentAmount;
    private BigDecimal huaweiAmount;
    private Integer createUserId;
    private String createUserName;
    private String company;
    private Integer auditUserId;
    private String auditUserName;
    private Date createTime;
    private Integer status;
    private String rejectionContent;
}
