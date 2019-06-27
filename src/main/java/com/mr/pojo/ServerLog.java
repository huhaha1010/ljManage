package com.mr.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class ServerLog {
    private Integer id;

    private Integer serverId;

    private Integer behavior;

    private String adminName;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date operatingTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Integer getBehavior() {
        return behavior;
    }

    public void setBehavior(Integer behavior) {
        this.behavior = behavior;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Date getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(Date operatingTime) {
        this.operatingTime = operatingTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}