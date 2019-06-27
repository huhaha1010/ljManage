package com.mr.controller;

import com.alibaba.fastjson.JSONObject;
import com.mr.pojo.Server;
import com.mr.pojo.ServerLog;
import com.mr.service.ServerLogService;
import com.mr.service.ServerService;
import com.mr.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ServerLogController {
    private static final Logger log = Logger.getLogger(ServerLogController.class);

    @Autowired
    private ServerLogService serverLogService;

    @Autowired
    private ServerService serverService;

    @RequestMapping("/serverLog/insertServerLog")
    public void insertServerLog(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        Integer serverId = Integer.valueOf(request.getParameter("serverId"));
        Integer behavior = Integer.valueOf(request.getParameter("behavior"));
        HttpSession session = request.getSession();
        log.info("serverLog中sessionId=" + session.getId());
        String adminName = session.getAttribute("adminName").toString();
        log.info("adminName=" + adminName);
        ServerLog serverLog = serverLogService.selectById(serverId);
        if (serverLog != null) {
            log.info("操作日志中还有命令未执行");
            jsonObject.put("code", "301");
            jsonObject.put("msg", "请等待服务器执行命令");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        ServerLog serverLogInsert = new ServerLog();
        serverLogInsert.setServerId(serverId);
        serverLogInsert.setBehavior(behavior);
        serverLogInsert.setState(0);
        serverLogInsert.setAdminName(adminName);
        try {
            serverLogInsert.setOperatingTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            log.info("时间格式错误");
            jsonObject.put("code", "500");
            jsonObject.put("msg", "时间格式错误");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        Server server = new Server();
        server.setServerId(serverId);
        server.setServerState(behavior);
        try {
            serverLogService.insertServerLog(serverLogInsert);
            serverService.updateById(server);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("插入服务器操作日志失败");
            jsonObject.put("code", "500");
            jsonObject.put("msg", "插入服务器操作日志失败");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        log.info("插入服务器操作日志成功");
        jsonObject.put("code", "0");
        jsonObject.put("msg", "操作成功，请等待服务器执行命令");
        ResponseUtil.setResponse(response, jsonObject);
    }

    @RequestMapping("/serverLog/selectServerLog")
    public void selectServerLog(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        Integer serverId = Integer.valueOf(request.getParameter("serverId"));
        ServerLog serverLog = serverLogService.selectById(serverId);
        if (serverLog == null) {
            log.info("操作日志中没有命令需要执行");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "操作日志中没有命令需要执行");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        log.info("操作日志中命令需要执行");
        jsonObject.put("status", "0000");
        jsonObject.put("behavior", serverLog.getBehavior());
        jsonObject.put("id", serverLog.getId());
        ResponseUtil.setResponse(response, jsonObject);
    }

    @RequestMapping("/serverLog/updateLog")
    public void updateLogAndServer(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        Integer id = Integer.valueOf(request.getParameter("id"));
        Integer serverId = Integer.valueOf(request.getParameter("serverId"));
        Integer serverState = Integer.valueOf(request.getParameter("serverState"));
        ServerLog serverLog = new ServerLog();
        serverLog.setId(id);
        serverLog.setState(1);
        try {
            serverLog.setUpdateTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            log.info("时间格式错误");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "时间格式错误");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        Server server = new Server();
        server.setServerId(serverId);
        server.setServerState(serverState);
        try {
            serverLogService.updateById(serverLog);
        } catch (Exception e) {
            log.info("日志与服务器状态更新失败");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "日志与服务器状态更新失败");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        log.info("日志与服务器状态更新成功");
        jsonObject.put("status", "0000");
        jsonObject.put("info", "日志与服务器状态更新成功");
        ResponseUtil.setResponse(response, jsonObject);
    }
}
