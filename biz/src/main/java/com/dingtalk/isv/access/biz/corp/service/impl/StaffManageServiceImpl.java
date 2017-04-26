package com.dingtalk.isv.access.biz.corp.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.model.corp.CorpTokenVO;
import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpStaffDao;
import com.dingtalk.isv.access.biz.corp.model.DepartmentDO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.helper.DepartmentConverter;
import com.dingtalk.isv.access.biz.corp.model.helper.StaffConverter;
import com.dingtalk.isv.access.biz.dingutil.CorpOapiRequestHelper;
import com.dingtalk.isv.access.biz.dingutil.CrmOapiRequestHelper;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.CorpUserDetailList;
import com.dingtalk.open.client.api.service.corp.CorpUserService;
import com.dingtalk.open.client.common.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;

/**
 * 开放平台企业通讯录员工相关接口封装
 * Created by mint on 16-1-22.
 */
public class StaffManageServiceImpl implements StaffManageService {
    private static final Logger bizLogger = LoggerFactory.getLogger("STAFF_MANAGE_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(StaffManageServiceImpl.class);

    @Autowired
    private CorpStaffDao corpStaffDao;
    @Autowired
    private CorpUserService corpUserService;
    @Autowired
    private CorpManageService corpManageService;
    @Autowired
    private DeptManageService deptManageService;
    @Autowired
    private CrmOapiRequestHelper crmOapiRequestHelper;
    @Autowired
    private CorpOapiRequestHelper corpRequestHelper;

    @Override
    public ServiceResult<StaffVO> getStaff(String staffId, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("staffId", JSON.toJSONString(staffId)),
                LogFormatter.KeyValue.getNew("corpId", JSON.toJSONString(corpId)),
                LogFormatter.KeyValue.getNew("suiteKey", JSON.toJSONString(suiteKey))
        ));
        try {
            ServiceResult<CorpTokenVO> corpTokenSr = corpManageService.getCorpToken(suiteKey, corpId);
            String corpToken = corpTokenSr.getResult().getCorpToken();
            CorpUserDetail corpUserDetail = corpUserService.getCorpUser(corpToken, staffId);
            if (null == corpId) {
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        "获取corpUser异常",
                        LogFormatter.KeyValue.getNew("staffId", staffId),
                        LogFormatter.KeyValue.getNew("corpId", corpId),
                        LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
                ));
            }
            StaffVO staffVO = StaffConverter.corpUser2StaffVO(corpUserDetail, corpId);
            return ServiceResult.success(staffVO);
        } catch (ServiceException e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "获取corpUser异常",
                    String.valueOf(e.getCode()),
                    e.getMessage(),
                    LogFormatter.KeyValue.getNew("staffId", JSON.toJSONString(staffId)),
                    LogFormatter.KeyValue.getNew("corpId", JSON.toJSONString(corpId)),
                    LogFormatter.KeyValue.getNew("suiteKey", JSON.toJSONString(suiteKey))
            ));
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("staffId", JSON.toJSONString(staffId)),
                    LogFormatter.KeyValue.getNew("corpId", JSON.toJSONString(corpId)),
                    LogFormatter.KeyValue.getNew("suiteKey", JSON.toJSONString(suiteKey))
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("staffId", JSON.toJSONString(staffId)),
                    LogFormatter.KeyValue.getNew("corpId", JSON.toJSONString(corpId)),
                    LogFormatter.KeyValue.getNew("suiteKey", JSON.toJSONString(suiteKey))
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<CorpUserDetailList> getStaffByDepartment(Long deptId, String corpId, String suiteKey, Long offset, Integer size, String order) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("deptId", JSON.toJSONString(deptId)),
                LogFormatter.KeyValue.getNew("corpId", JSON.toJSONString(corpId)),
                LogFormatter.KeyValue.getNew("suiteKey", JSON.toJSONString(suiteKey))
        ));
        try {
            ServiceResult<CorpTokenVO> corpTokenSr = corpManageService.getCorpToken(suiteKey, corpId);
            String corpToken = corpTokenSr.getResult().getCorpToken();

            CorpUserDetailList list = corpUserService.getCorpUserList(corpToken, deptId, offset, size, order);

            return ServiceResult.success(list);
        } catch (ServiceException e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "获取corpUser异常",
                    String.valueOf(e.getCode()),
                    e.getMessage(),
                    LogFormatter.KeyValue.getNew("staffId", JSON.toJSONString(deptId)),
                    LogFormatter.KeyValue.getNew("corpId", JSON.toJSONString(corpId)),
                    LogFormatter.KeyValue.getNew("suiteKey", JSON.toJSONString(suiteKey))
            ));
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("staffId", JSON.toJSONString(deptId)),
                    LogFormatter.KeyValue.getNew("corpId", JSON.toJSONString(corpId)),
                    LogFormatter.KeyValue.getNew("suiteKey", JSON.toJSONString(suiteKey))
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("staffId", JSON.toJSONString(deptId)),
                    LogFormatter.KeyValue.getNew("corpId", JSON.toJSONString(corpId)),
                    LogFormatter.KeyValue.getNew("suiteKey", JSON.toJSONString(suiteKey))
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<LoginUserVO> getStaffByAuthCode(String suitKey, String corpId, String code) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suitKey", suitKey),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("code", code)
        ));
        try {
            ServiceResult<CorpTokenVO> tokenSr = corpManageService.getCorpToken(suitKey, corpId);
            String accessToken = tokenSr.getResult().getCorpToken();
            ServiceResult<LoginUserVO> loginVoSr = corpRequestHelper.getStaffByAuthCode(suitKey, corpId, accessToken, code);
            if (loginVoSr.isSuccess() && null != loginVoSr.getResult()) {
                return loginVoSr;
            }
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suitKey", suitKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("code", code)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suitKey", suitKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("code", code)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    @Override
    public ServiceResult<Void> saveOrUpdateCorpStaff(StaffVO staffVO) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("staffVO", JSON.toJSONString(staffVO))
        ));
        try {
            StaffDO staffDO = StaffConverter.staffVO2StaffDO(staffVO);
            corpStaffDao.saveOrUpdateCorpStaff(staffDO);
            return ServiceResult.success(null);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("staffVO", JSON.toJSONString(staffVO))

            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("staffVO", JSON.toJSONString(staffVO))
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    @Override
    public ServiceResult<StaffVO> getStaffByCorpIdAndUserId(String corpId, String userId) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("userId", userId)
        ));
        try {

            StaffDO staffDO = corpStaffDao.getStaffByCorpIdAndUserId(corpId, userId);
            if (null == staffDO) {
                return ServiceResult.success(null);
            }
            StaffVO staffVO = StaffConverter.staffDO2StaffVO(staffDO);
            return ServiceResult.success(staffVO);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("userId", userId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("userId", userId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    @Override
    public ServiceResult<Void> getAndSaveStaffByDepartment(Long deptId, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("deptId", deptId)
        ));
        try {

            Integer size = 10;  //默认分页大小为10
            String order = null; //使用默认排序方式
            Long offset = 0L;
            Boolean hasMore = true;

            while (hasMore) {
                ServiceResult<CorpUserDetailList> userDetailSr = getStaffByDepartment(deptId, corpId, suiteKey,
                        offset, size, order);
                if(!userDetailSr.isSuccess()){
                    return ServiceResult.failure(userDetailSr.getCode(), userDetailSr.getMessage());
                }
                CorpUserDetailList detailList = userDetailSr.getResult();
                List<CorpUserDetail> userList = detailList.getUserlist();

                Iterator it = userList.iterator();

                while (it.hasNext()){
                    CorpUserDetail userDetail = (CorpUserDetail)it.next();
                    ServiceResult<Void> sr = saveOrUpdateCorpStaff(StaffConverter.corpUser2StaffVO(userDetail, corpId));
                    if(!sr.isSuccess()){
                        return ServiceResult.failure(sr.getCode(), sr.getMessage());
                    }
                }


                hasMore = detailList.isHasMore();
                offset ++;
            }

            return ServiceResult.success(null);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("deptId", deptId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("deptId", deptId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    @Override
    public ServiceResult<Void> getAndSaveAllCorpOrgStaff(String suiteKey, String corpId) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {
            ServiceResult<List<DepartmentVO>> dbSr = deptManageService.getDepartmentListByCorpId(corpId);

            if(!dbSr.isSuccess()){
                return ServiceResult.failure(dbSr.getCode(), dbSr.getMessage());
            }
            List<DepartmentVO> list = dbSr.getResult();

            Iterator it = list.iterator();
            while(it.hasNext()){
                DepartmentVO d = (DepartmentVO)it.next();
                ServiceResult<Void> sr = getAndSaveStaffByDepartment(d.getDeptId(), corpId, suiteKey);
                if(!sr.isSuccess()){
                    System.out.println("save department not success:" + sr.getCode());
                    return ServiceResult.failure(sr.getCode(), sr.getMessage());
                }
            }
            System.out.println("result list:" + JSON.toJSONString(list));
            return ServiceResult.success(null);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    @Override
    public ServiceResult<Void> getAndSaveStaff(String staffId, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("staffId", staffId)
        ));
        try {

            ServiceResult<StaffVO> staffSr = getStaff(staffId, corpId, suiteKey);
            if(!staffSr.isSuccess()){
                return ServiceResult.failure(staffSr.getCode(), staffSr.getMessage());
            }
            ServiceResult<Void> staffSaveSr = saveOrUpdateCorpStaff(staffSr.getResult());
            if(!staffSaveSr.isSuccess()){
                return ServiceResult.failure(staffSaveSr.getCode(), staffSaveSr.getMessage());
            }
            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("staffId", staffId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("staffId", staffId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    @Override
    /**
     * 本地删除后需要移入删除表做备份
     */
    public ServiceResult<Void> deleteStaffByCorpIdAndUserId(String corpId, String userId) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("userId", userId)
        ));
        try {

            StaffDO staffDO = corpStaffDao.getStaffByCorpIdAndUserId(corpId, userId);
            if(null == staffDO){
                return ServiceResult.success(null);
            }
            //本地删除并移入删除表以做备份
            corpStaffDao.saveStaffDeleted(staffDO);
            corpStaffDao.deleteStaffByCorpIdAndUserId(corpId, userId);
            return ServiceResult.success(null);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("userId", userId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("userId", userId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    public ServiceResult<List<StaffVO>> getStaffListByCorpId(String corpId){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {
            List<StaffDO> list = corpStaffDao.getStaffListByCorpId(corpId);

            return ServiceResult.success(StaffConverter.StaffDOList2StaffVOList(list));
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    public ServiceResult<List<String>> getStaffUserIdListByCorpId(String corpId){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {
            List<String> list = corpStaffDao.getStaffUserIdListByCorpId(corpId);

            return ServiceResult.success(list);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }
}
