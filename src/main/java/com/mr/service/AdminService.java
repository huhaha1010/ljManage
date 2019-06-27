package com.mr.service;

import com.mr.pojo.Admin;

public interface AdminService {
    boolean isAdmin(Admin admin);

    Admin selectAdmin(Admin admin);
}
