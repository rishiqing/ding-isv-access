package com.dingtalk.isv.access.biz.event;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.model.event.OrderChargeEvent;
import com.dingtalk.isv.access.api.service.order.ChargeService;
import com.dingtalk.isv.access.biz.order.dao.OrderEventDao;
import com.dingtalk.isv.access.biz.order.model.OrderEventDO;
import com.dingtalk.isv.common.event.EventListener;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Wallace Mao
 * Date: 2018-10-19 11:26
 */
public class OrderChargeEventListener implements EventListener {
    private static final Logger bizLogger = LoggerFactory.getLogger("CHARGE_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(OrderChargeEventListener.class);

    @Autowired
    private ChargeService chargeService;

    @Subscribe
    @AllowConcurrentEvents  //  event并行执行
    public void listenCorpOrgSyncEvent(OrderChargeEvent event) {
        try{
            String suiteKey = event.getSuiteKey();
            Long eventId = event.getOrderEventId();
            //  充值
            ServiceResult<Void> sr = chargeService.charge(suiteKey, eventId);
            if(!sr.isSuccess()){
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                        LogFormatter.KeyValue.getNew("event", event)
                ));
            }

        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("OrderChargeEvent", JSON.toJSONString(event))
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("OrderChargeEvent", JSON.toJSONString(event))
            ), e);
        }
    }
}
