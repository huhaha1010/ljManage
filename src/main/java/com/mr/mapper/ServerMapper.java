package com.mr.mapper;

import com.mr.pojo.Server;

public interface ServerMapper {
    Server selectByPrimaryKey(Integer serverId);
}