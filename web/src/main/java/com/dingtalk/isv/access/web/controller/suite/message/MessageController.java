package com.dingtalk.isv.access.web.controller.suite.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.message.SendMessageService;
import com.dingtalk.isv.access.api.service.suite.AppManageService;
import com.dingtalk.isv.access.biz.scheduler.SendCorpMessageJob;
import com.dingtalk.isv.access.biz.util.MessageUtil;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.HttpResult;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.corp.MessageBody;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

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
    @Autowired
    private Scheduler scheduler;

    @ResponseBody
    @RequestMapping(value = "/msg/sendtoconversation", method = {RequestMethod.POST})
    public Map<String, Object> sendToConversation(HttpServletRequest request,
                                                  @RequestParam("corpid") String corpId,
                                                  @RequestParam("appid") Long appId,
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
            String msgtype = json.getString("msgtype");
            JSONObject data = json.getJSONObject(msgtype);

            MessageBody message = MessageUtil.parseOAMessage(data);
            ServiceResult sr = sendMessageService.sendNormalMessage(suiteKey, corpId, sender, cid, msgtype, message);
            if(!sr.isSuccess()){
                return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("errcode", 0);

            return httpResult.getSuccess(map);
        }catch(Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统错误",
                    LogFormatter.KeyValue.getNew("json", json),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ),e);
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
    @ResponseBody
    @RequestMapping(value = "/msg/sendasynccorpmessage", method = {RequestMethod.POST})
    public Map<String, Object> sendAsyncCorpMessage(HttpServletRequest request,
                                                    @RequestParam("corpid") String corpId,
                                                    @RequestParam("appid") Long appId,
                                                    @RequestBody JSONObject json
    ) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpid", corpId),
                LogFormatter.KeyValue.getNew("appid", appId),
                LogFormatter.KeyValue.getNew("json", json)
        ));
        try{
            String msgType = json.getString("msgtype");
            //Long appId = json.getLong("agent_id");

            List<String> userIdList = null;
            if(json.containsKey("userid_list")){
                String[] userArray  = json.getString("userid_list").split(",");
                userIdList = Arrays.asList(userArray);
            }
            List<Long> deptIdList = new ArrayList<Long>();
            if(json.containsKey("dept_id_list")){
                String[] strDeptIdArray = json.getString("dept_id_list").split(",");
                for (int i = 0; i < strDeptIdArray.length; i++){
                    deptIdList.add(Long.valueOf(strDeptIdArray[i]));
                }
            }
            //  安全起见，toAllUser接口不开放
            Boolean toAllUser = false;
            JSONObject msgcontent = json.getJSONObject("msgcontent");

            //  根据appId查询到suiteKey
            ServiceResult<AppVO> appVOSr = appManageService.getAppByAppId(appId);
            String suiteKey = appVOSr.getResult().getSuiteKey();

            MessageBody message = MessageUtil.parseOAMessage(msgcontent);
            ServiceResult sr = sendMessageService.sendCorpMessageAsync(suiteKey, corpId, appId, msgType, toAllUser, userIdList, deptIdList, message);
            if(!sr.isSuccess()){
                return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("errcode", 0);

            return httpResult.getSuccess(map);
        }catch(Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统错误",
                    LogFormatter.KeyValue.getNew("json", json),
                    LogFormatter.KeyValue.getNew("appid", appId),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ),e);
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
    @ResponseBody
    @RequestMapping(value = "/msg/remind", method = {RequestMethod.POST})
    public Map<String, Object> setRemind(HttpServletRequest request,
                                                    @RequestParam("corpid") String corpId,
                                                    @RequestParam("appid") Long appId,
                                                    @RequestBody JSONObject json
    ) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpid", corpId),
                LogFormatter.KeyValue.getNew("appid", appId),
                LogFormatter.KeyValue.getNew("json", json)
        ));
        try{
            String todoId = json.getString("todo_id");
            //  定时的时间列表
            JSONArray millsArray = json.getJSONArray("mills_array");
            //  定时的规则列表
            JSONArray remindArray = json.getJSONArray("remind_array");
            String userList = json.getString("userid_list");
            String msgType = json.getString("msgtype");
            JSONObject jsonContent = json.getJSONObject("msgcontent");

            //  暂定一次不超过5个
            if (millsArray.size() > 5) {
                return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
            }
            Iterator it = millsArray.iterator();
            Iterator itRule = remindArray.iterator();
            while (it.hasNext()) {
                JobKey jobKey = new JobKey("J-" + corpId + "-" + todoId, "J-rsq-remind");
                JobDetail currentDetail = scheduler.getJobDetail(jobKey);
                if(currentDetail != null){
                    scheduler.deleteJob(jobKey);
                }
                Long mills = (Long)it.next();
                String remind = (String)itRule.next();
                String msgContent = MessageUtil.remindText(jsonContent, remind);

                JobDetail job = JobBuilder.newJob(SendCorpMessageJob.class)
                        .withIdentity(jobKey)
                        .usingJobData("corpId", corpId)
                        .usingJobData("appId", appId)
                        .usingJobData("userIdList", userList)
                        .usingJobData("msgType", msgType)
                        .usingJobData("msgContent", msgContent)
                        .build();

                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity("T-" + corpId + "-" + todoId, "T-rsq-remind")
                        .startAt(new Date(mills))
                        .forJob(job)
                        .build();
                scheduler.scheduleJob(job, trigger);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("errcode", 0);

            return httpResult.getSuccess(map);
        }catch(Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统错误",
                    LogFormatter.KeyValue.getNew("json", json),
                    LogFormatter.KeyValue.getNew("appid", appId),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ),e);
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
}
