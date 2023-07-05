package com.datech.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("sales_invoice_goods")
public class SalesInvoiceGoodsEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer invoiceApplyId;

    @TableField(value = "goods_name")
    private String goodsName;
    private String goodsModel;
    private String goodsUnit;
    private BigDecimal goodsNumber;
    private BigDecimal goodsPrice;
    private BigDecimal goodsAmount;
}
