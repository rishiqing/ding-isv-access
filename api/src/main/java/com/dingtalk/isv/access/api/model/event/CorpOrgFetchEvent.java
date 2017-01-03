package com.dingtalk.isv.access.api.model.event;

import java.io.Serializable;

/**
 * 企业获取组织结构信息事件
 * Created by Wallace on 2017/1/3.
 */
public class CorpOrgFetchEvent implements Serializable {
    private String corpId;
    private String suiteKey;

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
    @Override
    public String toString() {
        return "CorpOrgFetchEvent{" +
                ", corpId='" + corpId + '\'' +
                ", suiteKey='" + suiteKey + '\'' +
                '}';
    }
}
