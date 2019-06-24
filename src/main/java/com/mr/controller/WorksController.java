package com.mr.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mr.config.Config;
import com.mr.pojo.Works;
import com.mr.pojo.WorksLog;
import com.mr.service.WorksLogService;
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

	@Autowired
	private WorksLogService worksLogService;
	
	@RequestMapping("/works/insertList")
	public void insertList(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "jsonWorksFile") MultipartFile file) {
		JSONObject jsonObject = new JSONObject();
		log.info("开始读取json文件");
		DateFormat fileDateFormate = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = fileDateFormate.format(new Date());
		String schoolIpNum = request.getParameter("schoolIpNum");
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
		log.info("works中的jsonArray.size()=" + jsonArray.size());
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
			try {
				works.setWorksCreateTime(fileDateFormate.parse(item.getString("uploadTime")));
				works.setWorksUploadTime(fileDateFormate.parse(date));
			} catch (Exception e) {
				e.printStackTrace();
				jsonObject.put("status", "0001");
				jsonObject.put("info", "转换时间格式失败");
				ResponseUtil.setResponse(response, jsonObject);
				return;
			}
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

	/**
	 * 查询作品
	 * @param request
	 * @param response
	 */
	@RequestMapping("/works/list")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		log.info("开始查询作品");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String worksId = request.getParameter("searchWorksId");
		String worksOwnerId = request.getParameter("searchWorksOwnerId");
		String worksUploaderId = request.getParameter("searchWorksUploaderId");
		String worksName = request.getParameter("searchWorksName");
		String space = request.getParameter("searchSpace");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		log.info("worksId=" + worksId);
		log.info("startTime=" + startTime);
		log.info("endTime=" + endTime);
		Works works = new Works();
		works.setWorksId(worksId);
		works.setWorksUploaderId(worksUploaderId);
		works.setWorksName(worksName);
		works.setSpace(space);
		works.setWorksOwnerId(worksOwnerId);
		JSONObject jsonObject = new JSONObject();
		List<Works> list = null;
		Date dateStart = null;
		Date dateEnd = null;
		try {
			dateStart = (startTime == null || startTime.equals("")) ? null : dateFormat.parse(startTime);
			dateEnd = (endTime == null || endTime.equals("")) ? null : dateFormat.parse(endTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			list = worksService.selectWorksList(works, dateStart, dateEnd);
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

	/**
	 * 根据id查询作品信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/works/selectById")
	public void selectById(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		Integer id = Integer.valueOf(request.getParameter("id"));
		Works works = null;
		try {
			works = worksService.selectById(id);
			jsonObject.put("status", "0000");
			jsonObject.put("msg", "操作成功");
			jsonObject.put("works", works);
		} catch (Exception e) {
			jsonObject.put("status", "500");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	/**
	 * 更改作品信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/works/edit")
	public void updateById(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		Integer id = Integer.valueOf(request.getParameter("id"));
		String worksName = request.getParameter("worksName");
		String worksUploaderId = request.getParameter("worksUploaderId");
		String worksOwnerId = request.getParameter("worksOwnerId");
		Double worksPrice = Double.valueOf(request.getParameter("worksPrice"));
		String worksDescription = request.getParameter("worksDescription");
		String worksIcon = request.getParameter("worksIcon");
		Integer permission = Integer.valueOf(request.getParameter("permission"));
		String space = request.getParameter("space");
		String worksTag = request.getParameter("worksTag");
		Works works = new Works();
		works.setId(id);
		works.setWorksName(worksName);
		works.setWorksUploaderId(worksUploaderId);
		works.setWorksOwnerId(worksOwnerId);
		works.setWorksPrice(worksPrice);
		works.setWorksDescription(worksDescription);
		works.setWorksIcon(worksIcon);
		works.setPermission(permission);
		works.setSpace(space);
		works.setWorksTag(worksTag);
		try {
			worksService.updateById(works);
			jsonObject.put("code", "0");
			jsonObject.put("msg", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", "500");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	@RequestMapping("/works/remove")
	public void remove(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		String[] ids = request.getParameter("ids").split(",");
		List<Integer> list = new ArrayList<>();
		for (String id : ids) {
			log.info("id=" + id);
			list.add(Integer.valueOf(id));
		}
		try {
			worksService.deleteListById(list);
			jsonObject.put("code", "0");
			jsonObject.put("msg", "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("code", "500");
		}
		ResponseUtil.setResponse(response, jsonObject);
	}

	@RequestMapping("/works/insertWorksLog")
	public void insertMaterialLog(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "worksLog") MultipartFile file) {
		JSONObject jsonObject = new JSONObject();
		String schoolIpNum = request.getParameter("schoolIpNum");
		if (schoolIpNum == null || schoolIpNum.equals("")) {
			jsonObject.put("status", "0001");
			jsonObject.put("info", "服务器编号为空");
			ResponseUtil.setResponse(response, jsonObject);
			return;
		}
		SimpleDateFormat dateFormatFile = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat dateFormatDatabase = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String date = dateFormatFile.format(new Date());
		String fileOriName = file.getOriginalFilename();
		String fileName = fileOriName.substring(0, fileOriName.lastIndexOf(".")) + date + ".log";
		String filePath = Config.WORKS_LOG_PATH + fileName;
		File logFile = new File(Config.WORKS_LOG_PATH, fileName);
		if (!logFile.exists()) {
			logFile.mkdirs();
		}
		try {
			file.transferTo(logFile);
			log.info("云服务器接收作品日志成功");
		} catch (IllegalStateException | IOException e) {
			log.info("云服务器接收作品日志失败");
			jsonObject.put("status", "0001");
			jsonObject.put("info", "云服务器接收作品日志失败");
			e.printStackTrace();
			ResponseUtil.setResponse(response, jsonObject);
			return;
		}
		try {
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			String[] arrs = null;
			br.readLine();
			while ((line = br.readLine()) != null) {
				arrs = line.split("\\s+");
				WorksLog worksLog = new WorksLog();
				worksLog.setUserId(Integer.valueOf(arrs[0]));
				worksLog.setWorksId(Integer.valueOf(arrs[1]));
				worksLog.setBehaviourType(arrs[2]);
				worksLog.setNums(Integer.valueOf(arrs[3]));
				try {
					worksLog.setOperatingTime(dateFormatDatabase.parse(arrs[4]));
					worksLog.setInsertTime(dateFormatFile.parse(date));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				worksLog.setSchoolIpNum(Integer.valueOf(schoolIpNum));
				worksLogService.insertSelective(worksLog);
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
			jsonObject.put("status", "0001");
			jsonObject.put("info", "插入数据失败");
			log.info("插入数据失败");
			ResponseUtil.setResponse(response, jsonObject);
			return;
		}

		jsonObject.put("status", "0000");
		jsonObject.put("info", "读取日志成功");
		log.info("读取作品日志完毕");
		ResponseUtil.setResponse(response, jsonObject);
	}
}
