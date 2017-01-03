package com.dingtalk.isv.access.biz.event;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.enums.suite.CorpFailType;
import com.dingtalk.isv.access.api.model.event.CorpOrgFetchEvent;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpOrgFetchFailDao;
import com.dingtalk.isv.access.biz.corp.model.CorpOrgFetchFailDO;
import com.dingtalk.isv.common.event.EventListener;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 企业同步组织结构信息异步调用的listener
 * Created by Wallace on 2017/1/3.
 */
public class CorpOrgFetchEventListener implements EventListener {
    private static final Logger bizLogger = LoggerFactory.getLogger("EVENT_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(CorpOrgFetchEventListener.class);
    @Autowired
    private DeptManageService deptManageService;
    @Autowired
    private StaffManageService staffManageService;
    @Autowired
    private CorpOrgFetchFailDao corpOrgFetchFailDao;

    /**
     * 企业同步组织结构信息授权码异步逻辑
     * @param corpOrgFetchEvent
     */
    @Subscribe
    public void listenCorpOrgFetchEvent(CorpOrgFetchEvent corpOrgFetchEvent) {
        try{
            bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                    LogFormatter.KeyValue.getNew("corpOrgFetchEvent", JSON.toJSONString(corpOrgFetchEvent))
            ));
            String suiteKey = corpOrgFetchEvent.getSuiteKey();
            String corpId = corpOrgFetchEvent.getCorpId();
//            System.out.println(Thread.currentThread().getId() + ":-----listenCorpAuthSuiteEvent-----" + new Date());
            //同步部门
            ServiceResult<Void> sr = deptManageService.getAndSaveAllCorpOrg(suiteKey, corpId);

//            System.out.println(Thread.currentThread().getId() + ":-----after ---- listenCorpAuthSuiteEvent-----" + new Date() + "," + activeAppSr.getMessage());
            if(!sr.isSuccess()){
                //加入失败job,失败任务会重试
                CorpOrgFetchFailDO corpOrgFetchFailDO = new CorpOrgFetchFailDO();
                corpOrgFetchFailDO.setSuiteKey(corpOrgFetchEvent.getSuiteKey());
                corpOrgFetchFailDO.setCorpId(corpOrgFetchEvent.getCorpId());
                corpOrgFetchFailDO.setCorpFailType(CorpFailType.GET_CHILDREN_DEPTS);
                corpOrgFetchFailDO.setFailInfo(sr.getMessage());
                corpOrgFetchFailDao.addOrUpdateCorpOrgFetchFailDO(corpOrgFetchFailDO);
                return;
            }

            //同步人员
            ServiceResult<Void> staffSr = staffManageService.getAndSaveAllCorpOrgStaff(suiteKey, corpId);

            if(!staffSr.isSuccess()){
                //加入失败job,失败任务会重试
                CorpOrgFetchFailDO corpOrgFetchFailDO = new CorpOrgFetchFailDO();
                corpOrgFetchFailDO.setSuiteKey(corpOrgFetchEvent.getSuiteKey());
                corpOrgFetchFailDO.setCorpId(corpOrgFetchEvent.getCorpId());
                corpOrgFetchFailDO.setCorpFailType(CorpFailType.GET_DEPT_STAFF);
                corpOrgFetchFailDO.setFailInfo(staffSr.getMessage());
                corpOrgFetchFailDao.addOrUpdateCorpOrgFetchFailDO(corpOrgFetchFailDO);
                return;
            }

        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("corpOrgFetchEvent", JSON.toJSONString(corpOrgFetchEvent))
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("corpOrgFetchEvent", JSON.toJSONString(corpOrgFetchEvent))
            ), e);
        }

    }

}
