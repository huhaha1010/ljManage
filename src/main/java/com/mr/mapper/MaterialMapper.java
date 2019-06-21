package com.mr.mapper;

import java.util.Date;
import java.util.List;

import com.mr.pojo.Material;
import org.springframework.web.bind.annotation.PathVariable;

public interface MaterialMapper {
    //批量插入
    void insertList(List<Material> list);

    //查询素材
    List<Material> selectMaterialList(@PathVariable("material") Material material, @PathVariable("startTime") Date startTime, @PathVariable("endTime") Date endTime);

    //根据id查询素材
    Material selectById(Integer id);

    //更新素材
    void updateById(Material material);

    //删除素材
    void deleteListById(List<Integer> list);
}