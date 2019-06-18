package com.mr.mapper;

import com.mr.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    User selectByUserName(String userName);

    User selectByUserEmail(String userEmail);

    User selectByUserPhone(String userPhone);

    void insertSelective(User user);

    User selectByNamePhoneEmailAndPwd(@Param("attribute") String attribute, @Param("userPwd") String userPwd);

    User selectByIdPhonePwd(User user);

    void updatePwdByPhone(@Param("userPwd") String userPwd, @Param("userPhone") String userPhone);

    User selectByIdEmailPwd(User user);

    void updatePwdByEmail(@Param("userPwd") String userPwd, @Param("userEmail") String userEmail);

    List<User> selectUserList();

    User selectEditUser(Integer userId);
}