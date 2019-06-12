package com.mr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.mr.pojo.Server;
import com.mr.service.ServerService;
import com.mr.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务器控制层
 * @author hhl
 *
 */
@Controller
public class ServerController {
	private static final Logger log = Logger.getLogger(ServerController.class);
	
	@Autowired
	private ServerService serverService;
	
	@RequestMapping("/server/selectIpById")
	public void selectById(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		int serverId = Integer.valueOf(request.getParameter("serverId"));
		try {
			Server server = serverService.selectById(serverId);
			log.info("查询服务器ip成功");
			jsonObject.put("status", "0000");
			jsonObject.put("info", "查询服务器ip成功");
			jsonObject.put("serverIp", server.getServerIp());
		} catch (Exception e) {
			log.info("查询服务器ip失败");
			jsonObject.put("status", "0001");
			jsonObject.put("info", "查询服务器ip失败");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}
}
