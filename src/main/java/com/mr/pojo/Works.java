package com.mr.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Works {
    private Integer id;

    private String worksId;

    private String worksName;

    private String worksUploaderId;

    private Double worksPrice;

    private String worksDescription;

    private Integer worksDownloadNum;

    private Integer worksTpv;

    private Integer pwQuantity;

    private String schoolIpNum;

    private String worksIcon;

    private String worksOwnerId;

    private Integer permission;

    private String space;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date worksCreateTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date worksUploadTime;

    private String worksTag;

    public String getWorksTag() {
        return worksTag;
    }

    public void setWorksTag(String worksTag) {
        this.worksTag = worksTag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorksId() {
        return worksId;
    }

    public void setWorksId(String worksId) {
        this.worksId = worksId == null ? null : worksId.trim();
    }

    public String getWorksName() {
        return worksName;
    }

    public void setWorksName(String worksName) {
        this.worksName = worksName == null ? null : worksName.trim();
    }

    public String getWorksUploaderId() {
        return worksUploaderId;
    }

    public void setWorksUploaderId(String worksUploaderId) {
        this.worksUploaderId = worksUploaderId == null ? null : worksUploaderId.trim();
    }

    public Double getWorksPrice() {
        return worksPrice;
    }

    public void setWorksPrice(Double worksPrice) {
        this.worksPrice = worksPrice;
    }

    public String getWorksDescription() {
        return worksDescription;
    }

    public void setWorksDescription(String worksDescription) {
        this.worksDescription = worksDescription == null ? null : worksDescription.trim();
    }

    public Integer getWorksDownloadNum() {
        return worksDownloadNum;
    }

    public void setWorksDownloadNum(Integer worksDownloadNum) {
        this.worksDownloadNum = worksDownloadNum;
    }

    public Integer getWorksTpv() {
        return worksTpv;
    }

    public void setWorksTpv(Integer worksTpv) {
        this.worksTpv = worksTpv;
    }

    public Integer getPwQuantity() {
        return pwQuantity;
    }

    public void setPwQuantity(Integer pwQuantity) {
        this.pwQuantity = pwQuantity;
    }

    public String getSchoolIpNum() {
        return schoolIpNum;
    }

    public void setSchoolIpNum(String schoolIpNum) {
        this.schoolIpNum = schoolIpNum;
    }

    public String getWorksIcon() {
        return worksIcon;
    }

    public void setWorksIcon(String worksIcon) {
        this.worksIcon = worksIcon == null ? null : worksIcon.trim();
    }

    public String getWorksOwnerId() {
        return worksOwnerId;
    }

    public void setWorksOwnerId(String worksOwnerId) {
        this.worksOwnerId = worksOwnerId == null ? null : worksOwnerId.trim();
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space == null ? null : space.trim();
    }

    public Date getWorksCreateTime() {
        return worksCreateTime;
    }

    public void setWorksCreateTime(Date worksCreateTime) {
        this.worksCreateTime = worksCreateTime == null ? null : worksCreateTime;
    }

    public Date getWorksUploadTime() {
        return worksUploadTime;
    }

    public void setWorksUploadTime(Date worksUploadTime) {
        this.worksUploadTime = worksUploadTime == null ? null : worksUploadTime;
    }
}