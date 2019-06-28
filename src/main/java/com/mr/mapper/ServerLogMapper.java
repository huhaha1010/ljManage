package com.mr.mapper;

import com.mr.pojo.ServerLog;

public interface ServerLogMapper {
    ServerLog selectByServerId(Integer serverId);

    void updateByPrimaryKeySelective(ServerLog serverLog);

    void insertSelective(ServerLog serverLog);

    ServerLog selectByPrimaryKey(Integer id);
}
