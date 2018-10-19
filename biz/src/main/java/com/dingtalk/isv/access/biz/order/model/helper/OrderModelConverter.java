package com.dingtalk.isv.access.biz.order.model.helper;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.biz.corp.model.CorpChargeStatusDO;
import com.dingtalk.isv.access.biz.order.model.OrderEventDO;
import com.dingtalk.isv.access.biz.order.model.OrderRsqPushEventDO;
import com.dingtalk.isv.access.biz.order.model.OrderStatusDO;

/**
 * @author Wallace Mao
 * Date: 2018-10-18 21:57
 */
public class OrderModelConverter {
    public static OrderEventDO json2OrderEventDO(JSONObject json){
        if(json == null){
            return null;
        }
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

    public static OrderStatusDO orderEventDO2OrderStatusDO(OrderEventDO orderEvent){
        if(orderEvent == null){
            return null;
        }
        OrderStatusDO orderStatus = new OrderStatusDO();
        orderStatus.setOrderId(orderEvent.getOrderId());
        orderStatus.setSuiteKey(orderEvent.getSuiteKey());
        orderStatus.setBuyCorpId(orderEvent.getBuyCorpId());
        orderStatus.setGoodsCode(orderEvent.getGoodsCode());
        orderStatus.setItemCode(orderEvent.getItemCode());
        orderStatus.setItemName(orderEvent.getItemName());
        orderStatus.setSubQuantity(orderEvent.getSubQuantity());
        orderStatus.setMaxOfPeople(orderEvent.getMaxOfPeople());
        orderStatus.setMinOfPeople(orderEvent.getMinOfPeople());
        orderStatus.setPaidtime(orderEvent.getPaidtime());
        orderStatus.setServiceStopTime(orderEvent.getServiceStopTime());
        orderStatus.setPayFee(orderEvent.getPayFee());
        orderStatus.setOrderCreateSource(orderEvent.getOrderCreateSource());
        orderStatus.setNominalPayFee(orderEvent.getNominalPayFee());
        orderStatus.setDiscountFee(orderEvent.getDiscountFee());
        orderStatus.setDiscount(orderEvent.getDiscount());
        orderStatus.setDistributorCorpId(orderEvent.getDistributorCorpId());
        orderStatus.setDistributorCorpName(orderEvent.getDistributorCorpName());

        return orderStatus;
    }

    public static OrderRsqPushEventDO orderStatusDO2OrderRsqPushEventDO(OrderStatusDO orderStatusDO){
        if(orderStatusDO == null){
            return null;
        }
        OrderRsqPushEventDO event = new OrderRsqPushEventDO();
        event.setSuiteKey(orderStatusDO.getSuiteKey());
        event.setOrderId(orderStatusDO.getOrderId());
        event.setCorpId(orderStatusDO.getBuyCorpId());
        event.setQuantity(orderStatusDO.getSubQuantity());
        event.setServiceStopTime(orderStatusDO.getServiceStopTime());

        return event;
    }

    public static CorpChargeStatusDO orderStatusDO2CorpChargeStatusDO(OrderStatusDO orderStatusDO){
        if(orderStatusDO == null){
            return null;
        }
        CorpChargeStatusDO corpStatus = new CorpChargeStatusDO();
        corpStatus.setSuiteKey(orderStatusDO.getSuiteKey());
        corpStatus.setCorpId(orderStatusDO.getBuyCorpId());
        corpStatus.setCurrentOrderId(orderStatusDO.getOrderId());
        corpStatus.setCurrentGoodsCode(orderStatusDO.getGoodsCode());
        corpStatus.setCurrentItemCode(orderStatusDO.getItemCode());
        corpStatus.setCurrentSubQuantity(orderStatusDO.getSubQuantity());
        corpStatus.setCurrentMaxOfPeople(orderStatusDO.getMaxOfPeople());
        corpStatus.setCurrentMinOfPeople(orderStatusDO.getMinOfPeople());
        corpStatus.setCurrentServiceStopTime(orderStatusDO.getServiceStopTime());

        return corpStatus;
    }
}
