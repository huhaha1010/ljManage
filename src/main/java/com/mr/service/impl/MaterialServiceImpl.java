package com.mr.service.impl;

import java.util.List;

import com.mr.mapper.MaterialMapper;
import com.mr.pojo.Material;
import com.mr.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterialServiceImpl implements MaterialService {
	@Autowired
	private MaterialMapper materialMapper;

	@Override
	public void insertList(List<Material> list) {
		// TODO Auto-generated method stub
		materialMapper.insertList(list);
	}

}
