package com.dingtalk.isv.access.web.controller.suite.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.corp.CorpTokenVO;
import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.message.SendMessageService;
import com.dingtalk.isv.access.api.service.suite.AppManageService;
import com.dingtalk.isv.access.biz.corp.model.StaffIdsDO;
import com.dingtalk.isv.access.web.controller.IdMapController;
import com.dingtalk.isv.access.web.util.MessageUtil;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.HttpResult;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.corp.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 发送消息的Controller
 * User: user 毛文强
 * Date: 2017/9/27
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class MessageController {
    private static final Logger mainLogger = LoggerFactory.getLogger(MessageController.class);
    private static final Logger bizLogger = LoggerFactory.getLogger("CONTROLLER_ISV_MESSAGE_LOGGER");

    @Resource
    private HttpResult httpResult;

    @Resource
    private AppManageService appManageService;
    @Resource
    private SendMessageService sendMessageService;

    @ResponseBody
    @RequestMapping(value = "/msg/sendtoconversation/oa", method = {RequestMethod.POST})
    public Map<String, Object> sendToConversation(HttpServletRequest request,
                                                  @RequestParam("corpid") String corpId,
                                                  @RequestParam("appid") String appId,
                                                  @RequestBody JSONObject json
    ) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("json", json),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("appId", appId)
        ));
        try{
            //  根据appId查询到suiteKey
            ServiceResult<AppVO> appVOSr = appManageService.getAppByAppId(Long.valueOf(appId));
            String suiteKey = appVOSr.getResult().getSuiteKey();

            String sender = json.getString("sender");
            String cid = json.getString("cid");
            String oaType = "oa";
            JSONObject data = json.getJSONObject("data");

            MessageBody message = MessageUtil.parseOAMessage(data);
            ServiceResult sr = sendMessageService.sendNormalMessage(suiteKey, corpId, sender, cid, oaType, message);
            if(!sr.isSuccess()){
                return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("errcode", 0);

            return httpResult.getSuccess(map);
        }catch(Exception e){
            bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统错误",
                    LogFormatter.KeyValue.getNew("json", json),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ),e);
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
}
