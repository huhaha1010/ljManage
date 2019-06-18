package com.mr.controller;

import com.alibaba.fastjson.JSONObject;
import com.mr.pojo.Admin;
import com.mr.service.AdminService;
import com.mr.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AdminController {
    private static final Logger log = Logger.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        HttpSession session = request.getSession();
        String adminName = request.getParameter("adminName");
        String adminPwd = request.getParameter("adminPwd");
        Admin admin = new Admin();
        admin.setAdminName(adminName);
        admin.setAdminPwd(adminPwd);
        if (adminService.isAdmin(admin)) {
            log.info(adminName + "登录成功");
            jsonObject.put("status", "0000");
            jsonObject.put("info", "登录成功");
            jsonObject.put("sessionId", session.getId());
            log.info(session.getId());
            session.setAttribute("adminName", adminName);
        } else {
            log.info(adminName + "登录用户名或密码错误");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "登录用户名或密码错误");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    @RequestMapping("/admin/getAdminSession")
    public void getAdminSession(HttpServletRequest request, HttpServletResponse response) {
        log.info("开始获取用户");
        JSONObject jsonObject = new JSONObject();
        HttpSession session = request.getSession();
        log.info("sessionId为：" + session.getId());
        log.info(session.toString());
        String adminName = null;
        try {
            adminName = session.getAttribute("adminName").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(adminName);
        jsonObject.put("adminName", adminName);
        ResponseUtil.setResponse(response, jsonObject);
    }
}
