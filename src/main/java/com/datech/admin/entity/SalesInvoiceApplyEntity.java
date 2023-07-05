package com.datech.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sales_invoice_apply")
public class SalesInvoiceApplyEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "contract_no")
    private String contractNo;
    private String projectName;
    private String salesSubject;
    private String service;
    private String serviceTaxRate;
    private String equipment;
    private String equipmentTaxRate;

    private BigDecimal invoicedAmount;
    private BigDecimal thisTimeAmount;
    private BigDecimal returnedAmount;
    private BigDecimal expectedReturnAmount;
    private String expectedReturnTime;
    private String arrivalInfo;
    private String receiptInfo;
    private String invoiceContent;
    private String companyName;
    private String taxpayerNo;
    private String address;
    private String bank;
    private String remark;
    private String invoiceSigner;
    private String logisticsNo;
    private String goods;


    private Integer createUserId;
    private String createUserName;
    private String company;
    private Integer auditShangwuId;
    private String auditShangwuName;
    private Integer auditCaiwuId;
    private String auditCaiwuName;
    private Date createTime;
    private Integer status;
    private String rejectionContent;
}
