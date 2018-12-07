package com.dingtalk.isv.rsq.biz.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dingtalk.isv.access.api.model.corp.CorpVO;
import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpDepartmentDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpStaffDao;
import com.dingtalk.isv.access.biz.corp.model.CorpDO;
import com.dingtalk.isv.access.biz.corp.model.DepartmentDO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.helper.CorpConverter;
import com.dingtalk.isv.access.biz.corp.model.helper.DepartmentConverter;
import com.dingtalk.isv.access.biz.corp.model.helper.StaffConverter;
import com.dingtalk.isv.access.biz.suite.dao.SuiteDao;
import com.dingtalk.isv.access.biz.suite.model.SuiteDO;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.exceptions.RsqIntegrationException;
import com.dingtalk.isv.rsq.biz.httputil.RsqAccountRequestHelper;
import com.dingtalk.isv.rsq.biz.model.RsqCorp;
import com.dingtalk.isv.rsq.biz.model.RsqDepartment;
import com.dingtalk.isv.rsq.biz.model.RsqUser;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * 用于同步企业事件回调对人员的增删改查到日事清
 * Created by Wallace on 2016/11/29.
 */
public class RsqCorpCallbackService {
    private static final Logger bizLogger = LoggerFactory.getLogger("RSQ_CORP_CALLBACK_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(RsqCorpCallbackService.class);

    @Autowired
    private SuiteDao suiteDao;
    @Autowired
    private CorpDao corpDao;
    @Autowired
    private CorpDepartmentDao corpDepartmentDao;
    @Autowired
    private CorpStaffDao corpStaffDao;
    @Autowired
    private RsqAccountRequestHelper rsqAccountRequestHelper;
    @Autowired
    private StaffManageService staffManageService;
    @Autowired
    private DeptManageService deptManageService;
    @Autowired
    private RsqAccountService rsqAccountService;

    /**
     * 创建公司，分为以下几步：
     * 1  根据corpId查询是否有记录，是否rsqId存在，如果rsqId存在，则直接返回rsqId
     * 2  如果记录不存在或者rsqId不存在，则发送到日事清服务器请求创建
     * 3  保存返回结果
     * @param suiteKey
     * @param corpId
     * @return
     */
    public ServiceResult<CorpVO> createRsqTeam(String suiteKey, String corpId){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {
            CorpDO corpDO = corpDao.getCorpByCorpId(corpId);

            //  如果corpDO的rsqId存在，那么直接返回
            if(null != corpDO.getRsqId()){
                return ServiceResult.success(CorpConverter.CorpDO2CorpVO(corpDO));
            }

            //  如果corpDO的rsqId不存在，那么就请求日事清服务器创建，创建成功后更新corpDO
            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);
            ServiceResult<RsqCorp> rsqCorpSr = rsqAccountRequestHelper.createCorp(suiteDO, corpDO);

            corpDO.setRsqId(String.valueOf(rsqCorpSr.getResult().getId()));

            corpDao.updateRsqInfo(corpDO);

            CorpVO corpVO = CorpConverter.CorpDO2CorpVO(corpDO);

            return ServiceResult.success(corpVO);
        } catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 创建部门，分为以下几步：
     * 1  根据corpId和deptId查询是否有记录，是否department的rsqId存在，则直接返回department
     * 2  如果记录不存在或者rsqId不存在，则发送到日事清服务器请求创建
     * 3  保存返回结果
     * @param suiteKey
     * @param departmentVO
     * @return
     */
    public ServiceResult<DepartmentVO> createRsqDepartment(String suiteKey, DepartmentVO departmentVO){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
        ));
        try {
            String corpId = departmentVO.getCorpId();
            Long deptId = departmentVO.getDeptId();
            Long parentId = departmentVO.getParentId();

            //  如果departmentDO的rsqId存在，则不重新创建部门
            if(null != departmentVO.getRsqId()){
                return ServiceResult.success(null);
            }

            CorpDO corpDO = corpDao.getCorpByCorpId(corpId);

            //  如果corpDO的rsqId不存在，那么返回失败
            if(null == corpDO.getRsqId()){
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),"corp rsqId not exists: " + corpDO.toString());
            }

            DepartmentDO parentDept = departmentVO.getParentId() == null ? null :
                    corpDepartmentDao.getDepartmentByCorpIdAndDeptId(corpId, parentId);


            //  如果corpDO的rsqId不存在，那么就请求日事清服务器创建，创建成功后更新corpDO
            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);

            ServiceResult<RsqDepartment> rsqDeptSr = rsqAccountRequestHelper.createDepartment(suiteDO,
                    corpDO,
                    DepartmentConverter.DepartmentVO2DepartmentDO(departmentVO),
                    parentDept);

            if(!rsqDeptSr.isSuccess()){
                return ServiceResult.failure(rsqDeptSr.getCode(), rsqDeptSr.getMessage());
            }
            departmentVO.setRsqId(String.valueOf(rsqDeptSr.getResult().getId()));

            corpDepartmentDao.updateRsqInfo(DepartmentConverter.DepartmentVO2DepartmentDO(departmentVO));

            return ServiceResult.success(null);
        } catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                    LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                    LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 更新部门
     * @param suiteKey
     * @param departmentVO
     * @return
     */
    public ServiceResult<DepartmentVO> updateRsqDepartment(String suiteKey, DepartmentVO departmentVO){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
        ));
        try {
            String corpId = departmentVO.getCorpId();
            Long deptId = departmentVO.getDeptId();
            Long parentId = departmentVO.getParentId();
            //  suiteKey
            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);
            //  读取本地保存的原staff，从而获取到id
            ServiceResult<DepartmentVO> dbDOSr = deptManageService.getDepartmentByCorpIdAndDeptId(corpId, deptId);
            if(!dbDOSr.isSuccess()){
                return ServiceResult.failure(dbDOSr.getCode(), dbDOSr.getMessage());
            }
            DepartmentVO orgDept = dbDOSr.getResult();
            if(null == orgDept.getRsqId()){
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        "系统异常:department rsqId is null:",
                        LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                        LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                        LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
                ));
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), "department rsqId is null");
            }
            departmentVO.setRsqId(orgDept.getRsqId());

            DepartmentVO parentDepartmentVO = null;
            if(null != parentId){
                ServiceResult<DepartmentVO> parentDeptSr = deptManageService.getDepartmentByCorpIdAndDeptId(corpId, parentId);
                if(!parentDeptSr.isSuccess()){
                    return ServiceResult.failure(parentDeptSr.getCode(), parentDeptSr.getMessage());
                }
                parentDepartmentVO = parentDeptSr.getResult();
                if(null == parentDepartmentVO.getRsqId()){
                    bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                            "系统异常:one of the department don't have rsqId:",
                            LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                            LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                            LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
                    ));
                    return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),"one of the department don't have rsqId, see log error for detail");
                }
            }

            //  提交更新
            ServiceResult<RsqDepartment> rsqDeptSr = rsqAccountRequestHelper.updateDepartment(suiteDO,
                    DepartmentConverter.DepartmentVO2DepartmentDO(departmentVO),
                    DepartmentConverter.DepartmentVO2DepartmentDO(parentDepartmentVO));

            if(!rsqDeptSr.isSuccess()){
                return ServiceResult.failure(rsqDeptSr.getCode(),rsqDeptSr.getMessage());
            }

            return ServiceResult.success(null);
        } catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                    LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                    LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 删除部门
     * @param suiteKey
     * @param departmentVO
     * @return
     */
    public ServiceResult<Void> deleteRsqDepartment(String suiteKey, DepartmentVO departmentVO){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
        ));
        try {
            if(null == departmentVO.getRsqId()){
                bizLogger.warn(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        "系统警告: deleted staff rsqUserId is null:",
                        LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                        LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                        LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
                ));
                return ServiceResult.success(null);
            }else{
                //  suiteKey
                SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);
                //  提交更新
                ServiceResult<RsqDepartment> rsqDeptSr = rsqAccountRequestHelper.deleteDepartment(suiteDO,
                        DepartmentConverter.DepartmentVO2DepartmentDO(departmentVO));

                if(!rsqDeptSr.isSuccess()){
                    return ServiceResult.failure(rsqDeptSr.getCode(),rsqDeptSr.getMessage());
                }

                return ServiceResult.success(null);
            }
        } catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                    LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("deptId", departmentVO.getDeptId()),
                    LogFormatter.KeyValue.getNew("corpId", departmentVO.getCorpId())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 创建公司员工
     * @param suiteKey
     * @param staffVO
     * @return
     */
    public ServiceResult<StaffVO> createRsqTeamStaff(String suiteKey, StaffVO staffVO) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
        ));
        try {

            //  如果staffDO的rsqUserId存在，则不重新发送请求创建
            if(null != staffVO.getRsqUserId()){
                return ServiceResult.success(null);
            }

            String userId = staffVO.getStaffId();
            String corpId = staffVO.getCorpId();
            //  生成用户信息

            CorpDO corpDO = corpDao.getCorpByCorpId(corpId);
            if(null == corpDO || null == corpDO.getRsqId()){
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),"rsqId not found in corpDO: " + corpDO.toString());
            }

            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);

            //  保存用户信息到日事清系统
            String username = generateRsqUsername(suiteDO.getRsqAppName());  //自动生成用户名
            String password = rsqAccountService.generateRsqPassword(username);  //自动生成明文密码
            staffVO.setRsqUsername(username);
            staffVO.setRsqPassword(password);

            JSONArray rsqIdArray = rsqAccountService.convertRsqDepartment(corpId, staffVO.getDepartment());
            if(null == rsqIdArray){
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        "系统异常:one of the staff department don't have rsqId:",
                        LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                        LogFormatter.KeyValue.getNew("corpId", corpId),
                        LogFormatter.KeyValue.getNew("userId", userId)
                ));
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),"one of the staff department don't have rsqId, see log error for detail");
            }
            Map params = new HashMap<String, Object>();
            params.put("rsqDepartment", rsqIdArray);

            ServiceResult<RsqUser> rsqUserSr = rsqAccountRequestHelper.createUser(suiteDO, StaffConverter.staffVO2StaffDO(staffVO), corpDO, params);

            if(!rsqUserSr.isSuccess()){
                return ServiceResult.failure(rsqUserSr.getCode(),rsqUserSr.getMessage());
            }
            RsqUser user = rsqUserSr.getResult();
            staffVO.setRsqUserId(String.valueOf(user.getId()));
            //TODO 为控制并发，保证username和password与日事清系统一致，使用返回值作为rsqUsername和rsqPassword
            staffVO.setRsqUsername(user.getUsername());
            staffVO.setRsqPassword(rsqAccountService.generateRsqPassword(user.getUsername()));
            staffVO.setRsqLoginToken(user.getLoginToken());

            corpStaffDao.updateRsqInfo(StaffConverter.staffVO2StaffDO(staffVO));
            return ServiceResult.success(staffVO);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                    LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                    LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());

        }
    }

    /**
     * 更新公司员工
     * @param suiteKey
     * @param staffVO
     * @return
     */
    public ServiceResult<Void> updateRsqTeamStaff(String suiteKey, StaffVO staffVO) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
        ));
        try {
            //  suiteKey
            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);
            //  读取本地保存的原staff，从而获取到id
            ServiceResult<StaffVO> dbDOSr = staffManageService.getStaffByCorpIdAndUserId(staffVO.getCorpId(), staffVO.getStaffId());
            if(!dbDOSr.isSuccess()){
                return ServiceResult.failure(dbDOSr.getCode(), dbDOSr.getMessage());
            }
            StaffVO orgStaff = dbDOSr.getResult();
            if(null == orgStaff.getRsqUserId()){
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        "系统异常:staff rsqUserId is null:",
                        LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                        LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                        LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
                ));
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), "staff rsqUserId is null");
            }
            staffVO.setRsqUserId(orgStaff.getRsqUserId());

            //  转换deptId
            JSONArray rsqIdArray = rsqAccountService.convertRsqDepartment(staffVO.getCorpId(), staffVO.getDepartment());
            if(null == rsqIdArray){
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        "系统异常:one of the staff department don't have rsqId:",
                        LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                        LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                        LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
                ));
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),"one of the staff department don't have rsqId, see log error for detail");
            }
            Map params = new HashMap<String, Object>();
            params.put("rsqDepartment", rsqIdArray);

            //  提交更新
            ServiceResult<RsqUser> rsqUserSr = rsqAccountRequestHelper.updateUser(suiteDO, StaffConverter.staffVO2StaffDO(staffVO), params);

            if(!rsqUserSr.isSuccess()){
                return ServiceResult.failure(rsqUserSr.getCode(),rsqUserSr.getMessage());
            }

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                    LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                    LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 设置公司员工为管理员
     * @param suiteKey
     * @param staffVO
     * @return
     */
    public ServiceResult<Void> setRsqTeamStaffAdmin(String suiteKey, StaffVO staffVO) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
        ));
        try {
            //  suiteKey
            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);
            //  读取本地保存的原staff，从而获取到id
            ServiceResult<StaffVO> dbDOSr = staffManageService.getStaffByCorpIdAndUserId(staffVO.getCorpId(), staffVO.getStaffId());
            if(!dbDOSr.isSuccess()){
                return ServiceResult.failure(dbDOSr.getCode(), dbDOSr.getMessage());
            }
            StaffVO orgStaff = dbDOSr.getResult();
            if(null == orgStaff.getRsqUserId()){
                bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        "系统异常:staff rsqUserId is null:",
                        LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                        LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                        LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
                ));
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), "staff rsqUserId is null");
            }
            staffVO.setRsqUserId(orgStaff.getRsqUserId());

            //  提交更新
            ServiceResult<RsqUser> rsqUserSr = rsqAccountRequestHelper.setUserAdmin(suiteDO, StaffConverter.staffVO2StaffDO(staffVO));

            if(!rsqUserSr.isSuccess()){
                return ServiceResult.failure(rsqUserSr.getCode(),rsqUserSr.getMessage());
            }

            return ServiceResult.success(null);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                    LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                    LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());

        }
    }

    /**
     * 从公司删除员工
     * @param suiteKey
     * @param staffVO
     * @return
     */
    public ServiceResult<Void> removeUserFromTeam(String suiteKey, StaffVO staffVO) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
        ));
        try {
            if(null == staffVO.getRsqUserId()){
                bizLogger.warn(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                        "系统警告: deleted staff rsqUserId is null:",
                        LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                        LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                        LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
                ));
                return ServiceResult.success(null);
            }else{
                //  suiteKey
                SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);
                // 公司
                CorpDO corpDO = corpDao.getCorpByCorpId(staffVO.getCorpId());
                //  提交更新
                ServiceResult<Void> rsqUserSr = rsqAccountRequestHelper.removeUser(suiteDO, corpDO, StaffConverter.staffVO2StaffDO(staffVO));

                if(!rsqUserSr.isSuccess()){
                    return ServiceResult.failure(rsqUserSr.getCode(),rsqUserSr.getMessage());
                }

                return ServiceResult.success(null);
            }

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                    LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", staffVO.getCorpId()),
                    LogFormatter.KeyValue.getNew("staffVO", staffVO.getStaffId())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());

        }
    }

    public String generateRsqUsername(String appName){
        StringBuffer sb = new StringBuffer();
        sb.append(RandomStringUtils.randomAlphabetic(5))
                .append("_")
                .append(new Date().getTime())
                .append("@")
                .append(appName)
                .append(".rishiqing.com");
        return  sb.toString();
    }
}
