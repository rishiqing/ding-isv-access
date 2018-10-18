package com.dingtalk.isv.access.biz.corp.model;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-10-18 16:38
 */
public class StaffPopupLogDO {
    /**
     * PK
     */
    private Long id;
    private Date gmtCreate;
    private Date gmtModified;

    private String corpId;
    private String userId;
    private String popupType;
    private Long popupMuteExpire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPopupType() {
        return popupType;
    }

    public void setPopupType(String popupType) {
        this.popupType = popupType;
    }

    public Long getPopupMuteExpire() {
        return popupMuteExpire;
    }

    public void setPopupMuteExpire(Long popupMuteExpire) {
        this.popupMuteExpire = popupMuteExpire;
    }

    @Override
    public String toString() {
        return "StaffPopupLogDO{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", corpId='" + corpId + '\'' +
                ", userId='" + userId + '\'' +
                ", popupType='" + popupType + '\'' +
                ", popupMuteExpire=" + popupMuteExpire +
                '}';
    }
}
