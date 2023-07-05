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
 * 销售合同收款明细表
 */
@Data
@TableName("sales_collection")
public class SalesCollectionEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "contract_no")
    private String contractNo;
    private String customerName;
    private String projectName;
    private String salesPerson;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date collectionDate;
    private BigDecimal collectionAmount;
    private String collectionBank;
    private String collectionMode;
    private String collectionCompany;
    private Integer createUserId;
    private String createUserName;
    private String company;
    private Integer auditUserId;
    private String auditUserName;
    private Date createTime;
    private Integer status;
    private String rejectionContent;
}
