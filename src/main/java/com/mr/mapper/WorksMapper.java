package com.mr.mapper;

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
    List<Works> selectWorksList(Works works, @PathVariable("startTime") String startTime, @PathVariable("endTime") String endTime);
}