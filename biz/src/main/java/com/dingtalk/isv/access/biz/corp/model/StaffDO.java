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
    private Map<Long, Long> orderInDepts;
    private Boolean isAdmin;
    private Boolean isBoss;
    private String dingId;
    private Map<Long, Boolean> isLeaderInDepts;
    private Boolean isHide;
    private List<Long> department;
    private String position;
    private String avatar;
    private String jobnumber;
    private Map<String, String> extattr;

    private Boolean isSys;    //钉钉免登接口使用code换取用户信息时获取到的用户信息
    private Integer sysLevel;  //钉钉免登接口使用code换取用户信息时获取到的用户信息

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

    public Map<Long, Long> getOrderInDepts() {
        return orderInDepts;
    }

    public void setOrderInDepts(Map<Long, Long> orderInDepts) {
        this.orderInDepts = orderInDepts;
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

    public Map<Long, Boolean> getIsLeaderInDepts() {
        return isLeaderInDepts;
    }

    public void setIsLeaderInDepts(Map<Long, Boolean> isLeaderInDepts) {
        this.isLeaderInDepts = isLeaderInDepts;
    }

    public Boolean getHide() {
        return isHide;
    }

    public void setHide(Boolean hide) {
        isHide = hide;
    }

    public List<Long> getDepartment() {
        return department;
    }

    public void setDepartment(List<Long> department) {
        this.department = department;
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

    public Map<String, String> getExtattr() {
        return extattr;
    }

    public void setExtattr(Map<String, String> extattr) {
        this.extattr = extattr;
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
