package com.mr.mapper;

import com.mr.pojo.ServerLog;

public interface ServerLogMapper {
    ServerLog selectById(Integer serverId);

    void updateByPrimaryKeySelective(ServerLog serverLog);

    void insertSelective(ServerLog serverLog);
}
