package com.mr.mapper;

import com.mr.pojo.Server;

import java.util.List;

public interface ServerMapper {
    Server selectByPrimaryKey(Integer serverId);

    List<Server> selectServerList(Server server);

    Server selectByIp(String serverIp);

    Server selectByName(String serverName);

    void deleteById(Integer serverId);

    void insertSelective(Server server);

    void deleteListById(List<Integer> list);

    void updateById(Server server);
}