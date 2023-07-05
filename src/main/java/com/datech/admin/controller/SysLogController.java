package com.datech.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datech.admin.entity.SysLogEntity;
import com.datech.admin.service.SysLogServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class SysLogController {
    @Resource
    public SysLogServiceImpl sysLogService;

    @ResponseBody
    @GetMapping(value = "/syslog/{table}/{targetId}")
    public Object getOne(@PathVariable("table") String table, @PathVariable("targetId") Integer targetId) {
        LambdaQueryWrapper<SysLogEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysLogEntity::getTargetTable, table);
        queryWrapper.eq(SysLogEntity::getTargetId, targetId);
        queryWrapper.orderByAsc(SysLogEntity::getCreateTime);
        List<SysLogEntity> dataList = sysLogService.list(queryWrapper);
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dataList.forEach(log -> {
            builder.append("<div>");
            builder.append(sdf.format(log.getCreateTime()));
            builder.append("&nbsp;&nbsp;&nbsp;");
            builder.append("操作人："+log.getOptUserName());
            builder.append("&nbsp;&nbsp;&nbsp;");
            builder.append("操作内容："+log.getContent());
            builder.append("</div>");
        });
        return "{\"msg\":\""+builder.toString()+"\"}";
    }


}
