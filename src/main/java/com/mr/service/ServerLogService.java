package com.mr.service;

import com.mr.pojo.ServerLog;

public interface ServerLogService {
    ServerLog selectById(Integer serverId);

    void updateById(ServerLog serverLog);

    void insertServerLog(ServerLog serverLog);
}
