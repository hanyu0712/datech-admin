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
 * 采购合同付款表
 */
@Data
@TableName("purchase_payment")
public class PurchasePaymentEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "contract_no")
    private String contractNo;
    private String supplierName;
    private String projectName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;
    private BigDecimal paymentAmount;
    private String paymentBank;
    private String paymentMode;
    private String receiveCompany;
    private Integer createUserId;
    private String createUserName;
    private String company;
    private Integer auditUserId;
    private String auditUserName;
    private Date createTime;
    private Integer status;
    private String rejectionContent;
}
