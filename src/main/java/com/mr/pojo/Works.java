package com.mr.pojo;

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

    private String worksCreateTime;

    private String worksUploadTime;

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

    public String getWorksCreateTime() {
        return worksCreateTime;
    }

    public void setWorksCreateTime(String worksCreateTime) {
        this.worksCreateTime = worksCreateTime == null ? null : worksCreateTime.trim();
    }

    public String getWorksUploadTime() {
        return worksUploadTime;
    }

    public void setWorksUploadTime(String worksUploadTime) {
        this.worksUploadTime = worksUploadTime == null ? null : worksUploadTime.trim();
    }
}