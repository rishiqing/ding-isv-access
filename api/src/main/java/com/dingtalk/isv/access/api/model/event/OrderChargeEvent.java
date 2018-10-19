package com.dingtalk.isv.access.api.model.event;

import java.io.Serializable;

/**
 * 企业获取组织结构信息事件
 * Created by Wallace on 2017/1/3.
 */
public class OrderChargeEvent implements Serializable {
    private String suiteKey;
    private Long OrderEventId;

    public String getSuiteKey() {
        return suiteKey;
    }

    public void setSuiteKey(String suiteKey) {
        this.suiteKey = suiteKey;
    }

    public Long getOrderEventId() {
        return OrderEventId;
    }

    public void setOrderEventId(Long orderEventId) {
        OrderEventId = orderEventId;
    }

    @Override
    public String toString() {
        return "OrderChargeEvent{" +
                "suiteKey='" + suiteKey + '\'' +
                ", OrderEventId=" + OrderEventId +
                '}';
    }
}
