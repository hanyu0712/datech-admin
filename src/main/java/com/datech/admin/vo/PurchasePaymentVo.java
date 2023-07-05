package com.datech.admin.vo;

import com.datech.admin.entity.PurchasePaymentEntity;
import com.datech.admin.entity.SalesCollectionEntity;
import lombok.Data;

/**
 * 销售合同收入确认明细表
 */
@Data
public class PurchasePaymentVo extends PurchasePaymentEntity {

    private String paymentDateStr;
}
