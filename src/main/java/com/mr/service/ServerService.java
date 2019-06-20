package com.mr.service;

import com.mr.pojo.Server;

import java.util.List;

public interface ServerService {
	//根据服务器编号查询服务器ip
	Server selectById(int serverId);

	//查询服务器集合
	List<Server> selectServerList(Server server);

	//检查服务器ip是否唯一
	boolean isServerIpUnique(String serverIp);

	//检查服务器名称是否唯一
	boolean isServerNameUnique(String serverName);

	//根据服务器id删除服务器信息
	void deleteById(Integer serverId);

	//增加服务器
	void insertSelective(Server server);

	//根据服务器id批量删除服务器信息
	void deleteListById(List<Integer> list);

	//根据服务器id更新
	void updateById(Server server);
}
