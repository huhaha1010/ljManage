package com.mr.service;

import com.mr.pojo.Edition;

import java.util.Date;
import java.util.List;

public interface EditionService {
    Edition selectByServerId(Integer type);

    void insertSelective(Edition edition);

    List<Edition> selectEditionList(Edition edition, Date startTime, Date endTime);

    Edition selectById(Integer id);

    void updateById(Edition edition);

    void deleteListById(List<Integer> list);
}
