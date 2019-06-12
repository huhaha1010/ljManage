package com.mr.controller;

import java.io.File;
import java.io.IOException;
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
		JSONArray jsonArray = JSONArray.parseArray(jsonString);
		
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
			material.setMaterialCreateTime(item.getString("time"));
			material.setMaterialUploadTime(date);
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
}
