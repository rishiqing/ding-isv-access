package com.dingtalk.isv.access.biz.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.service.order.ChargeService;
import com.dingtalk.isv.access.biz.order.dao.OrderEventDao;
import com.dingtalk.isv.access.biz.order.model.OrderEventDO;
import com.dingtalk.isv.access.biz.order.model.helper.OrderEventConverter;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Wallace Mao
 * Date: 2018-10-18 21:41
 */
public class ChargeServiceImpl implements ChargeService {
    private static final Logger bizLogger = LoggerFactory.getLogger("CHARGE_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(ChargeServiceImpl.class);

    @Autowired
    private OrderEventDao orderEventDao;

    @Override
    public ServiceResult<Void> handleChargeEvent(String suiteKey, JSONObject jsonMessage) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("jsonMessage", JSON.toJSONString(jsonMessage))
        ));
        try {
            OrderEventDO orderEvent = OrderEventConverter.json2OrderEventDO(jsonMessage);
            orderEventDao.saveOrUpdateOrderEvent(orderEvent);
            return ServiceResult.success(null);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("jsonMessage", jsonMessage)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("jsonMessage", jsonMessage)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
}
