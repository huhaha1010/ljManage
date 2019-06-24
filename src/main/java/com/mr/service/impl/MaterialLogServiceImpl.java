package com.mr.service.impl;

import com.mr.mapper.MaterialLogMapper;
import com.mr.pojo.MaterialLog;
import com.mr.service.MaterialLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialLogServiceImpl implements MaterialLogService {
    @Autowired
    private MaterialLogMapper materialLogMapper;

    @Override
    public void insert(MaterialLog materialLog) {
        materialLogMapper.insertSelective(materialLog);
    }
}
