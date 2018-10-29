package com.dingtalk.isv.access.bizex.dingpush.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.event.CorpAuthSuiteEvent;
import com.dingtalk.isv.access.api.model.suite.CorpSuiteAuthVO;
import com.dingtalk.isv.access.api.model.suite.SuiteTicketVO;
import com.dingtalk.isv.access.api.model.suite.SuiteTokenVO;
import com.dingtalk.isv.access.api.model.suite.SuiteVO;
import com.dingtalk.isv.access.api.service.suite.CorpSuiteAuthService;
import com.dingtalk.isv.access.api.service.suite.SuiteManageService;
import com.dingtalk.isv.access.biz.corp.service.impl.CorpLockServiceImpl;
import com.dingtalk.isv.access.bizex.dingpush.dao.OpenSyncBizDataDao;
import com.dingtalk.isv.access.bizex.dingpush.model.OpenGlobalLock;
import com.dingtalk.isv.access.bizex.dingpush.model.OpenSyncBizData;
import com.dingtalk.isv.access.bizex.dingpush.service.DbAuthCheckService;
import com.dingtalk.isv.access.bizex.dingpush.service.OpenGlobalLockService;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.google.common.eventbus.AsyncEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Wallace Mao
 * Date: 2018-10-29 10:23
 */
public class DbAuthCheckServiceImpl implements DbAuthCheckService {
    private static final String DB_CHECK_LOCK_KEY = "auth_check";
    @Autowired
    private OpenGlobalLockService openGlobalLockService;
    @Autowired
    private OpenSyncBizDataDao openSyncBizDataDao;
    @Autowired
    private SuiteManageService suiteManageService;
    @Autowired
    private CorpSuiteAuthService corpSuiteAuthService;
    @Autowired
    private AsyncEventBus asyncCorpAuthSuiteEventBus;

    /**
     * 逻辑上这样的：
     * 1  调用openGlobalLockService获取锁，如果锁获取成功，那么逐条执行
     */
    @Override
    public void checkDingEvent(){
        OpenGlobalLock lock = openGlobalLockService.requireOpenGlobalLock(DB_CHECK_LOCK_KEY);
        System.out.println(">>>>>>>>>>>>>>>>>" + lock);
        if(lock == null){
            return;
        }
        try{
            List<OpenSyncBizData> syncList = openSyncBizDataDao.getOpenSyncBizDataListByStatus(0L);
            for(OpenSyncBizData data : syncList){
                handleSyncData(data);
                data.setStatus(1L);
                openSyncBizDataDao.updateStatus(data);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            openGlobalLockService.releaseOpenGlobalLock(DB_CHECK_LOCK_KEY);
        }

    }

    @Override
    public void handleSyncData(OpenSyncBizData data){
        JSONObject json = JSONObject.parseObject(data.getBizData());
        String syncAction = json.getString("syncAction");
        if("suite_ticket".equals(syncAction)){
            String receiveSuiteTicket = json.getString("suiteTicket");
            String suiteId = data.getBizId();
            ServiceResult<SuiteVO> sr = suiteManageService.getSuiteBySuiteId(suiteId);
            SuiteVO suiteVO = sr.getResult();
            SuiteTicketVO suiteTicketVO = new SuiteTicketVO();
            suiteTicketVO.setSuiteTicket(receiveSuiteTicket);
            suiteTicketVO.setSuiteKey(suiteVO.getSuiteKey());
            suiteManageService.saveOrUpdateSuiteTicket(suiteTicketVO);
        }else if("org_suite_auth".equals(syncAction)){
            String suiteId = data.getBizId();
            ServiceResult<SuiteVO> sr = suiteManageService.getSuiteBySuiteId(suiteId);
            String suiteKey = sr.getResult().getSuiteKey();
            ServiceResult<SuiteTokenVO> suiteTokenSr = suiteManageService.getSuiteToken(suiteKey);
            String suiteToken = suiteTokenSr.getResult().getSuiteToken();

            String corpId = json.getJSONObject("auth_corp_info").getString("corpid");
            CorpSuiteAuthVO corpSuiteAuthVO = new CorpSuiteAuthVO();
            corpSuiteAuthVO.setSuiteKey(suiteKey);
            corpSuiteAuthVO.setCorpId(corpId);
            corpSuiteAuthVO.setPermanentCode(json.getString("permanent_code"));
            corpSuiteAuthService.saveOrUpdateCorpSuiteAuth(corpSuiteAuthVO);

            CorpAuthSuiteEvent corpAuthSuiteEvent = new CorpAuthSuiteEvent();
            corpAuthSuiteEvent.setSuiteKey(suiteKey);
            corpAuthSuiteEvent.setSuiteToken(suiteToken);
            corpAuthSuiteEvent.setCorpId(corpSuiteAuthVO.getCorpId());
            corpAuthSuiteEvent.setPermanentCode(corpSuiteAuthVO.getPermanentCode());
            corpAuthSuiteEvent.setChPermanentCode(corpSuiteAuthVO.getChPermanentCode());
            asyncCorpAuthSuiteEventBus.post(corpAuthSuiteEvent);
        }else{
            throw new RuntimeException("===unhandled syncAction: " + syncAction);
        }
    }
}
