package com.mr.service.impl;

import com.mr.mapper.EditionMapper;
import com.mr.pojo.Edition;
import com.mr.service.EditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EditionServiceImpl implements EditionService {
    @Autowired
    private EditionMapper editionMapper;

    @Override
    public Edition selectByServerId(Integer type) {
        Edition edition = editionMapper.selectByServerId(type);
        return edition;
    }

    @Override
    public void insertSelective(Edition edition) {
        editionMapper.insertSelective(edition);
    }

    @Override
    public List<Edition> selectEditionList(Edition edition, Date startTime, Date endTime) {
        List<Edition> list = editionMapper.selectEditionList(edition, startTime, endTime);
        return list;
    }

    @Override
    public Edition selectById(Integer id) {
        Edition edition = editionMapper.selectById(id);
        return edition;
    }

    @Override
    public void updateById(Edition edition) {
        editionMapper.updateById(edition);
    }

    @Override
    public void deleteListById(List<Integer> list) {
        editionMapper.deleteListById(list);
    }
}
