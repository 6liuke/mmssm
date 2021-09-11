package com.bjpowernode.controller;

import com.bjpowernode.pojo.Admin;
import com.bjpowernode.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;

/**
 * @author liuke
 * @create 2021-09-07  18:17
 */

@Controller
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    AdminService adminService;


    @RequestMapping("/login")
    public String login(String name, String pwd, HttpServletRequest request){

        Admin admin = adminService.login(name, pwd);
        if (admin!=null){
            request.setAttribute("admin",admin);
            return "main";
        }else {
            request.setAttribute("errmsg","用户名或密码错误");
            return "login";
        }
    }
}
