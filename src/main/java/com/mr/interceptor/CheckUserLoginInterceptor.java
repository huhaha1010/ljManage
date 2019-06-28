package com.mr.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.mr.annotation.IsCheckUserLogin;
import com.mr.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CheckUserLoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = Logger.getLogger(CheckUserLoginInterceptor.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        IsCheckUserLogin auth = handlerMethod.getMethodAnnotation(IsCheckUserLogin.class);

        /**
         * 如果在controller中的方法没有使用IsCheckUserLogin注解或者check=false,
         * 就不需要判断在请求时用户是否已经登录.
         */
        if (auth == null || !auth.check()) {
            return true;
        }

        HttpSession session = request.getSession();
        String adminName = (String) session.getAttribute("adminName");     //判断用户是否登录,如果adminName==null,则没有登录
        if (adminName != null) {
            return true;
        } else {
            log.info("没有登录,跳转到登录页面");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", "0001");
            jsonObject.put("code", "400");
            jsonObject.put("info", "登录失效");
            ResponseUtil.setResponse(response, jsonObject);
            return false;
        }
    }
}
