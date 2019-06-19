package com.mr.controller;

import com.alibaba.fastjson.JSONObject;
import com.mr.config.Config;
import com.mr.pojo.User;
import com.mr.service.UserService;
import com.mr.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    private static final Logger log = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/user/register")
    public void insert(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        HttpSession session = request.getSession();
        if (session.getAttribute("phoneCode") == null) {
            log.info("session中没有phoneCode");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "请先进行手机验证");
            ResponseUtil.setResponse(response,jsonObject);
            return;
        }
        String phoneCode = session.getAttribute("phoneCode").toString();
        String userName = request.getParameter("userName");
        String userPwd = request.getParameter("userPwd");
        String userPhone = request.getParameter("userPhone");
        String userEmail = request.getParameter("userEmail");
        String inputPhoneCode = request.getParameter("inputPhoneCode");
        if (userName == null || userName.equals("") ||
        userPwd == null || userPwd.equals("") || userPhone == null || userPhone.equals("") ||
        userEmail == null || userEmail.equals("") || inputPhoneCode == null || inputPhoneCode.equals("")) {
            log.info("用户输入项为空");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "输入项不能为空");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        //判断用户昵称是否已经注册
        if (userService.isUserNameRegistered(userName)) {
            log.info("用户昵称已被注册");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "用户昵称已被注册");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        //判断手机验证码是否一致
        if (!phoneCode.equals(inputPhoneCode)) {
            log.info("手机验证码不一致");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "手机验证码有误");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        //判断手机号是否已被注册
        if (userService.isUserPhoneRegistered(userPhone)) {
            log.info("用户手机号已被注册");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "用户手机号已被注册");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        //判断用户邮箱是否已经被注册
        if (userService.isUserEmailRegistered(userEmail)) {
            log.info("用户邮箱已被注册");
            log.info("用户邮箱为：" + userEmail);
            jsonObject.put("status", "0001");
            jsonObject.put("info", "用户邮箱已被注册");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        //插入用户信息
        User user = new User();
        user.setUserName(userName);
        user.setUserPwd(userPwd);
        user.setUserPhone(userPhone);
        user.setUserEmail(userEmail);
        try {
            userService.insertSelective(user);
            log.info("用户注册成功");
            jsonObject.put("status", "0000");
            jsonObject.put("info", "注册成功");
        } catch (Exception e) {
            log.info("用户注册失败");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "注册失败");
            e.printStackTrace();
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 灵境用户登录
     *
     * 用户输入昵称、手机号或者邮箱和密码登录
     */
    @RequestMapping("/user/login")
    public void selectByNamePhoneEmailAndPwd(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        HttpSession session = request.getSession();
        String attribute = request.getParameter("userNamePhoneEmail");
        String userPwd = request.getParameter("userPwd");

        if (attribute == null || attribute.equals("") || userPwd == null || userPwd.equals("")) {
            log.info("用户输入项为空");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "用户输入项不能为空");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        User user = userService.selectByNamePhoneEmailAndPwd(attribute, userPwd);
        if (user == null) {
            log.info("用户登录信息有误");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "用户登录信息有误");
        } else {
            log.info("登录成功");
            session.setAttribute("userId", user.getUserId());
            jsonObject.put("status", "0000");
            jsonObject.put("info", "登录成功");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 修改密码时手机发送验证码
     */
    @RequestMapping("/user/updatePwdSendPhoneCode")
    public void updatePwdSendPhoneCode(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        HttpSession session = request.getSession();
        String userPhone = request.getParameter("userPhone");
        String verificationCode = getCode();

        if (userPhone == null || userPhone.equals("")) {
            log.info("手机号为空");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "手机号不能为空");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        boolean res = PhoneUtil.sendPhoneCode(userPhone, verificationCode);
        if (res) {
            log.info("手机验证码发送成功");
            jsonObject.put("status", "0000");
            jsonObject.put("info", "手机验证码发送成功");
            session.setAttribute("phoneUpdatePwdCode", verificationCode);
            SessionUtil.removeAttributeInSession(session, "phoneUpdatePwdCode");
        } else {
            log.info("手机验证码发送失败");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "手机验证码发送失败");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 通过手机号修改密码
     */
    @RequestMapping("/user/updatePwdByPhone")
    public void updatePwdByPhone(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        HttpSession session = request.getSession();
        if (session.getAttribute("phoneUpdatePwdCode") == null) {
            log.info("session中phoneUpdatePwdCode为空");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "请先进行手机验证");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        String verificationPhoneCode = session.getAttribute("phoneUpdatePwdCode").toString();
        log.info("手机验证码为：" + verificationPhoneCode);
        String userPhone = request.getParameter("userPhone");
        String oldUserPwd = request.getParameter("oldUserPwd");
        String newUserPwd = request.getParameter("newUserPwd");
        String inputPhoneCode = request.getParameter("inputPhoneCode");
        if (userPhone == null || userPhone.equals("") ||
        oldUserPwd == null || oldUserPwd.equals("") ||
        newUserPwd == null || newUserPwd.equals("") ||
        inputPhoneCode == null || inputPhoneCode.equals("")) {
            log.info("用户输入信息为空");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "输入信息不能为空");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        int userId = Integer.valueOf(session.getAttribute("userId").toString());
        User user = new User();
        user.setUserId(userId);
        user.setUserPhone(userPhone);
        user.setUserPwd(oldUserPwd);
        boolean isUserNow = userService.isUser(user);
        if (!isUserNow) {
            log.info("用户id、手机号、密码三者不一致");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "用户输入信息有误");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        if (!verificationPhoneCode.equals(inputPhoneCode)) {
            log.info("手机验证码错误");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "手机验证码错误");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        try {
            userService.updatePwdByPhone(newUserPwd, userPhone);
            log.info("用户" + userPhone + "修改密码成功");
            jsonObject.put("status", "0000");
            jsonObject.put("info", "修改密码成功");
        } catch (Exception e) {
            log.info("用户" + userPhone + "修改密码失败");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "修改密码失败");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 注册时手机发送验证码
     */
    @RequestMapping("/user/registerSendPhoneCode")
    public void registerSendPhoneCode(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        HttpSession session = request.getSession();
        String userPhone = request.getParameter("userPhone");
        String code = getCode();

        if (userPhone == null || userPhone.equals("")) {
            log.info("手机号为空");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "手机号不能为空");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        boolean res = PhoneUtil.sendPhoneCode(userPhone, code);
        if (res) {
            log.info("手机验证码发送成功");
            jsonObject.put("status", "0000");
            jsonObject.put("info", "手机验证码发送成功");
            session.setAttribute("phoneCode", code);
            SessionUtil.removeAttributeInSession(session, "phoneCode");
        } else {
            log.info("手机验证码发送失败");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "手机验证码发送失败");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 修改密码时邮箱发送验证码
     */
    @RequestMapping("/user/updatePwdSendEmailCode")
    public void updatePwdSendEmailCode(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        HttpSession session = request.getSession();
        String userEmail = request.getParameter("userEmail");
        String vericationCode = getCode();

        if (userEmail == null || userEmail.equals("")) {
            log.info("邮箱信息为空");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "邮箱信息不能为空");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        //发送邮箱验证码
        Map<String, String> para = new HashMap<>();
        para.put("toEmail", userEmail);
        para.put("userName", "默认用户");
        para.put("sub", "灵境用户邮箱验证");
        para.put("content", "如果这封邮件不是你操作的，请忽略。验证码为：" + vericationCode + ",5分钟后失效");
        String emailMsg = EmailUtils.sendemail(Config.SEND_FROM_EMAIL_ACCOUNT, Config.SEND_FROM_EMAIL_PWD, para);
        if (emailMsg.equals("fail")) {
            log.info("邮箱验证码发送失败");
            jsonObject.put("status", "0002");
            jsonObject.put("info", "邮箱验证码发送失败");
        } else {
            log.info("邮箱验证码发送成功");
            jsonObject.put("status", "0000");
            jsonObject.put("info", "邮箱验证码发送成功");
            session.setAttribute("emailUpdatePwdCode", vericationCode);
            SessionUtil.removeAttributeInSession(session, "emailUpdatePwdCode");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 通过邮箱修改密码
     */
    @RequestMapping("/user/updatePwdByEmail")
    public void updatePwdByEmail(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        HttpSession session = request.getSession();
        if (session.getAttribute("emailUpdatePwdCode") == null) {
            log.info("session中emailUpdatePwdCode为空");
            jsonObject.put("status", "0001");
            jsonObject.put("info", "请先进行邮箱验证");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        String emailCode = session.getAttribute("emailUpdatePwdCode").toString();
        log.info("邮箱验证码为：" + emailCode);
        String userEmail = request.getParameter("userEmail");
        String oldUserPwd = request.getParameter("oldUserPwd");
        String newUserPwd = request.getParameter("newUserPwd");
        String inputEmailCode = request.getParameter("inputEmailCode");

        if (session.getAttribute("userId") == null) {
            log.info("用户未登录");
            jsonObject.put("status", "0005");
            jsonObject.put("info", "用户未登录");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        int userId = Integer.valueOf(session.getAttribute("userId").toString());
        User user = new User();
        user.setUserId(userId);
        user.setUserEmail(userEmail);
        user.setUserPwd(oldUserPwd);
        boolean isUserNow = userService.isEmailUser(user);
        if (!isUserNow) {
            log.info("用户id、邮箱和密码三者不一致");
            jsonObject.put("status", "0002");
            jsonObject.put("info", "用户输入信息有误");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        if (!emailCode.equals(inputEmailCode)) {
            log.info("邮箱验证码错误");
            jsonObject.put("status", "0003");
            jsonObject.put("info", "邮箱验证码错误");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }

        try {
            userService.updatePwdByEmail(newUserPwd, userEmail);
            log.info("用户" + userEmail + "修改密码成功");
            jsonObject.put("status", "0000");
            jsonObject.put("info", "修改密码成功");
        } catch (Exception e) {
            log.info("用户" + userEmail + "修改密码失败");
            jsonObject.put("status", "0004");
            jsonObject.put("修改密码失败", "修改密码失败");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 显示用户
     */
    @RequestMapping("/user/list")
    public void list(HttpServletRequest request, HttpServletResponse response) {
        log.info("开始查询用户");
        String userName = request.getParameter("searchUserName");
        String userPhone = request.getParameter("searchUserPhone");
        String userEmail = request.getParameter("searchUserEmail");
        log.info("userName:" + userName);
        User user = new User();
        user.setUserName(userName);
        user.setUserPhone(userPhone);
        user.setUserEmail(userEmail);
        JSONObject jsonObject = new JSONObject();
        List<User> list = null;
        try {
            list = userService.selectUserList(user);
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
     * 根据用户id查询用户
     * @param request
     * @param response
     */
    @RequestMapping("/user/selectEditUser")
    public void selectEditUser(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        User user = userService.selectEditUser(userId);
        if(user != null) {
            jsonObject.put("status", "0000");
            jsonObject.put("user", user);
        } else {
            jsonObject.put("status", "0001");
            jsonObject.put("info", "用户不存在");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    @RequestMapping("/user/checkEmailUnique")
    public void checkEmailUnique(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String userEmail = request.getParameter("userEmail");
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        log.info("修改的userEmail为：" + userEmail);
        User user = userService.selectEditUser(userId);
        if (user.getUserEmail().equals(userEmail)) {
            jsonObject.put("status", "0");
            log.info("输入的邮箱不变");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        if (userService.isUserEmailRegistered(userEmail)) {
            jsonObject.put("status", "1");
            log.info(userEmail + "已被注册");
        } else {
            jsonObject.put("status", "0");
            log.info("邮箱可以注册");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    @RequestMapping("/user/checkPhoneUnique")
    public void checkPhoneUnique(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String userPhone = request.getParameter("userPhone");
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        log.info("修改的userPhone为：" + userPhone);
        User user = userService.selectEditUser(userId);
        if (user.getUserPhone().equals(userPhone)) {
            jsonObject.put("status", "0");
            log.info("输入的手机号不变");
            ResponseUtil.setResponse(response, jsonObject);
            return;
        }
        if (userService.isUserPhoneRegistered(userPhone)) {
            jsonObject.put("status", "1");
            log.info(userPhone + "已被注册");
        } else {
            jsonObject.put("status", "0");
            log.info("手机号可以注册");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 修改用户信息
     * @param request
     * @param response
     */
    @RequestMapping("/user/edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) {
        log.info("开始更新");
        JSONObject jsonObject = new JSONObject();
        Integer userId = Integer.valueOf(request.getParameter("userId"));
        String userName = request.getParameter("userName");
        log.info(userName);
        String userEmail = request.getParameter("userEmail");
        String userPhone = request.getParameter("userPhone");
        log.info(userPhone);
        User user = new User();
        user.setUserId(userId);
        user.setUserEmail(userEmail);
        user.setUserPhone(userPhone);
        user.setUserName(userName);
        try {
            userService.updateUserById(user);
            jsonObject.put("code", "0");
            jsonObject.put("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 删除用户
     * @param request
     * @param response
     */
    @RequestMapping("/user/remove")
    public void remove(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        Integer userId = Integer.valueOf(request.getParameter("ids"));
        log.info(userId);
        try {
            userService.deleteById(userId);
            jsonObject.put("code", "0");
            jsonObject.put("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 判断用户名称是否唯一
     * @param request
     * @param response
     */
    @RequestMapping("/user/checkAddUserNameUnique")
    public void checkUserNameUnique(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        String userName = request.getParameter("userName");
        if (userService.isUserNameRegistered(userName)) {
            jsonObject.put("code", "1");
            jsonObject.put("msg", "名称已被注册");
        } else {
            jsonObject.put("code", "0");
            jsonObject.put("msg", "可以注册");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 检查邮箱是否唯一
     * @param request
     * @param response
     */
    @RequestMapping("/user/checkAddUserEmailUnique")
    public void checkUserEmailUnique(HttpServletRequest request, HttpServletResponse response) {
        log.info("检查用户邮箱是否唯一");
        JSONObject jsonObject = new JSONObject();
        String userEmail = request.getParameter("userEmail");
        log.info("邮箱为：" + userEmail);
        if (userService.isUserEmailRegistered(userEmail)) {
            jsonObject.put("code", "1");
        } else {
            jsonObject.put("code", "0");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    @RequestMapping("/user/checkAddUserPhoneUnique")
    public void checkAddUserPhoneUnique(HttpServletRequest request, HttpServletResponse response) {
        log.info("检查用户手机号是否唯一");
        JSONObject jsonObject = new JSONObject();
        String userPhone = request.getParameter("userPhone");
        log.info("修改的手机号为：" + userPhone);
        if (userService.isUserPhoneRegistered(userPhone)) {
            jsonObject.put("code", "1");
        } else {
            jsonObject.put("code", "0");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    @RequestMapping("/user/adminAdd")
    public void adminAdd(HttpServletRequest request, HttpServletResponse response) {
        log.info("管理员增加用户");
        JSONObject jsonObject = new JSONObject();
        String userName = request.getParameter("userName");
        String userPhone = request.getParameter("userPhone");
        String userEmail = request.getParameter("userEmail");
        String userPwd = request.getParameter("userPwd");
        User user = new User();
        user.setUserName(userName);
        user.setUserPhone(userPhone);
        user.setUserEmail(userEmail);
        user.setUserPwd(userPwd);
        try {
            userService.insertSelective(user);
            jsonObject.put("code", "0");
            jsonObject.put("msg", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", "500");
            jsonObject.put("msg", "操作失败");
        }
        ResponseUtil.setResponse(response, jsonObject);
    }

    /**
     * 创建验证码
     */
    public static String getCode() {
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        return random;
    }
}
