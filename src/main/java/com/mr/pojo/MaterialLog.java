package com.mr.pojo;

import java.util.Date;

public class MaterialLog {
    private Integer id;

    private Integer ljUserId;

    private Integer materialId;

    private String behaviorTypes;

    private Integer downloadNum;

    private Date operatingTime;

    private Date insertTime;

    private Integer schoolIpNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLjUserId() {
        return ljUserId;
    }

    public void setLjUserId(Integer ljUserId) {
        this.ljUserId = ljUserId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getBehaviorTypes() {
        return behaviorTypes;
    }

    public void setBehaviorTypes(String behaviorTypes) {
        this.behaviorTypes = behaviorTypes == null ? null : behaviorTypes.trim();
    }

    public Integer getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(Integer downloadNum) {
        this.downloadNum = downloadNum;
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