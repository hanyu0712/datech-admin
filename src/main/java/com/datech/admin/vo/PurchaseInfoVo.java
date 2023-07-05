package com.datech.admin.vo;

import com.datech.admin.entity.PurchaseInfoEntity;
import com.datech.admin.entity.PurchaseInvoiceEntity;
import lombok.Data;

/**
 * 销售合同收入确认明细表
 */
@Data
public class PurchaseInfoVo extends PurchaseInfoEntity {

    private String signDateStr;
}
