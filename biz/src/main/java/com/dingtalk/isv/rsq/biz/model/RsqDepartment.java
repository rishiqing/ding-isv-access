package com.dingtalk.isv.rsq.biz.model;

/**
 * 日事清系统中的部门，供请求调用
 * Created by Wallace on 2016/11/19.
 */
public class RsqDepartment {

    public RsqDepartment() {}

    public RsqDepartment(Long id, String name){
        this.id = id;
        this.name = name;
    }
    private Long id;
    private String name;
    private String parentId;
    private String teamId;
    private Long orderNum;
    private String outerId;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public String getFromApp() {
        return fromApp;
    }

    public void setFromApp(String fromApp) {
        this.fromApp = fromApp;
    }
}
