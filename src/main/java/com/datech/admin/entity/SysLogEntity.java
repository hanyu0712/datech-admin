package com.datech.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_log")
public class SysLogEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "target_table")
    private String targetTable;

    private Integer targetId;

    private String content;

    private String optUserName;

    private Date createTime;
}
