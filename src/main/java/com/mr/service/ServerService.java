package com.mr.service;

import com.mr.pojo.Server;

public interface ServerService {
	//根据服务器编号查询服务器ip
	Server selectById(int serverId);
}
