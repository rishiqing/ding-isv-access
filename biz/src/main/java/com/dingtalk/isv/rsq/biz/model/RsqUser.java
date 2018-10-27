package com.dingtalk.isv.rsq.biz.model;

import com.alibaba.fastjson.JSONObject;

/**
 * 日事清系统中的普通用户，供请求调用
 * Created by Wallace on 2016/11/19.
 */
public class RsqUser {

    public RsqUser() {}

    public RsqUser(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    private Long id;
    private String username;
    private String password;
    private String realName;
    private String outerId;
    private String fromClient;
    private String unionId;
    private String loginToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOuterId() {
        return outerId;
    }

    public void setOuterId(String outerId) {
        this.outerId = outerId;
    }

    public String getFromClient() {
        return fromClient;
    }

    public void setFromClient(String fromClient) {
        this.fromClient = fromClient;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    @Override
    public String toString() {
        return "RsqUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", realName='" + realName + '\'' +
                ", outerId='" + outerId + '\'' +
                ", fromClient='" + fromClient + '\'' +
                ", unionId='" + unionId + '\'' +
                ", loginToken='" + loginToken + '\'' +
                '}';
    }
}
