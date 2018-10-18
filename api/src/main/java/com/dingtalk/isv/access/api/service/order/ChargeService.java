package com.dingtalk.isv.access.api.service.order;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.common.model.ServiceResult;

/**
 * 充值付费相关的service
 * @author Wallace Mao
 * Date: 2018-10-18 21:40
 */
public interface ChargeService {

    ServiceResult<Void> handleChargeEvent(String suiteKey, JSONObject jsonMessage);
}
