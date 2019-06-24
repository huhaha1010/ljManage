package com.mr.service.impl;

import com.mr.mapper.WorksLogMapper;
import com.mr.pojo.WorksLog;
import com.mr.service.WorksLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorksLogServiceImpl implements WorksLogService {
    @Autowired
    private WorksLogMapper worksLogMapper;

    @Override
    public void insertSelective(WorksLog worksLog) {
        worksLogMapper.insertSelective(worksLog);
    }
}
