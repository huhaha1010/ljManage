package com.mr.service;

import java.util.List;

import com.mr.pojo.Works;

public interface WorksService {
	//批量插入
	void insertList(List<Works> list);

	//查询作品集合
	List<Works> selectWorksList(Works works, String startTime, String endTime);
}
