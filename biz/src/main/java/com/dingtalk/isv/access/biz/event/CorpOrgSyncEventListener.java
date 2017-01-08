package com.dingtalk.isv.access.biz.event;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.enums.suite.CorpFailType;
import com.dingtalk.isv.access.api.model.event.CorpOrgSyncEvent;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpOrgSyncFailDao;
import com.dingtalk.isv.access.biz.corp.model.CorpOrgSyncFailDO;
import com.dingtalk.isv.common.event.EventListener;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.service.RsqAccountService;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

/**
 * 企业同步组织结构信息异步调用的listener
 * Created by Wallace on 2017/1/3.
 */
public class CorpOrgSyncEventListener implements EventListener {
    private static final Logger bizLogger = LoggerFactory.getLogger("EVENT_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(CorpOrgSyncEventListener.class);
    @Autowired
    private DeptManageService deptManageService;
    @Autowired
    private StaffManageService staffManageService;
    @Autowired
    private RsqAccountService rsqAccountService;
    @Autowired
    private CorpOrgSyncFailDao corpOrgSyncFailDao;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    @Qualifier("corpAuthSuiteQueue")
    private Queue corpAuthSuiteQueue;

    /**
     * 企业同步组织结构信息授权码异步逻辑
     * @param corpOrgSyncEvent
     */
    @Subscribe
    public void listenCorpOrgSyncEvent(CorpOrgSyncEvent corpOrgSyncEvent) {
        try{
            bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                    LogFormatter.KeyValue.getNew("corpOrgSyncEvent", JSON.toJSONString(corpOrgSyncEvent))
            ));
            String suiteKey = corpOrgSyncEvent.getSuiteKey();
            String corpId = corpOrgSyncEvent.getCorpId();

            ServiceResult<Void> syncSr = rsqAccountService.syncAllCorp(suiteKey, corpId);
            if(!syncSr.isSuccess()){
                //加入失败job,失败任务会重试
                CorpOrgSyncFailDO corpOrgSyncFailDO = new CorpOrgSyncFailDO();
                corpOrgSyncFailDO.setSuiteKey(suiteKey);
                corpOrgSyncFailDO.setCorpId(corpId);
                corpOrgSyncFailDO.setCorpFailType(CorpFailType.PUT_ISV_CORP);
                corpOrgSyncFailDO.setFailInfo(syncSr.getMessage());
                corpOrgSyncFailDao.addOrUpdateCorpOrgSyncFailDO(corpOrgSyncFailDO);
                return;
            }


            //  发到corpAuthSuiteQueue队列中，由第三方异步处理
//            jmsTemplate.send(corpAuthSuiteQueue,new CorpAuthSuiteMessage(corpId,suiteKey, CorpAuthSuiteMessage.Tag.Auth));

        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("corpOrgSyncEvent", JSON.toJSONString(corpOrgSyncEvent))
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("corpOrgSyncEvent", JSON.toJSONString(corpOrgSyncEvent))
            ), e);
        }

    }

}
