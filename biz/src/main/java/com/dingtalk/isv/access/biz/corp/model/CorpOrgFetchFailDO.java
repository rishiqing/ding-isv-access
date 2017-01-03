package com.dingtalk.isv.access.biz.corp.model;


import com.dingtalk.isv.access.api.enums.suite.CorpFailType;

import java.util.Date;

/**
 * 从钉钉中获取组织结构（部门和人员）信息失败的对象
 * Created by Wallace on 17-1-3.
 */
public class CorpOrgFetchFailDO {

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



    private CorpFailType corpFailType;


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

    public CorpFailType getCorpFailType() {
        return corpFailType;
    }

    public void setCorpFailType(CorpFailType corpFailType) {
        this.corpFailType = corpFailType;
    }

    public String getFailInfo() {
        return failInfo;
    }

    public void setFailInfo(String failInfo) {
        this.failInfo = failInfo;
    }
}
