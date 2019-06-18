package com.mr.service.impl;

import com.mr.mapper.AdminMapper;
import com.mr.pojo.Admin;
import com.mr.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public boolean isAdmin(Admin admin) {
        Admin adminRes = adminMapper.selectByAdmin(admin);
        return adminRes != null;
    }
}
