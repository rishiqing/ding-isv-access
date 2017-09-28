package com.dingtalk.isv.access.biz.scheduler;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.enums.suite.CorpFailType;
import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.message.SendMessageService;
import com.dingtalk.isv.access.api.service.suite.AppManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpOrgSyncFailDao;
import com.dingtalk.isv.access.biz.corp.model.CorpOrgSyncFailDO;
import com.dingtalk.isv.access.biz.util.MessageUtil;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.service.RsqAccountService;
import com.dingtalk.open.client.api.model.corp.MessageBody;
import com.taobao.api.ApiException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 发送企业通知消息的job
 * Created by Wallace on 2017/1/3.
 */
public class SendCorpMessageJob implements Job {
    private static final Logger    mainLogger = LoggerFactory.getLogger(SendCorpMessageJob.class);
    private static final Logger    bizLogger = LoggerFactory.getLogger("TASK_LOGGER");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("=============================");
        JobDetail detail  = jobExecutionContext.getJobDetail();
        JobKey key = detail.getKey();
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("execute job key:", key)
        ));
        try{
            XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContextKey");
            AppManageService appManageService = (AppManageService) xmlWebApplicationContext.getBean("appManageService");
            SendMessageService sendMessageService = (SendMessageService) xmlWebApplicationContext.getBean("sendMessageService");

            System.out.println("-----------SendCorpMessageJob called--------" + new Date() + "----" + key);
            JobDataMap dataMap = detail.getJobDataMap();
            String corpId = dataMap.getString("corpId");
            Long appId = dataMap.getLong("appId");
            List<String> userIdList = Arrays.asList(dataMap.getString("userIdList").split(","));
            String msgContent = dataMap.getString("msgContent");
            String msgType = dataMap.getString("msgType");
            //  根据appId查询到suiteKey
            ServiceResult<AppVO> appVOSr = appManageService.getAppByAppId(appId);
            String suiteKey = appVOSr.getResult().getSuiteKey();

            MessageBody message = MessageUtil.parseOAMessage(JSON.parseObject(msgContent));
            ServiceResult sr = sendMessageService.sendCorpMessageAsync(suiteKey, corpId, appId, msgType, false, userIdList, null, message);
            if(!sr.isSuccess()){
                throw new ApiException("err send async corp message: " + sr.getMessage());
            }
        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("execute job key:", key)
            ),e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("execute job key:", key)
            ),e);
        }
    }
}
