package com.mr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mr.pojo.Server;
import com.mr.pojo.User;
import com.mr.service.ServerService;
import com.mr.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

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

	/**
	 * 根据服务器编号查询服务器ip
	 * @param request
	 * @param response
	 */
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

	/**
	 * 查询服务器集合
	 * @param request
	 * @param response
	 */
	@RequestMapping("/server/list")
	public void selectList(HttpServletRequest request, HttpServletResponse response) {
		log.info("开始查询服务器");
		String serverIdStr = request.getParameter("searchServerId");
		String serverCompanyIdStr = request.getParameter("searchServerCompanyId");
		Integer serverId = null;
		Integer serverCompanyId = null;
		if (serverIdStr != null && serverIdStr != "") {
			serverId = Integer.valueOf(serverIdStr);
		}
		if (serverCompanyIdStr != null && serverCompanyIdStr != "") {
			serverCompanyId = Integer.valueOf(serverCompanyIdStr);
		}
		log.info("serverId:" + serverId);
		String serverIp = request.getParameter("searchServerIp");
		String serverName = request.getParameter("searchServerName");
		Server server = new Server();
		server.setServerId(serverId);
		server.setServerIp(serverIp);
		server.setServerName(serverName);
		server.setServerCompanyId(serverCompanyId);
		JSONObject jsonObject = new JSONObject();
		List<Server> list = null;
		try {
			list = serverService.selectServerList(server);
			jsonObject.put("code", 0);
			jsonObject.put("rows", list);
			jsonObject.put("total", list.size());
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("status", "0001");
			jsonObject.put("info", "查询失败");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	/**
	 * 检查服务器ip是否唯一
	 * @param request
	 * @param response
	 */
	@RequestMapping("/server/checkServerIpUnique")
	public void checkServerIpUnique(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		String serverIp = request.getParameter("serverIp");
		Integer serverId = Integer.valueOf(request.getParameter("serverId"));
		log.info("修改的serverIp为：" + serverIp);
		Server server = serverService.selectById(serverId);
		if (server.getServerIp().equals(serverIp)) {
			jsonObject.put("status", "0");
			log.info("输入的ip不变");
			ResponseUtil.setResponse(response, jsonObject);
			return;
		}
		if (serverService.isServerIpUnique(serverIp)) {
			jsonObject.put("status", "1");
			log.info(serverIp + "已被使用");
		} else {
			jsonObject.put("status", "0");
			log.info("ip可以登记");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	/**
	 * 根据id查询服务器
	 * @param request
	 * @param response
	 */
	@RequestMapping("/server/selectById")
	public void selectServerById(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		Integer serverId = Integer.valueOf(request.getParameter("serverId"));
		Server server = serverService.selectById(serverId);
		if(server != null) {
			jsonObject.put("status", "0000");
			jsonObject.put("server", server);
		} else {
			jsonObject.put("status", "0001");
			jsonObject.put("info", "用户不存在");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	/**
	 * 查询更改服务器名称是否唯一
	 * @param request
	 * @param response
	 */
	@RequestMapping("/server/checkServerNameUnique")
	public void checkServerNameUnique(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		String serverName = request.getParameter("serverName");
		Integer serverId = Integer.valueOf(request.getParameter("serverId"));
		log.info("修改的serverName为：" + serverName);
		Server server = serverService.selectById(serverId);
		if (server.getServerName().equals(serverName)) {
			jsonObject.put("status", "0");
			log.info("输入的ip不变");
			ResponseUtil.setResponse(response, jsonObject);
			return;
		}
		if (serverService.isServerNameUnique(serverName)) {
			jsonObject.put("status", "1");
			log.info(serverName + "已被使用");
		} else {
			jsonObject.put("status", "0");
			log.info("名称可以登记");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	/**
	 * 删除服务器信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/server/remove")
	public void remove(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = request.getParameter("ids").split(",");
		List<Integer> list = new ArrayList<>();
		for (String id : ids) {
			log.info("id:" + id);
			list.add(Integer.valueOf(id));
		}
		try {
			serverService.deleteListById(list);
			jsonObject.put("code", "0");
			jsonObject.put("msg", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", "500");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	/**
	 * 添加服务器时判断名称是否唯一
	 * @param request
	 * @param response
	 */
	@RequestMapping("/server/checkAddServerNameUnique")
	public void checkAddServerNameUnique(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		String serverName = request.getParameter("serverName");
		if (serverService.isServerNameUnique(serverName)) {
			jsonObject.put("code", "1");
			jsonObject.put("msg", "名称已被注册");
		} else {
			jsonObject.put("code", "0");
			jsonObject.put("msg", "可以注册");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	/**
	 * 增加服务器时检查ip是否唯一
	 * @param request
	 * @param response
	 */
	@RequestMapping("/server/checkAddServerIpUnique")
	public void checkAddServerIpUnique(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		String serverIp = request.getParameter("serverIp");
		if (serverService.isServerIpUnique(serverIp)) {
			jsonObject.put("code", "1");
			jsonObject.put("msg", "名称已被注册");
		} else {
			jsonObject.put("code", "0");
			jsonObject.put("msg", "可以注册");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	/**
	 * 增加服务器
	 * @param request
	 * @param response
	 */
	@RequestMapping("/server/adminAdd")
	public void adminAdd(HttpServletRequest request, HttpServletResponse response) {
		log.info("管理员增加服务器");
		JSONObject jsonObject = new JSONObject();
		String serverName = request.getParameter("serverName");
		String serverIp = request.getParameter("serverIp");
		Integer serverCompanyId = Integer.valueOf(request.getParameter("serverCompanyId"));
		Server server = new Server();
		server.setServerName(serverName);
		server.setServerIp(serverIp);
		server.setServerCompanyId(serverCompanyId);
		try {
			serverService.insertSelective(server);
			jsonObject.put("code", "0");
			jsonObject.put("msg", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", "500");
			jsonObject.put("msg", "操作失败");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	@RequestMapping("/server/edit")
	public void updateById(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		Integer serverId = Integer.valueOf(request.getParameter("serverId"));
		String serverName = request.getParameter("serverName");
		log.info("serverName:" + serverName);
		String serverIp = request.getParameter("serverIp");
		Integer serverCompanyId = Integer.valueOf(request.getParameter("serverCompanyId"));
		Server server = new Server();
		server.setServerId(serverId);
		server.setServerName(serverName);
		server.setServerIp(serverIp);
		server.setServerCompanyId(serverCompanyId);
		try {
			serverService.updateById(server);
			jsonObject.put("code", "0");
			jsonObject.put("msg", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", "500");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}
}
