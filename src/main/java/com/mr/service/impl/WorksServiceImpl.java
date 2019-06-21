package com.mr.service.impl;

import java.util.Date;
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
	public List<Works> selectWorksList(Works works, Date startTime, Date endTime) {
		List<Works> list = worksMapper.selectWorksList(works, startTime, endTime);
		return list;
	}

	@Override
	public Works selectById(Integer id) {
		Works works = worksMapper.selectById(id);
		return works;
	}

	@Override
	public void updateById(Works works) {
		worksMapper.updateById(works);
	}

	@Override
	public void deleteListById(List<Integer> list) {
		worksMapper.deleteListById(list);
	}

}
