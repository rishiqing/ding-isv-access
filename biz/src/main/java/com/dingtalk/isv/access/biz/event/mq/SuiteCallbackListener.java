package com.dingtalk.isv.access.biz.event.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.event.mq.SuiteCallBackMessage;
import com.dingtalk.isv.access.biz.corp.service.CorpCallbackQueueService;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.*;

/**
 * 通讯录回调事件的mq listener
 * 基本原理：根据tag的不同调用corpCallbackQueueService中的不同接口
 * Created by Wallace on 2017/1/5.
 */
public class SuiteCallbackListener implements MessageListener {
    private static final Logger bizLogger = LoggerFactory.getLogger("MQ_LISTENER_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(SuiteCallbackListener.class);
    @Autowired
    private CorpCallbackQueueService corpCallbackQueueService;

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
            System.out.println("接收到一个object message消息。");
            String strJsonObject = objMsg.getString("jsonObject");
            String strTag = objMsg.getString("tag");

            System.out.println("消息内容是：" + strJsonObject);
            System.out.println("tag消息内容是：" + strTag);

            SuiteCallBackMessage.Tag tag = SuiteCallBackMessage.Tag.valueOf(strTag);
            JSONObject eventJSON = JSON.parseObject(strJsonObject);

            String suiteKey = eventJSON.containsKey("suiteKey") ? eventJSON.getString("suiteKey") : null;
            String corpId = eventJSON.containsKey("CorpId") ? eventJSON.getString("CorpId") : null;
            String[] userIdArray = new String[]{};
            Long[] deptIdArray = new Long[]{};
            if(eventJSON.containsKey("UserId")){
                userIdArray = eventJSON.getJSONArray("UserId").toArray(userIdArray);
            }
            if(eventJSON.containsKey("DeptId")){
                Object[] objArray = eventJSON.getJSONArray("DeptId").toArray();
                deptIdArray = new Long[objArray.length];
                for(int i=0;i<objArray.length;i++){
                    deptIdArray[i] = new Long((Integer)objArray[i]);
                }
            }

            ServiceResult callbackSr = null;

            switch(tag){
                case USER_ADD_ORG:
                    callbackSr = corpCallbackQueueService.addUser(userIdArray, corpId, suiteKey);
                    break;
                case USER_MODIFY_ORG:
                    callbackSr = corpCallbackQueueService.changeUser(userIdArray, corpId, suiteKey);
                    break;
                case USER_LEAVE_ORG:
                    callbackSr = corpCallbackQueueService.deleteUser(userIdArray, corpId, suiteKey);
                    break;
                case ORG_ADMIN_ADD:
                case ORG_ADMIN_REMOVE:
                    callbackSr = corpCallbackQueueService.toggleUserAdmin(userIdArray, corpId, suiteKey);
                    break;
                case ORG_DEPT_CREATE:
                    callbackSr = corpCallbackQueueService.addDepartment(deptIdArray, corpId, suiteKey);
                    break;
                case ORG_DEPT_MODIFY:
                    callbackSr = corpCallbackQueueService.changeDepartment(deptIdArray, corpId, suiteKey);
                    break;
                case ORG_DEPT_REMOVE:
                    callbackSr = corpCallbackQueueService.deleteDepartment(deptIdArray, corpId, suiteKey);
                    break;
                case ORG_REMOVE:
                    bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                            LogFormatter.KeyValue.getNew("suiteCallbackListener", "ORG_REMOVE:-----" + strJsonObject)
                    ));
                    break;
            }
            if(!callbackSr.isSuccess()){
                corpCallbackQueueService.saveCorpCallbackFail(eventJSON, suiteKey);
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        LogFormatter.KeyValue.getNew("suiteCallbackListener", "error:" + strJsonObject)
                ));
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
