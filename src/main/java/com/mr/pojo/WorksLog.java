package com.mr.pojo;

import java.util.Date;

public class WorksLog {
    private Integer id;

    private Integer userId;

    private Integer worksId;

    private String behaviourType;

    private Integer nums;

    private Date operatingTime;

    private Date insertTime;

    private Integer schoolIpNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWorksId() {
        return worksId;
    }

    public void setWorksId(Integer worksId) {
        this.worksId = worksId;
    }

    public String getBehaviourType() {
        return behaviourType;
    }

    public void setBehaviourType(String behaviourType) {
        this.behaviourType = behaviourType == null ? null : behaviourType.trim();
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public Date getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(Date operatingTime) {
        this.operatingTime = operatingTime;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Integer getSchoolIpNum() {
        return schoolIpNum;
    }

    public void setSchoolIpNum(Integer schoolIpNum) {
        this.schoolIpNum = schoolIpNum;
    }
}