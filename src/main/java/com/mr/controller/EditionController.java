package com.mr.controller;

import com.alibaba.fastjson.JSONObject;
import com.mr.config.Config;
import com.mr.pojo.Edition;
import com.mr.service.EditionService;
import com.mr.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class EditionController {
    private static final Logger log = Logger.getLogger(EditionController.class);

    @Autowired
    private EditionService editionService;

    /**
     * 查询是否有新版本
     * @param request
     * @param response
     */
    @RequestMapping("/edition/selectNew")
    public void selectNew(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        Integer type = Integer.valueOf(request.getParameter("type"));
        Integer editionId = Integer.valueOf(request.getParameter("editionId"));
        Edition edition = editionService.selectByServerId(type);
        if (edition == null) {
            log.info("版本信息表为空");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "版本信息表为空");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        if (editionId == edition.getId()) {
            log.info("版本号相同");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "版本号相同");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        log.info("存在最新版本");
        jsonObject.put("status", "0000");
        jsonObject.put("info", "存在最新版本");
        jsonObject.put("projectUrl", edition.getProjectUrl());
        jsonObject.put("fileUrl", edition.getFileUrl());
        jsonObject.put("editionId", edition.getId());
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 上传版本并保存版本信息
     * @param request
     * @param response
     */
    @RequestMapping("/edition/insert")
    public void insert(MultipartHttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name");
        String des = request.getParameter("des");
        Integer type = Integer.valueOf(request.getParameter("type"));
        MultipartFile projectFile = request.getFile("projectFile");

        if (projectFile == null) {
            log.info("文件为空");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "文件为空");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        SimpleDateFormat dateFormatFile = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = dateFormatFile.format(new Date());

        String projectFileOriName = projectFile.getOriginalFilename();
        String projectFileName = projectFileOriName.substring(0, projectFileOriName.lastIndexOf(".")) + date + projectFileOriName.substring(projectFileOriName.lastIndexOf("."));
        File projectLogFile = new File(Config.PROJECT_FILE_PATH, projectFileName);
        if (!projectLogFile.exists()) {
            projectLogFile.mkdirs();
        }
        try {
            projectFile.transferTo(projectLogFile);
            log.info("云服务器接收项目文件成功");
        } catch (IllegalStateException | IOException e) {
            log.info("云服务器接收项目失败");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "云服务器接收项目失败");
            e.printStackTrace();
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        Edition edition = new Edition();
        edition.setName(name);
        edition.setDescription(des);
        edition.setType(type);
        edition.setProjectUrl(Config.PROJECT_FILE_URL + projectFileName);
        try {
            edition.setCreateTime(dateFormatFile.parse(date));
        }  catch (ParseException e) {
            e.printStackTrace();
            log.info("日期格式错误");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "日期格式错误");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        try {
            editionService.insertSelective(edition);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("插入版本信息失败");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "插入版本信息失败");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        log.info("插入版本信息成功");
        jsonObject.put("status", "0000");
        jsonObject.put("info", "插入版本信息成功");
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 查询版本
     * @param request
     * @param response
     */
    @RequestMapping("/edition/list")
    public void list(HttpServletRequest request, HttpServletResponse response) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String idStr = request.getParameter("searchEditionId");
        String typeStr = request.getParameter("searchEditionType");
        Integer id = null;
        Integer type = null;
        if (idStr != null && !idStr.equals("")) {
            id = Integer.valueOf(idStr);
        }
        if (typeStr != null && !typeStr.equals("")) {
            type = Integer.valueOf(typeStr);
        }
        String name = request.getParameter("searchEditionName");
        String startTime = request.getParameter("startEditionTime");
        String endTime = request.getParameter("endEditionTime");
        Edition edition = new Edition();
        edition.setId(id);
        edition.setName(name);
        edition.setType(type);
        JSONObject jsonObject = new JSONObject();
        List<Edition> list = null;
        Date dateStart = null;
        Date dateEnd = null;
        try {
            dateStart = (startTime == null || startTime.equals("")) ? null : dateFormat.parse(startTime);
            dateEnd = (endTime == null || endTime.equals("")) ? null : dateFormat.parse(endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            list = editionService.selectEditionList(edition, dateStart, dateEnd);
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
     * 根据id查询版本
     * @param request
     * @param response
     */
    @RequestMapping("edition/selectById")
    public void selectById(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        Integer id = Integer.valueOf(request.getParameter("id"));
        Edition edition = null;
        try {
            edition = editionService.selectById(id);
            jsonObject.put("status", "0000");
            jsonObject.put("msg", "操作成功");
            jsonObject.put("edition", edition);
        } catch (Exception e) {
            jsonObject.put("status", "500");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 修改版本
     * @param request
     * @param response
     */
    @RequestMapping("/edition/edit")
    public void updateById(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        Integer id = Integer.valueOf(request.getParameter("editionId"));
        String name = request.getParameter("editionName");
        String description = request.getParameter("editionDes");
        Integer type = Integer.valueOf(request.getParameter("editionType"));
        String projectUrl = request.getParameter("editionProjectUrl");
        String fileUrl = request.getParameter("editionFileUrl");
        Edition edition = new Edition();
        edition.setId(id);
        edition.setName(name);
        edition.setDescription(description);
        edition.setType(type);
        edition.setProjectUrl(projectUrl);
        edition.setFileUrl(fileUrl);
        try {
            editionService.updateById(edition);
            jsonObject.put("code", "0");
            jsonObject.put("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 删除版本信息，文件并未删除
     * @param request
     * @param response
     */
    @RequestMapping("/edition/remove")
    public void remove(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String[] ids = request.getParameter("ids").split(",");
        List<Integer> list = new ArrayList<>();
        for (String id : ids) {
            list.add(Integer.valueOf(id));
        }
        try {
            editionService.deleteListById(list);
            jsonObject.put("code", "0");
            jsonObject.put("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }
}
