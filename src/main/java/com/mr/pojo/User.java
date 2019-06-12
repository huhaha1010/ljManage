package com.mr.pojo;

public class User {
    private Integer userId;

    private String userPwd;

    private String userPhone;

    private String userEmail;

    private String userName;

    private Integer userUploadWorksNum;

    private Integer userUploadMaterialNum;

    public Integer getUserUploadWorksNum() {
        return userUploadWorksNum;
    }

    public void setUserUploadWorksNum(Integer userUploadWorksNum) {
        this.userUploadWorksNum = userUploadWorksNum;
    }

    public Integer getUserUploadMaterialNum() {
        return userUploadMaterialNum;
    }

    public void setUserUploadMaterialNum(Integer userUploadMaterialNum) {
        this.userUploadMaterialNum = userUploadMaterialNum;
    }

    private Integer userPurchaseQuantity;

    private Float userCost;

    private Integer userTotalTpv;

    private Integer userCompanyId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd == null ? null : userPwd.trim();
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getUserPurchaseQuantity() {
        return userPurchaseQuantity;
    }

    public void setUserPurchaseQuantity(Integer userPurchaseQuantity) {
        this.userPurchaseQuantity = userPurchaseQuantity;
    }

    public Float getUserCost() {
        return userCost;
    }

    public void setUserCost(Float userCost) {
        this.userCost = userCost;
    }

    public Integer getUserTotalTpv() {
        return userTotalTpv;
    }

    public void setUserTotalTpv(Integer userTotalTpv) {
        this.userTotalTpv = userTotalTpv;
    }

    public Integer getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(Integer userCompanyId) {
        this.userCompanyId = userCompanyId;
    }
}