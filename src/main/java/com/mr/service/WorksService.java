package com.mr.service;

import java.util.Date;
import java.util.List;

import com.mr.pojo.Works;

public interface WorksService {
	//批量插入
	void insertList(List<Works> list);

	//查询作品集合
	List<Works> selectWorksList(Works works, Date startTime, Date endTime);

	//根据id查询作品
	Works selectById(Integer id);

	//根据id更新作品
	void updateById(Works works);

	//批量删除
	void deleteListById(List<Integer> list);
}
