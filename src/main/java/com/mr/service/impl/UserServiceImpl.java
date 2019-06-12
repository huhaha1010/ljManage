package com.mr.service.impl;

import com.mr.mapper.UserMapper;
import com.mr.pojo.User;
import com.mr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean isUserNameRegistered(String userName) {
        User user = userMapper.selectByUserName(userName);
        return user != null;
    }

    @Override
    public boolean isUserEmailRegistered(String userEmail) {
        User user = userMapper.selectByUserEmail(userEmail);
        return user != null;
    }

    @Override
    public boolean isUserPhoneRegistered(String userPhone) {
        User user = userMapper.selectByUserPhone(userPhone);
        return user != null;
    }

    @Override
    public void insertSelective(User user) {
        userMapper.insertSelective(user);
    }

    @Override
    public User selectByNamePhoneEmailAndPwd(String attribute, String userPwd) {
        User user = userMapper.selectByNamePhoneEmailAndPwd(attribute, userPwd);
        return user;
    }

    @Override
    public boolean isUser(User user) {
        User userRes = userMapper.selectByIdPhonePwd(user);
        return userRes != null;
    }

    @Override
    public void updatePwdByPhone(String userPwd, String userPhone) {
        userMapper.updatePwdByPhone(userPwd, userPhone);
    }

    @Override
    public boolean isEmailUser(User user) {
        User userRes = userMapper.selectByIdEmailPwd(user);
        return userRes != null;
    }

    @Override
    public void updatePwdByEmail(String userPwd, String userEmail) {
        userMapper.updatePwdByEmail(userPwd, userEmail);
    }
}
