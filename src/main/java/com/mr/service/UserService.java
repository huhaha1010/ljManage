package com.mr.service;

import com.mr.pojo.User;

import java.util.List;

public interface UserService {
    //判断用户昵称是否已经注册
    boolean isUserNameRegistered(String userName);

    //判断用户邮箱是否已经注册
    boolean isUserEmailRegistered(String userEmail);

    //判断用户手机号是否已经注册
    boolean isUserPhoneRegistered(String userPhone);

    //插入用户
    void insertSelective(User user);

    //通过用户名、手机号或者邮箱和密码登录
    User selectByNamePhoneEmailAndPwd(String attribute, String userPwd);

    //修改密码时判断是否为该用户
    boolean isUser(User user);

    //根据用户手机号修改密码
    void updatePwdByPhone(String userPwd, String userPhone);

    //邮箱修改密码时判断是否为该用户
    boolean isEmailUser(User user);

    //根据邮箱修改密码
    void updatePwdByEmail(String userPwd, String userEmail);

    //得到所有用户
    List<User> selectUserList();

    //查询选中的用户
    User selectEditUser(Integer userId);
}
