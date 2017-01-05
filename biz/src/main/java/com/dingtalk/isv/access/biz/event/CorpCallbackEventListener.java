package com.dingtalk.isv.access.biz.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.model.event.CorpCallbackEvent;
import com.dingtalk.isv.access.api.model.event.mq.SuiteCallBackMessage;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpCallbackQueueDao;
import com.dingtalk.isv.access.biz.corp.model.CorpCallbackQueueDO;
import com.dingtalk.isv.common.event.EventListener;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

/**
 * 接受通讯录变更事件异步调用的listener
 * Created by Wallace on 2017/1/4.
 */
public class CorpCallbackEventListener implements EventListener {
    private static final Logger bizLogger = LoggerFactory.getLogger("EVENT_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(CorpCallbackEventListener.class);
    @Autowired
    private DeptManageService deptManageService;
    @Autowired
    private StaffManageService staffManageService;
    @Autowired
    private CorpCallbackQueueDao corpCallbackQueueDao;

    /**
     * 企业同步组织结构信息授权码异步逻辑
     * @param corpCallbackEvent
     */
    @Subscribe
    public void listenCorpCallbackEvent(CorpCallbackEvent corpCallbackEvent) {
        try{
            bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                    LogFormatter.KeyValue.getNew("corpCallbackEvent", JSON.toJSONString(corpCallbackEvent))
            ));

            String suiteKey = corpCallbackEvent.getSuiteKey();
            SuiteCallBackMessage.Tag tag = corpCallbackEvent.getTag();
            JSONObject eventJSON = corpCallbackEvent.getEventJSON();

            switch(tag){
                case USER_ADD_ORG:
                case USER_MODIFY_ORG:
                case USER_LEAVE_ORG:
                case ORG_ADMIN_ADD:
                case ORG_ADMIN_REMOVE:
                    String corpId = eventJSON.getString("CorpId");
                    JSONArray userIdarray = eventJSON.getJSONArray("UserId");
                    Iterator it = userIdarray.iterator();
                    while(it.hasNext()){
                        String userId = (String)it.next();
                        ServiceResult<StaffVO> staffSr = staffManageService.getStaff(userId, corpId, suiteKey);
                        if(!staffSr.isSuccess()){
                            saveFailInfo(corpCallbackEvent);
                            return;
                        }
                        ServiceResult<Void> staffSaveSr = staffManageService.saveOrUpdateCorpStaff(staffSr.getResult());
                        if(!staffSaveSr.isSuccess()){
                            saveFailInfo(corpCallbackEvent);
                            return;
                        }
                    }
                    break;
                case ORG_DEPT_CREATE:
                case ORG_DEPT_MODIFY:
                case ORG_DEPT_REMOVE:
                    String corpId2 = eventJSON.getString("CorpId");
                    JSONArray deptIdArray = eventJSON.getJSONArray("DeptId");
                    Iterator it2 = deptIdArray.iterator();
                    while(it2.hasNext()){
                        Long deptId = (Long)it2.next();
                        ServiceResult<DepartmentVO> deptSr = deptManageService.getDept(deptId, corpId2, suiteKey);
                        if(!deptSr.isSuccess()){
                            saveFailInfo(corpCallbackEvent);
                            return;
                        }
                        ServiceResult<Void> deptSaveSr = deptManageService.saveOrUpdateCorpDepartment(deptSr.getResult());
                        if(!deptSaveSr.isSuccess()){
                            saveFailInfo(corpCallbackEvent);
                            return;
                        }
                    }
                    break;
                case ORG_REMOVE:
                    bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                            LogFormatter.KeyValue.getNew("corpCallbackEvent", "ORG_REMOVE:-----" + JSON.toJSONString(corpCallbackEvent))
                    ));
                    break;
            }

        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("corpCallbackEvent", JSON.toJSONString(corpCallbackEvent))
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("corpCallbackEvent", JSON.toJSONString(corpCallbackEvent))
            ), e);
        }

    }

    public void saveFailInfo(CorpCallbackEvent event){
        JSONObject eventJSON = event.getEventJSON();
        String corpId = eventJSON.getString("CorpId");

        CorpCallbackQueueDO corpCallbackQueueDO = new CorpCallbackQueueDO();
        corpCallbackQueueDO.setSuiteKey(event.getSuiteKey());
        corpCallbackQueueDO.setCorpId(corpId);
        corpCallbackQueueDO.setEventJSON(JSON.toJSONString(eventJSON));
        corpCallbackQueueDao.addOrUpdateCorpCallbackQueueDO(corpCallbackQueueDO);
    }

}
