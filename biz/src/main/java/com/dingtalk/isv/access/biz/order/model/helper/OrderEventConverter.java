package com.dingtalk.isv.access.biz.order.model.helper;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.biz.order.model.OrderEventDO;

/**
 * @author Wallace Mao
 * Date: 2018-10-18 21:57
 */
public class OrderEventConverter {
    public static OrderEventDO json2OrderEventDO(JSONObject json){
        OrderEventDO obj = new OrderEventDO();
        obj.setEventType(json.getString("EventType"));
        obj.setSuiteKey(json.getString("SuiteKey"));
        obj.setBuyCorpId(json.getString("buyCorpId"));
        obj.setGoodsCode(json.getString("goodsCode"));
        obj.setItemCode(json.getString("itemCode"));
        obj.setItemName(json.getString("itemName"));
        obj.setOrderId(json.getLong("orderId"));
        obj.setPaidtime(json.getLong("paidtime"));
        obj.setServiceStopTime(json.getLong("serviceStopTime"));
        obj.setPayFee(json.getLong("payFee"));

        if(json.containsKey("subQuantity")){
            obj.setSubQuantity(json.getLong("subQuantity"));
        }
        if(json.containsKey("maxOfPeople")){
            obj.setMaxOfPeople(json.getLong("maxOfPeople"));
        }
        if(json.containsKey("minOfPeople")){
            obj.setMinOfPeople(json.getLong("minOfPeople"));
        }
        if(json.containsKey("orderCreateSource")){
            obj.setOrderCreateSource(json.getString("orderCreateSource"));
        }
        if(json.containsKey("nominalPayFee")){
            obj.setNominalPayFee(json.getLong("nominalPayFee"));
        }
        if(json.containsKey("discountFee")){
            obj.setDiscountFee(json.getLong("discountFee"));
        }
        if(json.containsKey("discount")){
            obj.setDiscount(json.getDouble("discount"));
        }
        if(json.containsKey("distributorCorpId")){
            obj.setDistributorCorpId(json.getString("distributorCorpId"));
        }
        if(json.containsKey("distributorCorpName")){
            obj.setDistributorCorpName(json.getString("distributorCorpName"));
        }
        return obj;
    }
}
