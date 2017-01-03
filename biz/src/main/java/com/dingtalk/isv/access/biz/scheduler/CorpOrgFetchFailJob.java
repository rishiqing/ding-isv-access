package com.dingtalk.isv.access.biz.scheduler;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.enums.suite.AuthFaileType;
import com.dingtalk.isv.access.api.enums.suite.CorpFailType;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.api.service.suite.CorpSuiteAuthService;
import com.dingtalk.isv.access.biz.corp.dao.CorpOrgFetchFailDao;
import com.dingtalk.isv.access.biz.corp.model.CorpOrgFetchFailDO;
import com.dingtalk.isv.access.biz.suite.dao.CorpSuiteAuthFaileDao;
import com.dingtalk.isv.access.biz.suite.model.CorpSuiteAuthFaileDO;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
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
public class CorpOrgFetchFailJob extends QuartzJobBean {
    private static final Logger    mainLogger = LoggerFactory.getLogger(SuiteTokenGenerateJob.class);
    private static final Logger    bizLogger = LoggerFactory.getLogger("TASK_LOGGER");
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                    LogFormatter.KeyValue.getNew("nextFireTime", jobExecutionContext.getNextFireTime())
            ));
            try{
                XmlWebApplicationContext xmlWebApplicationContext = (XmlWebApplicationContext) jobExecutionContext.getScheduler().getContext().get("applicationContextKey");
                CorpOrgFetchFailDao corpOrgFetchFailDao = (CorpOrgFetchFailDao) xmlWebApplicationContext.getBean("corpOrgFetchFailDao");
                DeptManageService deptManageService = (DeptManageService) xmlWebApplicationContext.getBean("deptManageService");
                StaffManageService staffManageService = (StaffManageService) xmlWebApplicationContext.getBean("staffManageService");

                List<CorpOrgFetchFailDO> list = corpOrgFetchFailDao.getCorpOrgFetchFailList(0,200);
                for(CorpOrgFetchFailDO corpOrgFetchFailDO:list){
                    bizLogger.info(LogFormatter.getKVLogData(null,
                            LogFormatter.KeyValue.getNew("failObject", JSON.toJSONString(corpOrgFetchFailDO)),
                            LogFormatter.KeyValue.getNew("nextFireTime", jobExecutionContext.getNextFireTime())
                    ));
                    String suiteKey = corpOrgFetchFailDO.getSuiteKey();
                    String corpId = corpOrgFetchFailDO.getCorpId();
//                    corpOrgFetchFailDao.deleteById(corpOrgFetchFailDO.getId());

                    //如果是在激活为应用的阶段失败了.进行如下重试策略
                    if(CorpFailType.GET_CHILDREN_DEPTS.equals(corpOrgFetchFailDO.getCorpFailType())){
                        ServiceResult<Void> sr = deptManageService.getAndSaveAllCorpOrg(suiteKey, corpId);

                        if(sr.isSuccess()){
                            corpOrgFetchFailDao.deleteById(corpOrgFetchFailDO.getId());

                            //同步人员
                            ServiceResult<Void> staffSr = staffManageService.getAndSaveAllCorpOrgStaff(suiteKey, corpId);

                            if(!staffSr.isSuccess()){
                                //加入失败job,失败任务会重试
                                CorpOrgFetchFailDO failDO = new CorpOrgFetchFailDO();
                                failDO.setSuiteKey(suiteKey);
                                failDO.setCorpId(corpId);
                                failDO.setCorpFailType(CorpFailType.GET_DEPT_STAFF);
                                failDO.setFailInfo(sr.getMessage());
                                corpOrgFetchFailDao.addOrUpdateCorpOrgFetchFailDO(failDO);
                            }
                        }


                    }else if(CorpFailType.GET_DEPT_STAFF.equals(corpOrgFetchFailDO.getCorpFailType())){
                        ServiceResult<Void> staffSr = staffManageService.getAndSaveAllCorpOrgStaff(suiteKey, corpId);
                        if(staffSr.isSuccess()){
                            corpOrgFetchFailDao.deleteById(corpOrgFetchFailDO.getId());
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
