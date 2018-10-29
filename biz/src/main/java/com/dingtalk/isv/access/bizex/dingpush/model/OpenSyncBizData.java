package com.dingtalk.isv.access.bizex.dingpush.model;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-10-28 23:38
 */
public class OpenSyncBizData {
    /**
     * PK
     */
    private Long id;
    private Date gmtCreate;
    private Date gmtModified;

    private String subscribeId;
    private String corpId;
    private String bizId;
    private Long bizType;
    private String bizData;
    private Long openCursor;
    private Long status;

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

    public String getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(String subscribeId) {
        this.subscribeId = subscribeId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Long getBizType() {
        return bizType;
    }

    public void setBizType(Long bizType) {
        this.bizType = bizType;
    }

    public String getBizData() {
        return bizData;
    }

    public void setBizData(String bizData) {
        this.bizData = bizData;
    }

    public Long getOpenCursor() {
        return openCursor;
    }

    public void setOpenCursor(Long openCursor) {
        this.openCursor = openCursor;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OpenSyncBizData{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", subscribeId='" + subscribeId + '\'' +
                ", corpId='" + corpId + '\'' +
                ", bizId='" + bizId + '\'' +
                ", bizType=" + bizType +
                ", bizData='" + bizData + '\'' +
                ", openCursor=" + openCursor +
                ", status=" + status +
                '}';
    }
}
