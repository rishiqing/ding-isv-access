package com.dingtalk.isv.access.biz.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.CorpMessageCorpconversationAsyncsendRequest;
import com.dingtalk.api.response.CorpMessageCorpconversationAsyncsendResponse;
import com.dingtalk.isv.access.api.constant.CommonUtils;
import com.dingtalk.isv.access.api.model.corp.CorpAppVO;
import com.dingtalk.isv.access.api.model.corp.CorpTokenVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.access.api.service.message.SendMessageService;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.corp.MessageBody;
import com.dingtalk.open.client.api.model.corp.MessageSendResult;
import com.dingtalk.open.client.api.service.corp.MessageService;
import com.google.common.base.Joiner;
import com.taobao.api.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by lifeng.zlf on 2016/3/22.
 */
public class SendMessageServiceImpl implements SendMessageService {
    private static final Logger bizLogger = LoggerFactory.getLogger("CRM_MANAGE_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(SendMessageServiceImpl.class);
    @Autowired
    private CorpManageService corpManageService;
    @Autowired
    private MessageService messageService;
    @Override
    public ServiceResult<Void> sendOAMessageToUser(String suiteKey, String corpId, Long appId, String msgType, List<String> staffIdList, List<Long> deptIdList, MessageBody message){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("appId", appId),
                LogFormatter.KeyValue.getNew("msgType", msgType),
                LogFormatter.KeyValue.getNew("staffIdList", staffIdList),
                LogFormatter.KeyValue.getNew("deptIdList", deptIdList),
                LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
        ));
        try{
            ServiceResult<CorpTokenVO> corpTokenSr = corpManageService.getCorpToken(suiteKey, corpId);
            ServiceResult<CorpAppVO>  appSr = corpManageService.getCorpApp(corpId, appId);
            String corpToken = corpTokenSr.getResult().getCorpToken();
            MessageSendResult sr = messageService.sendToCorpConversation(corpToken, CommonUtils.stringList2String(staffIdList, "|"), CommonUtils.longList2String(deptIdList, "|"), String.valueOf(appSr.getResult().getAgentId()), "oa", message);
            if(!StringUtils.isEmpty(sr.getInvaliduser())){
                return ServiceResult.failure(ServiceResultCode.CUSTOM_FIND_ERROR.getErrCode(),ServiceResultCode.CUSTOM_FIND_ERROR.getErrMsg());
            }
            return ServiceResult.success(null);
        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("appId", appId),
                    LogFormatter.KeyValue.getNew("msgType", msgType),
                    LogFormatter.KeyValue.getNew("staffIdList", staffIdList),
                    LogFormatter.KeyValue.getNew("deptIdList", deptIdList),
                    LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
            ));
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常"+e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("appId", appId),
                    LogFormatter.KeyValue.getNew("msgType", msgType),
                    LogFormatter.KeyValue.getNew("staffIdList", staffIdList),
                    LogFormatter.KeyValue.getNew("deptIdList", deptIdList),
                    LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
            ));
            return ServiceResult.failure(ServiceResultCode.CUSTOM_FIND_ERROR.getErrCode(),ServiceResultCode.CUSTOM_FIND_ERROR.getErrMsg());
        }

    }

    @Override
    public ServiceResult<Void> sendMessageToUser(String suiteKey, String corpId, Long appId, String msgType, List<String> staffIdList, List<Long> deptIdList, MessageBody message) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("appId", appId),
                LogFormatter.KeyValue.getNew("msgType", msgType),
                LogFormatter.KeyValue.getNew("staffIdList", staffIdList),
                LogFormatter.KeyValue.getNew("deptIdList", deptIdList),
                LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
        ));
        try{
            ServiceResult<CorpTokenVO> corpTokenSr = corpManageService.getCorpToken(suiteKey, corpId);
            ServiceResult<CorpAppVO>  appSr = corpManageService.getCorpApp(corpId, appId);
            String corpToken = corpTokenSr.getResult().getCorpToken();
            MessageSendResult sr = messageService.sendToCorpConversation(corpToken, CommonUtils.stringList2String(staffIdList, "|"), CommonUtils.longList2String(deptIdList, "|"), String.valueOf(appSr.getResult().getAgentId()), "text", message);
            if(!StringUtils.isEmpty(sr.getInvaliduser())){
                return ServiceResult.failure(ServiceResultCode.CUSTOM_FIND_ERROR.getErrCode(),ServiceResultCode.CUSTOM_FIND_ERROR.getErrMsg());
            }
            return ServiceResult.success(null);
        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("appId", appId),
                    LogFormatter.KeyValue.getNew("msgType", msgType),
                    LogFormatter.KeyValue.getNew("staffIdList", staffIdList),
                    LogFormatter.KeyValue.getNew("deptIdList", deptIdList),
                    LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
            ));
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常"+e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("appId", appId),
                    LogFormatter.KeyValue.getNew("msgType", msgType),
                    LogFormatter.KeyValue.getNew("staffIdList", staffIdList),
                    LogFormatter.KeyValue.getNew("deptIdList", deptIdList),
                    LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
            ));
            return ServiceResult.failure(ServiceResultCode.CUSTOM_FIND_ERROR.getErrCode(),ServiceResultCode.CUSTOM_FIND_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<Void> sendNormalMessage(String suiteKey, String corpId, String sender, String cid, String msgType, MessageBody message) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("msgType", msgType),
                LogFormatter.KeyValue.getNew("sender", sender),
                LogFormatter.KeyValue.getNew("cid", cid),
                LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
        ));
        try{
            ServiceResult<CorpTokenVO> corpTokenSr = corpManageService.getCorpToken(suiteKey, corpId);
            String corpToken = corpTokenSr.getResult().getCorpToken();
            String result = messageService.sendToNormalConversation(corpToken, sender, cid, msgType, message);
            return ServiceResult.success(null);
        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("msgType", msgType),
                    LogFormatter.KeyValue.getNew("sender", sender),
                    LogFormatter.KeyValue.getNew("cid", cid),
                    LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
            ));
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常"+e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("msgType", msgType),
                    LogFormatter.KeyValue.getNew("sender", sender),
                    LogFormatter.KeyValue.getNew("cid", cid),
                    LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
            ));
            return ServiceResult.failure(ServiceResultCode.CUSTOM_FIND_ERROR.getErrCode(),ServiceResultCode.CUSTOM_FIND_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<Void> sendCorpMessageAsync(String suiteKey, String corpId, Long appId, String msgType, Boolean toAllUser, List<String> staffIdList,  List<Long> deptIdList, MessageBody message) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("appId", appId),
                LogFormatter.KeyValue.getNew("toAllUser", toAllUser),
                LogFormatter.KeyValue.getNew("staffIdList", staffIdList),
                LogFormatter.KeyValue.getNew("deptIdList", deptIdList),
                LogFormatter.KeyValue.getNew("msgType", msgType),
                LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
        ));
        try{
            ServiceResult<CorpTokenVO> corpTokenSr = corpManageService.getCorpToken(suiteKey, corpId);
            String corpToken = corpTokenSr.getResult().getCorpToken();
            //  查找agentId
            ServiceResult<CorpAppVO> corpAppSr = corpManageService.getCorpApp(corpId, appId);
            if(!corpAppSr.isSuccess()){
                return ServiceResult.failure(ServiceResultCode.CUSTOM_NOT_FIND.getErrCode(),ServiceResultCode.CUSTOM_NOT_FIND.getErrMsg());
            }
            CorpAppVO corpAppVO = corpAppSr.getResult();
            //  TODO 使用新的钉钉API发送
            DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
            CorpMessageCorpconversationAsyncsendRequest req = new CorpMessageCorpconversationAsyncsendRequest();
            req.setMsgtype(msgType);
            req.setAgentId(corpAppVO.getAgentId());

            if(staffIdList != null && staffIdList.size() > 0){
                String userIds = Joiner.on(",").join(staffIdList);
                req.setUseridList(userIds);
            }

            if(deptIdList != null && deptIdList.size() > 0){
                String dpetIds = Joiner.on(",").join(deptIdList);
                req.setDeptIdList(dpetIds);
            }

            //  toAllUser默认为false
            Boolean isToAll = false;
            if(toAllUser != null){
                isToAll = toAllUser;
            }
            req.setToAllUser(isToAll);

            req.setMsgcontentString(JSON.toJSONString(message));
            bizLogger.warn("=========message: " + JSON.toJSONString(message));
            CorpMessageCorpconversationAsyncsendResponse rsp = client.execute(req, corpToken);
            JSONObject resultJOSN = JSON.parseObject(rsp.getBody());
            if(resultJOSN.containsKey("error_response")){
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),rsp.getBody());
            }
            return ServiceResult.success(null);
        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("appId", appId),
                    LogFormatter.KeyValue.getNew("toAllUser", toAllUser),
                    LogFormatter.KeyValue.getNew("staffIdList", staffIdList),
                    LogFormatter.KeyValue.getNew("deptIdList", deptIdList),
                    LogFormatter.KeyValue.getNew("msgType", msgType),
                    LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
            ));
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常"+e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("appId", appId),
                    LogFormatter.KeyValue.getNew("toAllUser", toAllUser),
                    LogFormatter.KeyValue.getNew("staffIdList", staffIdList),
                    LogFormatter.KeyValue.getNew("deptIdList", deptIdList),
                    LogFormatter.KeyValue.getNew("msgType", msgType),
                    LogFormatter.KeyValue.getNew("message", JSON.toJSONString(message))
            ));
            return ServiceResult.failure(ServiceResultCode.CUSTOM_FIND_ERROR.getErrCode(),ServiceResultCode.CUSTOM_FIND_ERROR.getErrMsg());
        }
    }
}
