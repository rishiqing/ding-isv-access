package com.dingtalk.isv.rsq.biz.model;

import com.alibaba.fastjson.JSONObject;

/**
 * 日事清系统中的公司，供请求调用
 * Created by Wallace on 2016/11/19.
 */
public class RsqCorp {

    public RsqCorp() {}

    public RsqCorp(Long id, String name){
        this.id = id;
        this.name = name;
    }
    private Long id;
    private String name;
    private String outerId;
    private String note;
    private String fromApp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFromApp() {
        return fromApp;
    }

    public void setFromApp(String fromApp) {
        this.fromApp = fromApp;
    }
}
