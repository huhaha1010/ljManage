package com.mr.mapper;

import com.mr.pojo.Edition;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

public interface EditionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Edition record);

    int insertSelective(Edition record);

    Edition selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Edition record);

    int updateByPrimaryKey(Edition record);

    Edition selectByServerId(Integer type);

    List<Edition> selectEditionList(@PathVariable("edition") Edition edition, @PathVariable("startTime") Date startTime, @PathVariable("endTime") Date endTime);

    Edition selectById(Integer id);

    void updateById(Edition edition);

    void deleteListById(List<Integer> list);
}