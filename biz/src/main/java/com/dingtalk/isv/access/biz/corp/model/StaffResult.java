package com.dingtalk.isv.access.biz.corp.model;

import java.util.List;
import java.util.Map;

/**
 * 用于返回前端的企业员工对象，只保留必要信息即可
 * Created by mint on 16-1-22.
 */
public class StaffResult {
    private String corpId;
    private String userId;
    private String name;
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

    private String unionId;

    private String rsqUserId;
    private String rsqUsername;
    private String rsqPassword;

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

    public String getRsqUserId() {
        return rsqUserId;
    }

    public void setRsqUserId(String rsqUserId) {
        this.rsqUserId = rsqUserId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Override
    public String toString() {
        return "StaffDO{" +
                "corpId='" + corpId + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
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
                ", rsqUserId=" + rsqUserId +
                '}';
    }
}
