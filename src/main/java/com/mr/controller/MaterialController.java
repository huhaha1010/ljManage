package com.mr.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mr.config.Config;
import com.mr.pojo.Material;
import com.mr.service.MaterialService;
import com.mr.util.JsonUtils;
import com.mr.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 素材的controller层
 * @author hhl
 *
 */
@Controller
public class MaterialController {
	private static final Logger log = Logger.getLogger(MaterialController.class);
	
	@Autowired
	private MaterialService materialService;
	
	@RequestMapping("/material/insertList")
	public void insertList(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "jsonMaterialFile") MultipartFile file) {
		JSONObject jsonObject = new JSONObject();
		log.info("test");
		log.info("开始读取素材json文件");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = simpleDateFormat.format(new Date());
		String schoolIpNum = request.getParameter("schoolIpNum");
		String fileOriName = file.getOriginalFilename();
		String fileName = fileOriName.substring(0, fileOriName.lastIndexOf(".")) + date + ".json";
		String jsonFilePath = Config.MATERIAL_JSONFILE_PATH + fileName;
		File jsonFile = new File(Config.MATERIAL_JSONFILE_PATH, fileName);
		if (!jsonFile.exists()) {
			jsonFile.mkdirs();
		}
		try {
			file.transferTo(jsonFile);
			log.info("云服务器接收素材json文件成功");
		} catch (IllegalStateException | IOException e) {
			log.info("云服务器接收素材json文件失败");
			jsonObject.put("status", "0001");
			jsonObject.put("info", "云服务器接收素材json文件失败");
			e.printStackTrace();
			ResponseUtil.setResponse(response, jsonObject);
			return;
		}
		String jsonString = JsonUtils.jsonFile2String(jsonFilePath);
		if (!jsonString.startsWith("[")) {
			log.info("json文件格式错误");
			jsonObject.put("msg", "0004");
			jsonObject.put("info", "json文件格式错误");
			ResponseUtil.setResponse(response, jsonObject);
			return;
		}
		log.info("jsonString=" + jsonString);
		JSONArray jsonArray = JSONArray.parseArray(jsonString);
		log.info("jsonArray.size()=" + jsonArray.size());
		//使用集合实现批量插入
		List<Material> list = new ArrayList<>();
		for (Object o : jsonArray) {
			JSONObject item = (JSONObject)o;
			Material material = new Material();
			material.setMaterialId(item.getString("id"));
			material.setMaterialName(item.getString("name"));
			material.setMaterialUploaderId(item.getString("uploader"));
			material.setMaterialDescription(item.getString("materialDescription"));
			material.setSchoolIpNum(schoolIpNum);
			material.setMaterialIcon(item.getString("icon"));
			material.setMaterialOwnerId(item.getString("owner"));
			material.setPermission(item.getInteger("state"));
			material.setSpace(item.getString("space"));
			material.setMaterialTag(item.getJSONArray("tag").toJSONString());
			try {
				material.setMaterialCreateTime(simpleDateFormat.parse(item.getString("time")));
				material.setMaterialUploadTime(simpleDateFormat.parse(date));
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("status", "0001");
				jsonObject.put("info", "时间格式错误");
				ResponseUtil.setResponse(response, jsonObject);
				return;
			}
			material.setCatalog(item.getString("catalog"));
			material.setMaterialUrl(item.getString("url"));
			list.add(material);
			if (list.size() >= 20) {
				try {
					materialService.insertList(list);
				} catch (Exception e) {
					log.info("批量插入出错");
					jsonObject.put("status", "0002");
					jsonObject.put("info", "云服务器保存json文件数据失败");
					e.printStackTrace();
					ResponseUtil.setResponse(response, jsonObject);
					return;
				}
				list.clear();
			}
		}
		log.info("list.size()=" + list.size());
		//最后一次集合中元素数量可能没有达到20
		if (list.size() != 0) {
			try {
				materialService.insertList(list);
				log.info("云服务器保存jso文件数据成功");
				jsonObject.put("status", "0000");
				jsonObject.put("info", "云服务器保存json文件数据成功");
			} catch (Exception e) {
				log.info("批量插入出错");
				jsonObject.put("status", "0003");
				jsonObject.put("info", "云服务器保存json文件数据失败");
				e.printStackTrace();
			}
		}
		log.info("读取json文件完毕");
		ResponseUtil.setResponse(response, jsonObject);
	}

	@RequestMapping("/material/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		log.info("开始查询素材");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String materialId = request.getParameter("searchMaterialId");
		String materialName = request.getParameter("searchMaterialName");
		String materialUploaderId = request.getParameter("searchMaterialUploaderId");
		String materialOwnerId = request.getParameter("searchMaterialOwnerId");
		String space = request.getParameter("searchMaterialSpace");
		String startTime = request.getParameter("startMaterialTime");
		String endTime = request.getParameter("endMaterialTime");
		log.info("materialId=" + materialId);
		log.info("startTime=" + startTime);
		log.info("endTime=" + endTime);
		Material material = new Material();
		material.setMaterialId(materialId);
		material.setMaterialUploaderId(materialUploaderId);
		material.setMaterialName(materialName);
		material.setSpace(space);
		material.setMaterialOwnerId(materialOwnerId);
		JSONObject jsonObject = new JSONObject();
		List<Material> list = null;
		Date dateStart = null;
		Date dateEnd = null;
		try {
			dateStart = (startTime == null || startTime.equals("")) ? null : dateFormat.parse(startTime);
			dateEnd = (endTime == null || endTime.equals("")) ? null : dateFormat.parse(endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			list = materialService.selectMaterialList(material, dateStart, dateEnd);
			jsonObject.put("code", 0);
			jsonObject.put("rows", list);
			jsonObject.put("total", list.size());
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("status", "0001");
			jsonObject.put("info", "查询失败");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	@RequestMapping("/material/selectById")
	public void selectById(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		Integer id = Integer.valueOf(request.getParameter("id"));
		Material material = null;
		try {
			material = materialService.selectById(id);
			jsonObject.put("status", "0000");
			jsonObject.put("msg", "操作成功");
			jsonObject.put("material", material);
		} catch (Exception e) {
			jsonObject.put("status", "500");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	@RequestMapping("/material/edit")
	public void updateById(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		Integer id = Integer.valueOf(request.getParameter("id"));
		String materialName = request.getParameter("materialName");
		String materialUploaderId = request.getParameter("materialUploaderId");
		String materialOwnerId = request.getParameter("materialOwnerId");
		Double materialPrice = Double.valueOf(request.getParameter("materialPrice"));
		String materialDescription = request.getParameter("materialDescription");
		String materialIcon = request.getParameter("materialIcon");
		Integer permission = Integer.valueOf(request.getParameter("permission"));
		String space = request.getParameter("space");
		String materialTag = request.getParameter("materialTag");
		Material material = new Material();
		material.setId(id);
		material.setMaterialName(materialName);
		material.setMaterialUploaderId(materialUploaderId);
		material.setMaterialOwnerId(materialOwnerId);
		material.setMaterialDescription(materialDescription);
		material.setMaterialIcon(materialIcon);
		material.setMaterialPrice(materialPrice);
		material.setPermission(permission);
		material.setSpace(space);
		material.setMaterialTag(materialTag);
		try {
			materialService.updateById(material);
			jsonObject.put("code", "0");
			jsonObject.put("msg", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", "500");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	@RequestMapping("/material/remove")
	public void remove(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = request.getParameter("ids").split(",");
		List<Integer> list = new ArrayList<>();
		for (String id : ids) {
			list.add(Integer.valueOf(id));
		}
		try {
			materialService.deleteListById(list);
			jsonObject.put("code", "0");
			jsonObject.put("msg", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", "500");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}
}
