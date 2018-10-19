package com.dingtalk.isv.access.biz.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.event.OrderChargeEvent;
import com.dingtalk.isv.access.api.service.order.ChargeService;
import com.dingtalk.isv.access.biz.constant.SystemConstant;
import com.dingtalk.isv.access.biz.corp.dao.CorpChargeStatusDao;
import com.dingtalk.isv.access.biz.corp.model.CorpChargeStatusDO;
import com.dingtalk.isv.access.biz.order.dao.OrderEventDao;
import com.dingtalk.isv.access.biz.order.dao.OrderRsqPushEventDao;
import com.dingtalk.isv.access.biz.order.dao.OrderStatusDao;
import com.dingtalk.isv.access.biz.order.model.OrderEventDO;
import com.dingtalk.isv.access.biz.order.model.OrderRsqPushEventDO;
import com.dingtalk.isv.access.biz.order.model.OrderStatusDO;
import com.dingtalk.isv.access.biz.order.model.helper.OrderModelConverter;
import com.dingtalk.isv.access.biz.suite.dao.SuiteDao;
import com.dingtalk.isv.access.biz.suite.model.SuiteDO;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.httputil.RsqAccountRequestHelper;
import com.google.common.eventbus.AsyncEventBus;
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
    private SuiteDao suiteDao;
    @Autowired
    private OrderEventDao orderEventDao;
    @Autowired
    private OrderStatusDao orderStatusDao;
    @Autowired
    private OrderRsqPushEventDao orderRsqPushEventDao;
    @Autowired
    private CorpChargeStatusDao corpChargeStatusDao;
    @Autowired
    private RsqAccountRequestHelper rsqAccountRequestHelper;
    @Autowired
    //  使用异步eventBus代替同步eventBus
    private AsyncEventBus asyncOrderChargeEventBus;

    @Override
    public ServiceResult<Void> handleChargeEvent(String suiteKey, JSONObject jsonMessage) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("jsonMessage", JSON.toJSONString(jsonMessage))
        ));
        try {
            OrderEventDO orderEvent = OrderModelConverter.json2OrderEventDO(jsonMessage);
            orderEventDao.saveOrUpdateOrderEvent(orderEvent);

            //  使用eventBus异步调用
            OrderChargeEvent event = new OrderChargeEvent();
            event.setSuiteKey(orderEvent.getSuiteKey());
            event.setOrderEventId(orderEvent.getId());

            asyncOrderChargeEventBus.post(event);
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

    /**
     * 充值的主方法，流程如下：
     * 1. 保存OrderStatusDO，初始状态为paid
     * 2. 保存OrderRsqPushEventDO，初始状态为pending
     * 3. 发送后台接口进行充值，充值成功后返回，失败后做报错处理，单独日志打印
     * 4. 更新OrderRsqPushEventDO的状态为success
     * 5. 更新OrderStatusDO的状态为rsq_sync
     * 6. 保存CorpChargeStatusDO
     * @param suiteKey
     * @param eventId
     * @return
     */
    @Override
    public ServiceResult<Void> charge(String suiteKey, Long eventId){
        try{
            OrderEventDO orderEvent = orderEventDao.getOrderEventById(eventId);

            // 保存order status
            OrderStatusDO orderStatus = OrderModelConverter.orderEventDO2OrderStatusDO(orderEvent);
            orderStatus.setStatus(SystemConstant.ORDER_STATUS_PAID);
            orderStatusDao.saveOrUpdateOrderStatus(orderStatus);

            // 保存OrderRsqPushEventDO
            OrderRsqPushEventDO rsqPushEvent = OrderModelConverter.orderStatusDO2OrderRsqPushEventDO(orderStatus);
            rsqPushEvent.setStatus(SystemConstant.ORDER_PUSH_STATUS_PENDING);
            orderRsqPushEventDao.saveOrUpdateOrderRsqPushEvent(rsqPushEvent);

            //发送后台接口进行充值
            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);
            ServiceResult<Void> requestSr = rsqAccountRequestHelper.doCharge(suiteDO, rsqPushEvent);
            if(!requestSr.isSuccess()){
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        "系统异常" ,
                        LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                        LogFormatter.KeyValue.getNew("eventId", eventId)
                ));
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
            }

            // 更新OrderRsqPushEventDO的状态为success
            rsqPushEvent.setStatus(SystemConstant.ORDER_PUSH_STATUS_SUCCESS);
            orderRsqPushEventDao.saveOrUpdateOrderRsqPushEvent(rsqPushEvent);

            // 更新OrderStatusDO的状态为rsq_sync
            orderStatus.setStatus(SystemConstant.ORDER_STATUS_RSQ_SYNC);
            orderStatusDao.saveOrUpdateOrderStatus(orderStatus);

            // 保存CorpChargeStatusDO
            CorpChargeStatusDO corpChargeStatusDO = OrderModelConverter.orderStatusDO2CorpChargeStatusDO(orderStatus);
            corpChargeStatusDO.setStatus(SystemConstant.ORDER_CORP_CHARGE_STATUS_OK);
            //TODO 计算当前人数
            // corpChargeStatusDO.setTotalQuantity();
            corpChargeStatusDao.saveOrUpdateCorpChargeStatus(corpChargeStatusDO);

            return ServiceResult.success(null);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("eventId", eventId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("eventId", eventId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
}
