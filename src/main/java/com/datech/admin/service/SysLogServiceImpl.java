package com.datech.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datech.admin.entity.SysLogEntity;
import com.datech.admin.mapper.SysLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLogEntity> {

    @Resource
    public SysLogMapper sysLogMapper;

    public int addLog(String table, Integer targetId, String content, String optUserName) {
        SysLogEntity log = new SysLogEntity();
        log.setTargetTable(table);
        log.setTargetId(targetId);
        log.setContent(content);
        log.setOptUserName(optUserName);
        log.setCreateTime(new Date());
        return sysLogMapper.insert(log);
    }

}
