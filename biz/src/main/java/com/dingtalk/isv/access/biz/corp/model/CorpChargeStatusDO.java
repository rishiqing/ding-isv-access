package com.dingtalk.isv.access.biz.corp.model;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-10-18 16:38
 */
public class CorpChargeStatusDO {
    /**
     * PK
     */
    private Long id;
    private Date gmtCreate;
    private Date gmtModified;

    //  购买该套件企业的corpid
    private String corpId;
    //  企业总人数
    private Long totalQuantity;
    //  当前生效订单id
    private Long currentOrderId;
    //  当前生效订单订购的具体人数
    private Long currentSubQuantity;
    //  当前生效订单购买的商品规格能服务的最多企业人数
    private Long currentMaxOfPeople;
    //  当前生效订单购买的商品规格能服务的最少企业人数
    private Long currentMinOfPeople;
    //  当前生效订单该企业的服务到期时间
    private Long currentServiceStopTime;

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

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Long getCurrentOrderId() {
        return currentOrderId;
    }

    public void setCurrentOrderId(Long currentOrderId) {
        this.currentOrderId = currentOrderId;
    }

    public Long getCurrentSubQuantity() {
        return currentSubQuantity;
    }

    public void setCurrentSubQuantity(Long currentSubQuantity) {
        this.currentSubQuantity = currentSubQuantity;
    }

    public Long getCurrentMaxOfPeople() {
        return currentMaxOfPeople;
    }

    public void setCurrentMaxOfPeople(Long currentMaxOfPeople) {
        this.currentMaxOfPeople = currentMaxOfPeople;
    }

    public Long getCurrentMinOfPeople() {
        return currentMinOfPeople;
    }

    public void setCurrentMinOfPeople(Long currentMinOfPeople) {
        this.currentMinOfPeople = currentMinOfPeople;
    }

    public Long getCurrentServiceStopTime() {
        return currentServiceStopTime;
    }

    public void setCurrentServiceStopTime(Long currentServiceStopTime) {
        this.currentServiceStopTime = currentServiceStopTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CorpChargeStatusDO{" +
                "id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", gmtModified=" + gmtModified +
                ", corpId='" + corpId + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", currentOrderId=" + currentOrderId +
                ", currentSubQuantity=" + currentSubQuantity +
                ", currentMaxOfPeople=" + currentMaxOfPeople +
                ", currentMinOfPeople=" + currentMinOfPeople +
                ", currentServiceStopTime=" + currentServiceStopTime +
                ", status='" + status + '\'' +
                '}';
    }
}
