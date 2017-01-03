package com.dingtalk.isv.access.biz.corp.model;

import java.util.Date;

/**
 * 企业部门对象
 * Created by wallace 16-12-29.
 */
public class DepartmentDO {

    private Long id;  //自增长主键，不能为准

    private Date gmtCreate;
    private Date gmtModified;

    private String corpId;  //存储部门的公司id
    private Long deptId;  //存储钉钉返回值中的id
    private String name;
    private Long order;
    private Long parentId;

    private Boolean createDeptGroup;
    private Boolean autoAddUser;
    private Boolean deptHiding;

    private String deptPerimits;
    private String userPerimits;

    private Boolean outerDept;
    private String outerPermitDepts;
    private String outerPermitUsers;
    private String orgDeptOwner;
    private String deptManagerUseridList;

    private String rsqId;  //日事清应用的id

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

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getCreateDeptGroup() {
        return createDeptGroup;
    }

    public void setCreateDeptGroup(Boolean createDeptGroup) {
        this.createDeptGroup = createDeptGroup;
    }

    public Boolean getAutoAddUser() {
        return autoAddUser;
    }

    public void setAutoAddUser(Boolean autoAddUser) {
        this.autoAddUser = autoAddUser;
    }

    public Boolean getDeptHiding() {
        return deptHiding;
    }

    public void setDeptHiding(Boolean deptHiding) {
        this.deptHiding = deptHiding;
    }

    public String getDeptPerimits() {
        return deptPerimits;
    }

    public void setDeptPerimits(String deptPerimits) {
        this.deptPerimits = deptPerimits;
    }

    public String getUserPerimits() {
        return userPerimits;
    }

    public void setUserPerimits(String userPerimits) {
        this.userPerimits = userPerimits;
    }

    public Boolean getOuterDept() {
        return outerDept;
    }

    public void setOuterDept(Boolean outerDept) {
        this.outerDept = outerDept;
    }

    public String getOuterPermitDepts() {
        return outerPermitDepts;
    }

    public void setOuterPermitDepts(String outerPermitDepts) {
        this.outerPermitDepts = outerPermitDepts;
    }

    public String getOuterPermitUsers() {
        return outerPermitUsers;
    }

    public void setOuterPermitUsers(String outerPermitUsers) {
        this.outerPermitUsers = outerPermitUsers;
    }

    public String getOrgDeptOwner() {
        return orgDeptOwner;
    }

    public void setOrgDeptOwner(String orgDeptOwner) {
        this.orgDeptOwner = orgDeptOwner;
    }

    public String getDeptManagerUseridList() {
        return deptManagerUseridList;
    }

    public void setDeptManagerUseridList(String deptManagerUseridList) {
        this.deptManagerUseridList = deptManagerUseridList;
    }

    public String getRsqId() {
        return rsqId;
    }

    public void setRsqId(String rsqId) {
        this.rsqId = rsqId;
    }
}
