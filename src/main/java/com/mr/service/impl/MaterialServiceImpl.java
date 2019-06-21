package com.mr.service.impl;

import java.util.Date;
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

	@Override
	public List<Material> selectMaterialList(Material material, Date startTime, Date endTime) {
		List<Material> list = materialMapper.selectMaterialList(material, startTime, endTime);
		return list;
	}

	@Override
	public Material selectById(Integer id) {
		Material material = materialMapper.selectById(id);
		return material;
	}

	@Override
	public void updateById(Material material) {
		materialMapper.updateById(material);
	}

	@Override
	public void deleteListById(List<Integer> list) {
		materialMapper.deleteListById(list);
	}

}
