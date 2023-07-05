package com.datech.admin.vo;

import lombok.Data;

@Data
public class SysUserVo {

    private Integer id;

    private String userName;
    private String realName;

    private String password;
    private String confirmPassword;
    private String newPassword;

    private String company;

    private String department;

    private String role;
}
