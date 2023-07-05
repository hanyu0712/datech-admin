package com.datech.admin.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.datech.admin.entity.SalesIncomeConfirmEntity;
import lombok.Data;

/**
 * 销售合同收入确认明细表
 */
@Data
@TableName("sales_contract_income_confirm")
public class SalesIncomeConfirmVo extends SalesIncomeConfirmEntity {

    private String invoiceDateStr;
}
