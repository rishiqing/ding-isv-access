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
import com.dingtalk.isv.rsq.biz.model.RsqUser;
import com.dingtalk.isv.rsq.biz.service.RsqCorpCallbackService;
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
    @Autowired
    private RsqCorpCallbackService rsqCorpCallbackService;

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
                String userId = userIdArray[i];
                //1 先读取钉钉并保存本地
                ServiceResult<Void> fetchSr = staffManageService.getAndSaveStaff(userId, corpId, suiteKey);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(fetchSr.getCode(),fetchSr.getMessage());
                }
                ServiceResult<StaffVO> staffVOSr = staffManageService.getStaffByCorpIdAndUserId(corpId, userId);
                if(!staffVOSr.isSuccess()){
                    return ServiceResult.failure(staffVOSr.getCode(), staffVOSr.getMessage());
                }
                StaffVO staffVO = staffVOSr.getResult();

                //2 同步日事清
                ServiceResult<StaffVO> rsqUserSr = rsqCorpCallbackService.createRsqTeamStaff(suiteKey, staffVO);
                if(!rsqUserSr.isSuccess()){
                    return ServiceResult.failure(rsqUserSr.getCode(),rsqUserSr.getMessage());
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
                String userId = userIdArray[i];
                //1 先读取钉钉
                ServiceResult<StaffVO> staffVOSr = staffManageService.getStaff(userId, corpId, suiteKey);
                if(!staffVOSr.isSuccess()){
                    return ServiceResult.failure(staffVOSr.getCode(), staffVOSr.getMessage());
                }
                StaffVO staffVO = staffVOSr.getResult();
                //2 同步日事清
                ServiceResult<Void> rsqUserSr = rsqCorpCallbackService.updateRsqTeamStaff(suiteKey, staffVO);
                if(!rsqUserSr.isSuccess()){
                    return ServiceResult.failure(rsqUserSr.getCode(),rsqUserSr.getMessage());
                }
                //3 保存本地
                ServiceResult<Void> fetchSr = staffManageService.saveOrUpdateCorpStaff(staffVO);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(fetchSr.getCode(),fetchSr.getMessage());
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
                String userId = userIdArray[i];
                //1 从本地读取user
                ServiceResult<StaffVO> staffVOSr = staffManageService.getStaffByCorpIdAndUserId(corpId, userId);
                if(!staffVOSr.isSuccess()){
                    return ServiceResult.failure(staffVOSr.getCode(), staffVOSr.getMessage());
                }
                StaffVO staffVO = staffVOSr.getResult();
                // 如果数据库中未找到，则不做处理
                if(null == staffVO){
                    continue;
                }
                //2 同步日事清
                ServiceResult<Void> rsqUserSr = rsqCorpCallbackService.removeUserFromTeam(suiteKey, staffVO);
                if(!rsqUserSr.isSuccess()){
                    return ServiceResult.failure(rsqUserSr.getCode(),rsqUserSr.getMessage());
                }
                //3 本地删除
                ServiceResult<Void> delSr = staffManageService.deleteStaffByCorpIdAndUserId(corpId, userIdArray[i]);
                if(!delSr.isSuccess()){
                    return ServiceResult.failure(delSr.getCode(),delSr.getMessage());
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
                String userId = userIdArray[i];
                //1 先读取钉钉
                ServiceResult<StaffVO> staffVOSr = staffManageService.getStaff(userId, corpId, suiteKey);
                if(!staffVOSr.isSuccess()){
                    return ServiceResult.failure(staffVOSr.getCode(), staffVOSr.getMessage());
                }
                StaffVO staffVO = staffVOSr.getResult();
                //2 同步日事清
                ServiceResult<Void> rsqUserSr = rsqCorpCallbackService.setRsqTeamStaffAdmin(suiteKey, staffVO);
                if(!rsqUserSr.isSuccess()){
                    return ServiceResult.failure(rsqUserSr.getCode(),rsqUserSr.getMessage());
                }
                //3 保存本地
                ServiceResult<Void> fetchSr = staffManageService.saveOrUpdateCorpStaff(staffVO);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(fetchSr.getCode(),fetchSr.getMessage());
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
                Long deptId = deptIdArray[i];
                //1 先读取钉钉并保存本地
                ServiceResult<Void> fetchSr = deptManageService.getAndSaveDepartment(deptId, corpId, suiteKey);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(fetchSr.getCode(),fetchSr.getMessage());
                }
                ServiceResult<DepartmentVO> deptVOSr = deptManageService.getDepartmentByCorpIdAndDeptId(corpId, deptId);
                if(!deptVOSr.isSuccess()){
                    return ServiceResult.failure(deptVOSr.getCode(), deptVOSr.getMessage());
                }
                DepartmentVO deptVO = deptVOSr.getResult();

                //2 同步日事清
                ServiceResult<DepartmentVO> rsqDeptSr = rsqCorpCallbackService.createRsqDepartment(suiteKey, deptVO);
                if(!rsqDeptSr.isSuccess()){
                    return ServiceResult.failure(rsqDeptSr.getCode(),rsqDeptSr.getMessage());
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
                Long deptId = deptIdArray[i];
                //1 先读取钉钉
                ServiceResult<DepartmentVO> deptVOSr = deptManageService.getDept(deptId, corpId, suiteKey);
                if(!deptVOSr.isSuccess()){
                    return ServiceResult.failure(deptVOSr.getCode(), deptVOSr.getMessage());
                }
                DepartmentVO deptVO = deptVOSr.getResult();
                //2 同步日事清
                ServiceResult<DepartmentVO> rsqDeptSr = rsqCorpCallbackService.updateRsqDepartment(suiteKey, deptVO);
                if(!rsqDeptSr.isSuccess()){
                    return ServiceResult.failure(rsqDeptSr.getCode(),rsqDeptSr.getMessage());
                }
                //3 保存本地
                ServiceResult<Void> fetchSr = deptManageService.saveOrUpdateCorpDepartment(deptVO);
                if(!fetchSr.isSuccess()){
                    return ServiceResult.failure(fetchSr.getCode(),fetchSr.getMessage());
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
                Long deptId = deptIdArray[i];
                //1 从本地读取user
                ServiceResult<DepartmentVO> deptVOSr = deptManageService.getDepartmentByCorpIdAndDeptId(corpId, deptId);
                if(!deptVOSr.isSuccess()){
                    return ServiceResult.failure(deptVOSr.getCode(), deptVOSr.getMessage());
                }
                DepartmentVO deptVO = deptVOSr.getResult();
                // 如果数据库中未找到，则不做处理
                if(null == deptVO){
                    continue;
                }
                //2 同步日事清
                ServiceResult<Void> rsqDeptSr = rsqCorpCallbackService.deleteRsqDepartment(suiteKey, deptVO);
                if(!rsqDeptSr.isSuccess()){
                    return ServiceResult.failure(rsqDeptSr.getCode(),rsqDeptSr.getMessage());
                }
                //3 本地删除
                ServiceResult<Void> delSr = deptManageService.deleteDepartmentByCorpIdAndUserId(corpId, deptId);
                if(!delSr.isSuccess()){
                    return ServiceResult.failure(delSr.getCode(),delSr.getMessage());
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
