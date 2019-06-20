package com.mr.service.impl;

import com.mr.mapper.ServerMapper;
import com.mr.pojo.Server;
import com.mr.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerServiceImpl implements ServerService {
	@Autowired
	private ServerMapper serverMapper;

	@Override
	public Server selectById(int serverId) {
		Server server = serverMapper.selectByPrimaryKey(serverId);
		return server;
	}

	@Override
	public List<Server> selectServerList(Server server) {
		return serverMapper.selectServerList(server);
	}

	@Override
	public boolean isServerIpUnique(String serverIp) {
		Server server = serverMapper.selectByIp(serverIp);
		return server != null;
	}

	@Override
	public boolean isServerNameUnique(String serverName) {
		Server server = serverMapper.selectByName(serverName);
		return server != null;
	}

	@Override
	public void deleteById(Integer serverId) {
		serverMapper.deleteById(serverId);
	}

	@Override
	public void insertSelective(Server server) {
		serverMapper.insertSelective(server);
	}

	@Override
	public void deleteListById(List<Integer> list) {
		serverMapper.deleteListById(list);
	}

	@Override
	public void updateById(Server server) {
		serverMapper.updateById(server);
	}
}
