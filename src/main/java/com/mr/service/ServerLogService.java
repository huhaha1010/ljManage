package com.mr.service;

import com.mr.pojo.ServerLog;

public interface ServerLogService {
    ServerLog selectByServerId(Integer serverId);

    void updateById(ServerLog serverLog);

    void insertServerLog(ServerLog serverLog);

    ServerLog selectByPrimaryKey(Integer id);
}
