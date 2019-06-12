package com.mr.service.impl;

import com.mr.mapper.ServerMapper;
import com.mr.pojo.Server;
import com.mr.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerServiceImpl implements ServerService {
	@Autowired
	private ServerMapper serverMapper;

	@Override
	public Server selectById(int serverId) {
		Server server = serverMapper.selectByPrimaryKey(serverId);
		return server;
	}
}
