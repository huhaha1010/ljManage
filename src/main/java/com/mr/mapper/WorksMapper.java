package com.mr.mapper;

import java.util.Date;
import java.util.List;

import com.mr.pojo.Works;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 
 * @author hhl
 *
 */
public interface WorksMapper {
    //批量插入
    void insertList(List<Works> list);

    //查询作品集合
    List<Works> selectWorksList(@PathVariable("works") Works works, @PathVariable("startTime") Date startTime, @PathVariable("endTime") Date endTime);

    //根据id查作品
    Works selectById(Integer id);

    //根据id更改作品
    void updateById(Works works);

    //批量删除作品
    void deleteListById(List<Integer> list);
}