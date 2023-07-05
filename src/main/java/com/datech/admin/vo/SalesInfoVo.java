package com.datech.admin.vo;

import com.datech.admin.entity.SalesCollectionEntity;
import com.datech.admin.entity.SalesInfoEntity;
import lombok.Data;

/**
 * 销售合同收入确认明细表
 */
@Data
public class SalesInfoVo extends SalesInfoEntity {

    private String signDateStr;
}
