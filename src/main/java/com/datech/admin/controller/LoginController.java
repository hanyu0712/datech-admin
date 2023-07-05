package com.datech.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datech.admin.entity.SysUserEntity;
import com.datech.admin.mapper.SysUserMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {

    @Resource
    public SysUserMapper sysUserMapper;

    @GetMapping(value = {"/", "/login"})
    public String login() {

        return "login";
    }

    @PostMapping("/login")
    public String login(SysUserEntity user, HttpSession session, Model model) {
        if (!StringUtils.hasLength(user.getUserName()) || !StringUtils.hasLength(user.getPassword())) {
            model.addAttribute("msg", "账号密码不能为空");
            return "login";
        }
        LambdaQueryWrapper<SysUserEntity> userWrapper = Wrappers.lambdaQuery();
        userWrapper.eq(SysUserEntity::getUserName, user.getUserName());
        userWrapper.eq(SysUserEntity::getPassword, user.getPassword());
        List<SysUserEntity> userList = sysUserMapper.selectList(userWrapper);
        if (CollectionUtils.isEmpty(userList)) {
            model.addAttribute("msg", "账号密码错误");
            return "login";
        }
        session.setAttribute("loginUser", userList.get(0));
        return "redirect:/main.html";
    }

    @GetMapping("main.html")
    public String mainPage(HttpSession session, Model model) {

        return "main";
    }

    @GetMapping("registration")
    public String registration(HttpSession session, Model model) {

        return "registration";
    }


}
