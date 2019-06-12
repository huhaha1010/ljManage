package com.mr.mapper;

import java.util.List;

import com.mr.pojo.Material;

public interface MaterialMapper {
    //批量插入
    void insertList(List<Material> list);
}