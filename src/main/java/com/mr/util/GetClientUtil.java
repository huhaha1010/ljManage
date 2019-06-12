package com.mr.util;

import javax.servlet.http.HttpServletRequest;

public class GetClientUtil {
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.equals("") && "unkonwn".equalsIgnoreCase(ip)) {
//            int index = ip.indexOf(",");
//            if (index != -1) {
//                return ip.substring(0, index);
//            } else {
//                return ip;
//            }
            ip = request.getHeader("Proxy-Client-Ip");
        }
//        ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.equals("") && "unkonwn".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.equals("") && "unkonwn".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
