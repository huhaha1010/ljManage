package com.mr.mapper;

import java.util.List;

import com.mr.pojo.Works;

/**
 * 
 * @author hhl
 *
 */
public interface WorksMapper {
    //批量插入
    void insertList(List<Works> list);
}