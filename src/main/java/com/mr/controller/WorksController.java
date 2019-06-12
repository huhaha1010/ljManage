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
import com.mr.pojo.Works;
import com.mr.service.WorksService;
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
 * 作品的controller层
 * @author hhl
 *
 */
@Controller
public class WorksController {
	private static final Logger log = Logger.getLogger(WorksController.class);
	
	@Autowired
	private WorksService worksService;
	
	@RequestMapping("/works/insertList")
	public void insertList(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "jsonWorksFile") MultipartFile file) {
		JSONObject jsonObject = new JSONObject();
		log.info("开始读取json文件");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = simpleDateFormat.format(new Date());
		String schoolIpNum = request.getParameter("schoolIpNum").toString();
		String fileOriName = file.getOriginalFilename();
		String fileName = fileOriName.substring(0, fileOriName.lastIndexOf(".")) + date + ".json";
		String jsonFilePath = Config.JSONFILE_PATH + fileName;
		File jsonFile = new File(Config.JSONFILE_PATH, fileName);
		if (!jsonFile.exists()) {
			jsonFile.mkdirs();
		}
		try {
			file.transferTo(jsonFile);
			log.info("云服务器接收json文件成功");
		} catch (IllegalStateException | IOException e) {
			log.info("云服务器接收json文件失败");
			jsonObject.put("msg", "0001");
			jsonObject.put("info", "云服务器接收json文件失败");
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
		List<Works> list = new ArrayList<>();
		for (Object o : jsonArray) {
			JSONObject item = (JSONObject)o;
			Works works = new Works();
			works.setWorksId(item.getString("id"));
			works.setWorksName(item.getString("name"));
			works.setWorksUploaderId(item.getString("uploader"));
			works.setWorksDescription(item.getString("desc"));
			works.setSchoolIpNum(schoolIpNum);
			works.setWorksIcon(item.getString("iconUrl"));
			works.setWorksOwnerId(item.getString("owner"));
			if (item.getBoolean("public")) {
				works.setPermission(1);//选择公开
			} else {
				works.setPermission(0);//选择不公开
			}
			works.setSpace(item.getString("space"));
			works.setWorksCreateTime(item.getString("uploadTime"));
			works.setWorksUploadTime(date);
			works.setWorksTag(item.getJSONArray("tags").toJSONString());
			list.add(works);
			if (list.size() >= 20) {
				try {
					worksService.insertList(list);
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
				worksService.insertList(list);
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
