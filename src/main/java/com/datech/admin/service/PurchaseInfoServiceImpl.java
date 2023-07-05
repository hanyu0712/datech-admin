package com.datech.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datech.admin.common.PageService;
import com.datech.admin.entity.PurchaseInfoEntity;
import com.datech.admin.mapper.PurchaseInfosMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PurchaseInfoServiceImpl extends ServiceImpl<PurchaseInfosMapper, PurchaseInfoEntity> implements PageService<PurchaseInfoEntity> {


}
