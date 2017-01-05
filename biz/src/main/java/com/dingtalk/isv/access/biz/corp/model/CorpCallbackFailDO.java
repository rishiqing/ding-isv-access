package com.dingtalk.isv.access.biz.corp.model;


import java.util.Date;

/**
 * 企业通讯录事件回调失败
 * Created by Wallace on 17-1-4.
 */
public class CorpCallbackFailDO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 企业corpid
     */
    private String corpId;

    /**
     * 套件key
     */
    private String suiteKey;



    private String eventJSON;

    private String tag;

    private String failInfo;

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

    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public String getEventJSON() {
        return eventJSON;
    }

    public void setEventJSON(String eventJSON) {
        this.eventJSON = eventJSON;
    }

    public String getFailInfo() {
        return failInfo;
    }

    public void setFailInfo(String failInfo) {
        this.failInfo = failInfo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
