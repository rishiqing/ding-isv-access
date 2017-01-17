package com.dingtalk.isv.access.biz.corp.model;

/**
 * 用于记录用户id映射关系的staff简化对象
 * Created by mint on 16-1-22.
 */
public class StaffIdsDO {
    private String userId;

    private String rsqUserId;  //日事清中的用户id

    private String emplId;
    private String avatar;
    private String name;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRsqUserId() {
        return rsqUserId;
    }

    public void setRsqUserId(String rsqUserId) {
        this.rsqUserId = rsqUserId;
    }

    public String getEmplId() {
        return emplId;
    }

    public void setEmplId(String emplId) {
        this.emplId = emplId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StaffIdsDO{" +
                "userId='" + userId + '\'' +
                ", rsqUserId=" + rsqUserId +
                ", emplId=" + emplId +
                ", avatar=" + avatar +
                ", name=" + name +
                '}';
    }
}
