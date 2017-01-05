package com.dingtalk.isv.access.biz.corp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.model.event.CorpCallbackEvent;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpCallbackFailDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpCallbackQueueDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpLockDao;
import com.dingtalk.isv.access.biz.corp.enumtype.CorpLockType;
import com.dingtalk.isv.access.biz.corp.model.CorpCallbackFailDO;
import com.dingtalk.isv.access.biz.corp.model.CorpCallbackQueueDO;
import com.dingtalk.isv.access.biz.corp.model.CorpLockDO;
import com.dingtalk.isv.access.biz.corp.service.CorpCallbackQueueService;
import com.dingtalk.isv.access.biz.corp.service.CorpLockService;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * 公司通讯录回调相关的service
 * Created by Wallace on 2017/1/5.
 */
public class CorpCallbackQueueServiceImpl implements CorpCallbackQueueService {
    private static final Logger bizLogger = LoggerFactory.getLogger("CORP_CALLBACK_QUEUE_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(CorpCallbackQueueServiceImpl.class);

    @Autowired
    private CorpCallbackQueueDao corpCallbackQueueDao;
    @Autowired
    private CorpCallbackFailDao corpCallbackFailDao;
    @Autowired
    private StaffManageService staffManageService;
    @Autowired
    private DeptManageService deptManageService;

    @Override
    public ServiceResult<Void> saveCorpCallbackQueue(JSONObject jsonObject, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("jsonObject", jsonObject),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            String corpId = jsonObject.getString("CorpId");
            String tag = jsonObject.getString("EventType");
            CorpCallbackQueueDO corpCallbackQueueDO = new CorpCallbackQueueDO();
            corpCallbackQueueDO.setSuiteKey(suiteKey);
            corpCallbackQueueDO.setCorpId(corpId);
            corpCallbackQueueDO.setEventJSON(jsonObject.toJSONString());
            corpCallbackQueueDO.setTag(tag);
            corpCallbackQueueDao.addOrUpdateCorpCallbackQueueDO(corpCallbackQueueDO);

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("jsonObject", jsonObject),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("jsonObject", jsonObject),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
    @Override
    public ServiceResult<Void> saveCorpCallbackFail(JSONObject jsonObject, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("jsonObject", jsonObject),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            String corpId = jsonObject.getString("CorpId");
            String tag = jsonObject.getString("EventType");
            CorpCallbackFailDO corpCallbackFailDO = new CorpCallbackFailDO();
            corpCallbackFailDO.setSuiteKey(suiteKey);
            corpCallbackFailDO.setCorpId(corpId);
            corpCallbackFailDO.setEventJSON(jsonObject.toJSONString());
            corpCallbackFailDO.setTag(tag);
            corpCallbackFailDao.addOrUpdateCorpCallbackFailDO(corpCallbackFailDO);

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("jsonObject", jsonObject),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("jsonObject", jsonObject),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<Void> addUser(String[] userIdArray, String corpId, String suiteKey) {

        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            for(int i = 0; i < userIdArray.length; i ++){
                //TODO 先同步日事清

                ServiceResult<Void> fetchSr = staffManageService.getAndSaveStaff(userIdArray[i], corpId, suiteKey);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
                }
            }

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<Void> changeUser(String[] userIdArray, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            for(int i = 0; i < userIdArray.length; i ++){
                //TODO 先同步日事清

                ServiceResult<Void> fetchSr = staffManageService.getAndSaveStaff(userIdArray[i], corpId, suiteKey);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
                }

            }

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<Void> deleteUser(String[] userIdArray, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            for(int i = 0; i < userIdArray.length; i ++){
                //TODO 先同步日事清

                ServiceResult<Void> delSr = staffManageService.deleteStaffByCorpIdAndUserId(corpId, userIdArray[i]);
                if(!delSr.isSuccess()){
                    return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
                }
            }

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<Void> toggleUserAdmin(String[] userIdArray, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            for(int i = 0; i < userIdArray.length; i ++){
                //TODO 先同步日事清

                ServiceResult<Void> fetchSr = staffManageService.getAndSaveStaff(userIdArray[i], corpId, suiteKey);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
                }

            }

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("userIdArray", userIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<Void> addDepartment(Long[] deptIdArray, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("deptIdArray", deptIdArray),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            for(int i = 0; i < deptIdArray.length; i ++){
                //TODO 先同步日事清

                ServiceResult<Void> fetchSr = deptManageService.getAndSaveDepartment(deptIdArray[i], corpId, suiteKey);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
                }

            }

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("deptIdArray", deptIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("deptIdArray", deptIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<Void> changeDepartment(Long[] deptIdArray, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("deptIdArray", deptIdArray),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            for(int i = 0; i < deptIdArray.length; i ++){
                //TODO 先同步日事清

                ServiceResult<Void> fetchSr = deptManageService.getAndSaveDepartment(deptIdArray[i], corpId, suiteKey);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
                }

            }

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("deptIdArray", deptIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("deptIdArray", deptIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<Void> deleteDepartment(Long[] deptIdArray, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("deptIdArray", deptIdArray),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            for(int i = 0; i < deptIdArray.length; i ++){
                //TODO 先同步日事清

                ServiceResult<Void> fetchSr = deptManageService.deleteStaffByCorpIdAndUserId(corpId, deptIdArray[i]);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
                }

            }

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("deptIdArray", deptIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("deptIdArray", deptIdArray),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
}
