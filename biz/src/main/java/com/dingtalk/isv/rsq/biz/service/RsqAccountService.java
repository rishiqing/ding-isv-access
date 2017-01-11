package com.dingtalk.isv.rsq.biz.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dingtalk.isv.access.api.model.corp.CorpVO;
import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.model.suite.SuiteVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.api.service.suite.SuiteManageService;
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
 * 用于企业授权后批量读取企业部门和成员信息
 * Created by Wallace on 2016/11/29.
 */
public class RsqAccountService {
    private static final Logger bizLogger = LoggerFactory.getLogger("RSQ_REQUEST_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(RsqAccountService.class);

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
            if(!rsqCorpSr.isSuccess()){
                return ServiceResult.failure(rsqCorpSr.getCode(), rsqCorpSr.getMessage());
            }

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
     * @param departmentDO  部门的数据库对象
     * @return
     */
    public ServiceResult<Void> createRsqDepartment(String suiteKey, DepartmentDO departmentDO){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("deptId", departmentDO.getDeptId()),
                LogFormatter.KeyValue.getNew("corpId", departmentDO.getCorpId())
        ));
        try {
            String corpId = departmentDO.getCorpId();
            //  如果departmentDO的rsqId存在，则不重新创建部门
            if(null != departmentDO.getRsqId()){
                return ServiceResult.success(null);
            }

            CorpDO corpDO = corpDao.getCorpByCorpId(corpId);

            //  如果corpDO的rsqId不存在，那么返回失败
            if(null == corpDO.getRsqId()){
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),"corp rsqId not exists: " + corpDO.toString());
            }

            DepartmentDO parentDept = departmentDO.getParentId() == null ? null :
                    corpDepartmentDao.getDepartmentByCorpIdAndDeptId(corpId, departmentDO.getParentId());


            //  如果corpDO的rsqId不存在，那么就请求日事清服务器创建，创建成功后更新corpDO
            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);

            ServiceResult<RsqDepartment> rsqDeptSr = rsqAccountRequestHelper.createDepartment(suiteDO, corpDO, departmentDO, parentDept);

            if(!rsqDeptSr.isSuccess()){
                return ServiceResult.failure(rsqDeptSr.getCode(), rsqDeptSr.getMessage());
            }
            departmentDO.setRsqId(String.valueOf(rsqDeptSr.getResult().getId()));

            corpDepartmentDao.updateRsqInfo(departmentDO);

            return ServiceResult.success(null);
        } catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("deptId", departmentDO.getDeptId()),
                    LogFormatter.KeyValue.getNew("corpId", departmentDO.getCorpId())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("deptId", departmentDO.getDeptId()),
                    LogFormatter.KeyValue.getNew("corpId", departmentDO.getCorpId())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 创建公司员工
     * @param suiteKey
     * @param staffDO
     * @return
     */
    public ServiceResult<Void> createRsqTeamStaff(String suiteKey, StaffDO staffDO) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", staffDO.getCorpId()),
                LogFormatter.KeyValue.getNew("userID", staffDO.getUserId())
        ));
        try {

            //  如果staffDO的rsqUserId存在，则不重新发送请求创建
            if(null != staffDO.getRsqUserId()){
                return ServiceResult.success(null);
            }

            String userId = staffDO.getUserId();
            String corpId = staffDO.getCorpId();
            //  生成用户信息

            CorpDO corpDO = corpDao.getCorpByCorpId(corpId);
            if(null == corpDO || null == corpDO.getRsqId()){
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),"rsqId not found in corpDO: " + corpDO.toString());
            }

            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);

            //  保存用户信息到日事清系统
            String username = generateRsqUsername(suiteDO.getRsqAppName());  //自动生成用户名
            String password = generateRsqPassword();  //自动生成明文密码
            staffDO.setRsqUsername(username);
            staffDO.setRsqPassword(password);

            JSONArray rsqIdArray = convertRsqDepartment(corpId, staffDO.getDepartment());
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

            ServiceResult<RsqUser> rsqUserSr = rsqAccountRequestHelper.createUser(suiteDO, staffDO, corpDO, params);

            if(!rsqUserSr.isSuccess()){
                return ServiceResult.failure(rsqUserSr.getCode(),rsqUserSr.getMessage());
            }
            RsqUser user = rsqUserSr.getResult();
            staffDO.setRsqUserId(String.valueOf(user.getId()));

            corpStaffDao.updateRsqInfo(staffDO);
            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", staffDO.getCorpId()),
                    LogFormatter.KeyValue.getNew("userID", staffDO.getUserId())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", staffDO.getCorpId()),
                    LogFormatter.KeyValue.getNew("userID", staffDO.getUserId())
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

    public String generateRsqPassword(){
        return RandomStringUtils.randomAlphabetic(6);
    }

    public JSONArray convertRsqDepartment(String corpId, String dingDepartment){
        JSONArray orgArray = JSON.parseArray(dingDepartment);
        JSONArray rsqArray = new JSONArray();
        Iterator it = orgArray.iterator();

        while (it.hasNext()){
            Long orgId = new Long((Integer)it.next());
            DepartmentDO departmentDO = corpDepartmentDao.getDepartmentByCorpIdAndDeptId(corpId, orgId);
            String rsqId = departmentDO.getRsqId();
            if(null == rsqId){
                return null;
            }
            rsqArray.add(rsqId);
        }
        return rsqArray;
    }

    public JSONArray convertRsqDepartment(String corpId, List departmentList){
        JSONArray rsqArray = new JSONArray();
        Iterator it = departmentList.iterator();

        while (it.hasNext()){
            Long orgId;
            Object num = it.next();
            if(num instanceof Integer){
                orgId = new Long((Integer)num);
            }else{
                orgId = (Long)num;
            }
            DepartmentDO departmentDO = corpDepartmentDao.getDepartmentByCorpIdAndDeptId(corpId, orgId);
            String rsqId = departmentDO.getRsqId();
            if(null == rsqId){
                return null;
            }
            rsqArray.add(rsqId);
        }
        return rsqArray;
    }

    /**
     * 将departmentDO同步到日事清，并递归同步其子部门
     * @param suiteKey
     * @param departmentDO
     * @return
     */
    public ServiceResult<Void> createRecursiveSubDepartment(String suiteKey, DepartmentDO departmentDO){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("deptId", departmentDO.getDeptId()),
                LogFormatter.KeyValue.getNew("corpId", departmentDO.getCorpId())
        ));
        try{
            String corpId = departmentDO.getCorpId();
            Long deptId = departmentDO.getDeptId();

            ServiceResult deptSr = this.createRsqDepartment(suiteKey, departmentDO);
            if(!deptSr.isSuccess()){
                return ServiceResult.failure(deptSr.getCode(),deptSr.getMessage());
            }

            ServiceResult<List<DepartmentVO>> sr =  deptManageService.getDepartmentListByCorpIdAndParentId(corpId, deptId);
            if(!sr.isSuccess()){
                return ServiceResult.failure(sr.getCode(), sr.getMessage());
            }

            List<DepartmentVO> deptList = sr.getResult();
            if(0 == deptList.size()){
                return ServiceResult.success(null);
            }

            Iterator<DepartmentVO> it = deptList.iterator();
            while(it.hasNext()){
                DepartmentVO subDept = it.next();
                ServiceResult<Void> subSr = createRecursiveSubDepartment(suiteKey, DepartmentConverter.DepartmentVO2DepartmentDO(subDept));
                if(!subSr.isSuccess()){
                    return ServiceResult.failure(subSr.getCode(), subSr.getMessage());
                }
            }

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("deptId", departmentDO.getDeptId()),
                    LogFormatter.KeyValue.getNew("corpId", departmentDO.getCorpId())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("deptId", departmentDO.getDeptId()),
                    LogFormatter.KeyValue.getNew("corpId", departmentDO.getCorpId())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 将一个公司的所有部门同步到日事清
     * @param suiteKey
     * @param corpId
     * @return
     */
    public ServiceResult<Void> createAllCorpDepartment(String suiteKey, String corpId){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {
            ServiceResult<List<DepartmentVO>> listSr = deptManageService.getDepartmentListByCorpId(corpId);
            List<DepartmentVO> list = listSr.getResult();

            Iterator it = list.iterator();
            while(it.hasNext()){
                DepartmentVO departmentVO = (DepartmentVO)it.next();
                ServiceResult deptSr = this.createRsqDepartment(suiteKey, DepartmentConverter.DepartmentVO2DepartmentDO(departmentVO));
                if(!deptSr.isSuccess()){
                    return ServiceResult.failure(deptSr.getCode(),deptSr.getMessage());
                }
            }

            return ServiceResult.success(null);
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
     * 将一个公司的所有员工同步到日事清
     * @param suiteKey
     * @param corpId
     * @return
     */
    public ServiceResult<Void> createAllCorpStaff(String suiteKey, String corpId){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {

            ServiceResult<List<StaffVO>> listSr = staffManageService.getStaffListByCorpId(corpId);
            List<StaffVO> list = listSr.getResult();

            Iterator it = list.iterator();
            while(it.hasNext()){
                StaffVO staffVO = (StaffVO)it.next();
                ServiceResult staffSr = this.createRsqTeamStaff(suiteKey, StaffConverter.staffVO2StaffDO(staffVO));
                if(!staffSr.isSuccess()){
                    return ServiceResult.failure(staffSr.getCode(),staffSr.getMessage());
                }
            }
            return ServiceResult.success(null);
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
     * 同步所有的企业信息到日事清，包括：
     * 1  公司信息
     * 2  部门信息
     * 3  部门成员信息
     * @return
     */
    public ServiceResult<Void> syncAllCorp(String suiteKey, String corpId){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {
            //1  创建日事清企业
            ServiceResult<CorpVO> corpSr = this.createRsqTeam(suiteKey, corpId);
            if(!corpSr.isSuccess()){
                return ServiceResult.failure(corpSr.getCode(),corpSr.getMessage());
            }
            //2  创建企业部门
            ServiceResult<DepartmentVO> rootSr = deptManageService.getDepartmentByCorpIdAndDeptId(corpId, 1L);
            if(!rootSr.isSuccess()){
                return ServiceResult.failure(corpSr.getCode(),corpSr.getMessage());
            }
            ServiceResult<Void> rsqDeptSr = this.createRecursiveSubDepartment(suiteKey, DepartmentConverter.DepartmentVO2DepartmentDO(rootSr.getResult()));
            if(!rsqDeptSr.isSuccess()){
                return ServiceResult.failure(corpSr.getCode(),corpSr.getMessage());
            }

            //3  新建企业部门成员
            ServiceResult<Void> rsqDeptStaffSr = this.createAllCorpStaff(suiteKey, corpId);
            if(!rsqDeptStaffSr.isSuccess()){
                return ServiceResult.failure(corpSr.getCode(),corpSr.getMessage());
            }
            return ServiceResult.success(null);

        } catch (Exception e) {
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
}
