package com.mr.mapper;

import com.mr.pojo.Admin;

public interface AdminMapper {
    /**
     * 判断是否存在admin
     * @param admin
     * @return
     */
    Admin selectByAdmin(Admin admin);
}
