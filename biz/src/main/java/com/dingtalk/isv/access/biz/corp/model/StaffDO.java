package com.dingtalk.isv.access.biz.corp.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 企业员工对象
 * Created by mint on 16-1-22.
 */
public class StaffDO {
    private Date gmtCreate;
    private Date gmtModified;
    private String corpId;
    private String userId;
    private String name;
    private String tel;
    private String workPlace;
    private String remark;
    private String mobile;
    private String email;
    private Boolean active;
    private String orderInDepts;
    private Boolean isAdmin;
    private Boolean isBoss;
    private String dingId;
    private String isLeaderInDepts;
    private Boolean isHide;
    private String department;
    private String position;
    private String avatar;
    private String jobnumber;
    private String extattr;

    private Boolean isSys;    //钉钉免登接口使用code换取用户信息时获取到的用户信息
    private Integer sysLevel;  //钉钉免登接口使用code换取用户信息时获取到的用户信息

    private String rsqUserId;  //日事清中的用户id
    private String rsqUsername;  //日事清中的用户登录名
    private String rsqPassword;  //日事清中的用户登录密码

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getBoss() {
        return isBoss;
    }

    public void setBoss(Boolean boss) {
        isBoss = boss;
    }

    public String getDingId() {
        return dingId;
    }

    public void setDingId(String dingId) {
        this.dingId = dingId;
    }

    public Boolean getHide() {
        return isHide;
    }

    public void setHide(Boolean hide) {
        isHide = hide;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getJobnumber() {
        return jobnumber;
    }

    public void setJobnumber(String jobnumber) {
        this.jobnumber = jobnumber;
    }

    public Boolean getSys() {
        return isSys;
    }

    public void setSys(Boolean sys) {
        isSys = sys;
    }

    public Integer getSysLevel() {
        return sysLevel;
    }

    public void setSysLevel(Integer sysLevel) {
        this.sysLevel = sysLevel;
    }

    public String getOrderInDepts() {
        return orderInDepts;
    }

    public void setOrderInDepts(String orderInDepts) {
        this.orderInDepts = orderInDepts;
    }

    public String getIsLeaderInDepts() {
        return isLeaderInDepts;
    }

    public void setIsLeaderInDepts(String isLeaderInDepts) {
        this.isLeaderInDepts = isLeaderInDepts;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getExtattr() {
        return extattr;
    }

    public void setExtattr(String extattr) {
        this.extattr = extattr;
    }

    public String getRsqUserId() {
        return rsqUserId;
    }

    public void setRsqUserId(String rsqUserId) {
        this.rsqUserId = rsqUserId;
    }

    public String getRsqUsername() {
        return rsqUsername;
    }

    public void setRsqUsername(String rsqUsername) {
        this.rsqUsername = rsqUsername;
    }

    public String getRsqPassword() {
        return rsqPassword;
    }

    public void setRsqPassword(String rsqPassword) {
        this.rsqPassword = rsqPassword;
    }

    @Override
    public String toString() {
        return "StaffDO{" +
                "corpId='" + corpId + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", workPlace='" + workPlace + '\'' +
                ", remark='" + remark + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                ", orderInDepts=" + orderInDepts +
                ", isAdmin=" + isAdmin +
                ", isBoss=" + isBoss +
                ", dingId='" + dingId + '\'' +
                ", isLeaderInDepts=" + isLeaderInDepts +
                ", isHide=" + isHide +
                ", department=" + department +
                ", position='" + position + '\'' +
                ", avatar='" + avatar + '\'' +
                ", jobnumber='" + jobnumber + '\'' +
                ", extattr=" + extattr +
                '}';
    }
}
