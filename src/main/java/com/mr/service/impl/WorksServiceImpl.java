package com.mr.service.impl;

import java.util.List;

import com.mr.mapper.WorksMapper;
import com.mr.pojo.Works;
import com.mr.service.WorksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorksServiceImpl implements WorksService {
	@Autowired
	private WorksMapper worksMapper;
	
	@Override
	public void insertList(List<Works> list) {
		worksMapper.insertList(list);
	}

	@Override
	public List<Works> selectWorksList(Works works, String startTime, String endTime) {
		List<Works> list = worksMapper.selectWorksList(works, startTime, endTime);
		return list;
	}

}
