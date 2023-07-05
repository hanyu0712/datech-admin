package com.datech.admin.vo;

import com.datech.admin.entity.PurchaseInvoiceEntity;
import com.datech.admin.entity.PurchasePaymentEntity;
import lombok.Data;

/**
 * 销售合同收入确认明细表
 */
@Data
public class PurchaseInvoiceVo extends PurchaseInvoiceEntity {

    private String invoiceDateStr;
    private String receiveDateStr;
}
