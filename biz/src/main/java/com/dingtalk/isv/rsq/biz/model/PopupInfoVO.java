package com.dingtalk.isv.rsq.biz.model;

import com.dingtalk.isv.access.biz.corp.model.StaffPopupConfigDO;
import com.dingtalk.isv.access.biz.corp.model.StaffPopupLogDO;

import java.util.*;

/**
 * @author Wallace Mao
 * Date: 2018-10-19 20:06
 */
public class PopupInfoVO {
    private String corpId;
    private Long serviceExpire;
    private Long buyNumber;
    private Long totalNumber;
    private Boolean isAdmin;
    private String specKey;
    private Map<String, StaffPopupConfigDO> popupConfigMap;
    private Map<String, StaffPopupLogDO> muteInfoMap;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getServiceExpire() {
        return serviceExpire;
    }

    public void setServiceExpire(Long serviceExpire) {
        this.serviceExpire = serviceExpire;
    }

    public Long getBuyNumber() {
        return buyNumber;
    }

    public void setBuyNumber(Long buyNumber) {
        this.buyNumber = buyNumber;
    }

    public Long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getSpecKey() {
        return specKey;
    }

    public void setSpecKey(String specKey) {
        this.specKey = specKey;
    }

    public Map<String, StaffPopupConfigDO> getPopupConfigMap() {
        return popupConfigMap;
    }

    public void setPopupConfigMap(Map<String, StaffPopupConfigDO> popupConfigMap) {
        this.popupConfigMap = popupConfigMap;
    }

    public Map<String, StaffPopupLogDO> getMuteInfoMap() {
        return muteInfoMap;
    }

    public void setMuteInfoMap(Map<String, StaffPopupLogDO> muteInfoMap) {
        this.muteInfoMap = muteInfoMap;
    }

    @Override
    public String toString() {
        return "PopupInfoVO{" +
                "corpId='" + corpId + '\'' +
                ", serviceExpire=" + serviceExpire +
                ", buyNumber=" + buyNumber +
                ", totalNumber=" + totalNumber +
                ", isAdmin=" + isAdmin +
                ", specKey='" + specKey + '\'' +
                ", popupConfigMap=" + popupConfigMap +
                ", muteInfoMap=" + muteInfoMap +
                '}';
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("corpId", this.corpId);
        map.put("serviceExpire", this.serviceExpire);
        map.put("buyNumber", this.buyNumber);
        map.put("totalNumber", this.totalNumber);
        map.put("isAdmin", this.isAdmin);
        map.put("specKey", this.specKey);

        Map<String, Object> muteInfo = new HashMap<String, Object>();
        if(this.popupConfigMap != null){
            for(Map.Entry<String, StaffPopupConfigDO> entry : this.popupConfigMap.entrySet()){
                String type = entry.getKey();
                StaffPopupConfigDO configDO = entry.getValue();
                StaffPopupLogDO logDO = this.muteInfoMap.get(type);

                Map<String, Object> objMap = new HashMap<String, Object>();
                if(!map.containsKey("saleQrCodeUrl")){
                    map.put("saleQrCodeUrl", configDO.getSaleQrCodeUrl());
                }
                if(!map.containsKey("salePhoneNumber")){
                    map.put("salePhoneNumber", configDO.getSalePhoneNumber());
                }
                objMap.put("muteExpire", logDO != null ? logDO.getPopupMuteExpire() : 0L);
                muteInfo.put(type, objMap);
            }
        }
        map.put("muteInfo", muteInfo);
        return map;
    }
}
