package com.mr.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Material {
    private Integer id;

    private String materialId;

    private String materialName;

    private String materialUploaderId;

    private String materialIcon;

    private Double materialPrice;

    private String materialDescription;

    private Integer materialDownloadNum;

    private Integer materialPwQuantity;

    private String schoolIpNum;

    private String materialOwnerId;

    private Integer permission;

    private String space;

    private String materialTag;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date materialCreateTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date materialUploadTime;

    private String catalog;

    private String materialUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId == null ? null : materialId.trim();
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName == null ? null : materialName.trim();
    }

    public String getMaterialUploaderId() {
        return materialUploaderId;
    }

    public void setMaterialUploaderId(String materialUploaderId) {
        this.materialUploaderId = materialUploaderId == null ? null : materialUploaderId.trim();
    }

    public String getMaterialIcon() {
        return materialIcon;
    }

    public void setMaterialIcon(String materialIcon) {
        this.materialIcon = materialIcon == null ? null : materialIcon.trim();
    }

    public Double getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(Double materialPrice) {
        this.materialPrice = materialPrice;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription == null ? null : materialDescription.trim();
    }

    public Integer getMaterialDownloadNum() {
        return materialDownloadNum;
    }

    public void setMaterialDownloadNum(Integer materialDownloadNum) {
        this.materialDownloadNum = materialDownloadNum;
    }

    public Integer getMaterialPwQuantity() {
        return materialPwQuantity;
    }

    public void setMaterialPwQuantity(Integer materialPwQuantity) {
        this.materialPwQuantity = materialPwQuantity;
    }

    public String getSchoolIpNum() {
        return schoolIpNum;
    }

    public void setSchoolIpNum(String schoolIpNum) {
        this.schoolIpNum = schoolIpNum == null ? null : schoolIpNum.trim();
    }

    public String getMaterialOwnerId() {
        return materialOwnerId;
    }

    public void setMaterialOwnerId(String materialOwnerId) {
        this.materialOwnerId = materialOwnerId == null ? null : materialOwnerId.trim();
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

    public String getMaterialTag() {
        return materialTag;
    }

    public void setMaterialTag(String materialTag) {
        this.materialTag = materialTag == null ? null : materialTag.trim();
    }

    public Date getMaterialCreateTime() {
        return materialCreateTime;
    }

    public void setMaterialCreateTime(Date materialCreateTime) {
        this.materialCreateTime = materialCreateTime == null ? null : materialCreateTime;
    }

    public Date getMaterialUploadTime() {
        return materialUploadTime;
    }

    public void setMaterialUploadTime(Date materialUploadTime) {
        this.materialUploadTime = materialUploadTime == null ? null : materialUploadTime;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog == null ? null : catalog.trim();
    }

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl == null ? null : materialUrl.trim();
    }
}