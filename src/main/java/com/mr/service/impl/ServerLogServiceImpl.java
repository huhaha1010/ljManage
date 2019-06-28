package com.mr.service.impl;

import com.mr.mapper.ServerLogMapper;
import com.mr.pojo.Server;
import com.mr.pojo.ServerLog;
import com.mr.service.ServerLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerLogServiceImpl implements ServerLogService {
    @Autowired
    private ServerLogMapper serverLogMapper;

    @Override
    public ServerLog selectByServerId(Integer serverId) {
        ServerLog serverLog = serverLogMapper.selectByServerId(serverId);
        return serverLog;
    }

    @Override
    public void updateById(ServerLog serverLog) {
        serverLogMapper.updateByPrimaryKeySelective(serverLog);
    }

    @Override
    public void insertServerLog(ServerLog serverLog) {
        serverLogMapper.insertSelective(serverLog);
    }

    @Override
    public ServerLog selectByPrimaryKey(Integer id) {
        ServerLog serverLog = serverLogMapper.selectByPrimaryKey(id);
        return serverLog;
    }
}
