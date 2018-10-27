package com.dingtalk.isv.access.biz.order.model;

import java.util.Date;

/**
 * 记录发送到日事清进行充值的请求
 * @author Wallace Mao
 * Date: 2018-10-18 16:38
 */
public class OrderRsqPushEventDO {
    /**
     * PK
     */
    private Long id;
    private Date gmtCreate;
    private Date gmtModified;

    private String suiteKey;
    //  订单id
    private Long orderId;
    //  购买该套件企业的corpid
    private String corpId;
    //  订购的具体人数
    private Long quantity;
    //  该订单的服务到期时间
    private Long serviceStopTime;
    //  日事清teamId
    private Long rsqTeamId;
    //  当前状态
    private String status;

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

    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getServiceStopTime() {
        return serviceStopTime;
    }

    public void setServiceStopTime(Long serviceStopTime) {
        this.serviceStopTime = serviceStopTime;
    }

    public Long getRsqTeamId() {
        return rsqTeamId;
    }

    public void setRsqTeamId(Long rsqTeamId) {
        this.rsqTeamId = rsqTeamId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderRsqPushEventDO{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", suiteKey='" + suiteKey + '\'' +
                ", orderId=" + orderId +
                ", corpId='" + corpId + '\'' +
                ", quantity=" + quantity +
                ", serviceStopTime=" + serviceStopTime +
                ", rsqTeamId=" + rsqTeamId +
                ", status='" + status + '\'' +
                '}';
    }
}
