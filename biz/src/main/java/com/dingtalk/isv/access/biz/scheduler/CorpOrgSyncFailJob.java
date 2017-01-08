package com.dingtalk.isv.access.biz.scheduler;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.enums.suite.CorpFailType;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpOrgSyncFailDao;
import com.dingtalk.isv.access.biz.corp.model.CorpOrgSyncFailDO;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.service.RsqAccountService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.List;

/**
 * 企业获取钉钉组织结构的异常重试任务
 * Created by Wallace on 2017/1/3.
 */
public class CorpOrgSyncFailJob extends QuartzJobBean {
    private static final Logger    mainLogger = LoggerFactory.getLogger(SuiteTokenGenerateJob.class);
    private static final Logger    bizLogger = LoggerFactory.getLogger("TASK_LOGGER");
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                    LogFormatter.KeyValue.getNew("nextFireTime", jobExecutionContext.getNextFireTime())
            ));
            try{
                XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContextKey");
                CorpOrgSyncFailDao corpOrgSyncFailDao = (CorpOrgSyncFailDao) xmlWebApplicationContext.getBean("corpOrgSyncFailDao");
                RsqAccountService rsqAccountService = (RsqAccountService) xmlWebApplicationContext.getBean("rsqAccountService");

                List<CorpOrgSyncFailDO> list = corpOrgSyncFailDao.getCorpOrgSyncFailList(0,200);
                for(CorpOrgSyncFailDO corpOrgSyncFailDO :list){
                    bizLogger.info(LogFormatter.getKVLogData(null,
                            LogFormatter.KeyValue.getNew("failObject", JSON.toJSONString(corpOrgSyncFailDO)),
                            LogFormatter.KeyValue.getNew("nextFireTime", jobExecutionContext.getNextFireTime())
                    ));
                    String suiteKey = corpOrgSyncFailDO.getSuiteKey();
                    String corpId = corpOrgSyncFailDO.getCorpId();

                    //如果是在激活为应用的阶段失败了.进行如下重试策略
                    if(CorpFailType.PUT_ISV_CORP.equals(corpOrgSyncFailDO.getCorpFailType())){
                        ServiceResult<Void> syncSr = rsqAccountService.syncAllCorp(suiteKey, corpId);

                        if(syncSr.isSuccess()){
                            corpOrgSyncFailDao.deleteById(corpOrgSyncFailDO.getId());
                        }
                    }
                }
            }catch (Exception e){
                bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        LogFormatter.KeyValue.getNew("nextFireTime", jobExecutionContext.getNextFireTime())
                ),e);
                mainLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        LogFormatter.KeyValue.getNew("nextFireTime", jobExecutionContext.getNextFireTime())
                ),e);
            }
    }
}
