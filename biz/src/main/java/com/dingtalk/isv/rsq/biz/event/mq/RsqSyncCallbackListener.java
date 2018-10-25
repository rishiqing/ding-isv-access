package com.dingtalk.isv.rsq.biz.event.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.event.mq.SuiteCallBackMessage;
import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.api.service.message.SendMessageService;
import com.dingtalk.isv.access.api.service.suite.AppManageService;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.service.CorpCallbackQueueService;
import com.dingtalk.isv.access.biz.util.MessageUtil;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.corp.MessageBody;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import java.util.ArrayList;
import java.util.List;

/**
 * 与日事清相关的部门和人员信息同步成功之后的回调事件的mq listener
 * Created by Wallace on 2017/4/26.
 */
public class RsqSyncCallbackListener implements MessageListener {
    private static final Logger bizLogger = LoggerFactory.getLogger("MQ_LISTENER_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(RsqSyncCallbackListener.class);
    @Autowired
    private CorpCallbackQueueService corpCallbackQueueService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private StaffManageService staffManageService;
    @Autowired
    private AppManageService appManageService;

    private String appId;
    private String sendMessageBody;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSendMessageBody() {
        return sendMessageBody;
    }

    public void setSendMessageBody(String sendMessageBody) {
        this.sendMessageBody = sendMessageBody;
    }

    @Override
    public void onMessage(Message message) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("message", message)
        ));
        mainLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("message", message)
        ));
        try {

            ActiveMQMapMessage objMsg = (ActiveMQMapMessage) message;
            String suiteKey = objMsg.getString("suiteKey");
            String corpId = objMsg.getString("corpId");

            System.out.println("消息内容是：suiteKey:" + suiteKey + ",corpId:" + corpId);

//            ServiceResult<List<StaffVO>> staffDOList = staffManageService.getStaffListByCorpId(corpId);
            ServiceResult<List<String>> staffListSr = staffManageService.getStaffUserIdListByCorpId(corpId);
            if(!staffListSr.isSuccess()){
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        LogFormatter.KeyValue.getNew("get staff user id list error when send rsq sync mq message", "suiteKey:" + suiteKey + ",corpId:" + corpId)
                ));
                return;
            }
            List<String> staffList = staffListSr.getResult();

            Long appId = Long.valueOf(this.appId);
            ServiceResult<AppVO> appSr = appManageService.getAppByAppId(appId);
            AppVO appVO = appSr.getResult();
            //activeMessage为空不开通
            if(appVO.getActiveMessage() == null || "".equals(appVO.getActiveMessage())){
                return;
            }

            Boolean toAllUser = false;
            //TODO 这里要做文字替换，将$CORPID$和$APPID$替换成真实的corpId和appId
            String msgString = appVO.getActiveMessage().replace("$CORPID$", corpId).replace("$APPID$", this.appId);

            JSONObject json = JSONObject.parseObject(msgString);
            String msgType = json.getString("msgtype");

            MessageBody msg = MessageUtil.parseMessage(json);
            ServiceResult sendSr = sendMessageService.sendCorpMessageAsync(suiteKey, corpId, appId, msgType, toAllUser, staffList, null, msg);

            if(!sendSr.isSuccess()){
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        LogFormatter.KeyValue.getNew("send rsq sync mq message failed", "suiteKey:" + suiteKey + ",corpId:" + corpId)
                ));
                return;
            }

        } catch (JMSException e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("message", message)
            ),e);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("message", message)
            ),e);
        }
    }
}
