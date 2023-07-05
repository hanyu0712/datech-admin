package com.datech.admin.controller;

import com.datech.admin.entity.SysUserEntity;
import com.datech.admin.mapper.SysUserMapper;
import com.datech.admin.util.BeanCopierUtil;
import com.datech.admin.vo.SysUserVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class SysUserController {

    @Resource
    public SysUserMapper sysUserMapper;


    @PostMapping("/user")
    public String user(SysUserVo vo, HttpSession session, Model model) {
        SysUserEntity user = BeanCopierUtil.copyBean(vo, SysUserEntity.class);
        user.setRole(2);
        sysUserMapper.insert(user);
        session.setAttribute("loginUser", user);
        return "redirect:/main.html";
    }

    @ResponseBody
    @GetMapping("/user")
    public String getUser(SysUserEntity user, HttpSession session, Model model) {

        return "main";
    }

    @PostMapping("/user/password")
    public String setPassword(SysUserVo vo, HttpSession session, Model model) {
        SysUserEntity user = new SysUserEntity();
        user.setId(vo.getId());
        user.setPassword(vo.getNewPassword());
        sysUserMapper.updateById(user);
        return "redirect:/main.html";
    }


}
