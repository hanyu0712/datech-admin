package com.datech.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sales_info")
public class SalesInfoEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "contract_no")
    private String contractNo;
    private String customerName;

    private String projectName;

    private String salesSubject;

    private String salesPerson;
    private BigDecimal contractAmount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date signDate;
    private String signStatus;
    private String taxRate;
    private String businessCategory;
    private String productCategory;
    private String payMode;
    private String qualityGuaranteeRatio;
    private String deliveryMonth;
    private String receivingMonth;
    private String logisticsNo;
    private BigDecimal rebateAmount;
    private BigDecimal invoiceAmount;
    private Integer createUserId;
    private String createUserName;
    private String company;
    private Integer auditUserId;
    private String auditUserName;
    private Date createTime;
    private Integer status;
    private String rejectionContent;
    private Integer isDel;
}
