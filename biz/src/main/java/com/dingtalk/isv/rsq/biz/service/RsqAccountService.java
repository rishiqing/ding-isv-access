package com.dingtalk.isv.rsq.biz.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dingtalk.isv.access.api.model.corp.CorpVO;
import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.model.event.OrderChargeEvent;
import com.dingtalk.isv.access.api.model.suite.SuiteVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.api.service.suite.SuiteManageService;
import com.dingtalk.isv.access.biz.constant.SystemConstant;
import com.dingtalk.isv.access.biz.corp.dao.CorpDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpDepartmentDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpStaffDao;
import com.dingtalk.isv.access.biz.corp.model.CorpDO;
import com.dingtalk.isv.access.biz.corp.model.DepartmentDO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.helper.CorpConverter;
import com.dingtalk.isv.access.biz.corp.model.helper.DepartmentConverter;
import com.dingtalk.isv.access.biz.corp.model.helper.StaffConverter;
import com.dingtalk.isv.access.biz.order.dao.OrderEventDao;
import com.dingtalk.isv.access.biz.order.dao.OrderStatusDao;
import com.dingtalk.isv.access.biz.order.model.OrderEventDO;
import com.dingtalk.isv.access.biz.order.model.OrderStatusDO;
import com.dingtalk.isv.access.biz.suite.dao.SuiteDao;
import com.dingtalk.isv.access.biz.suite.model.SuiteDO;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.event.mq.RsqSyncMessage;
import com.dingtalk.isv.rsq.biz.httputil.RsqAccountRequestHelper;
import com.dingtalk.isv.rsq.biz.model.RsqCorp;
import com.dingtalk.isv.rsq.biz.model.RsqDepartment;
import com.dingtalk.isv.rsq.biz.model.RsqUser;
import com.google.common.eventbus.AsyncEventBus;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 用于企业授权后批量读取企业部门和成员信息
 * Created by Wallace on 2016/11/29.
 */
public class RsqAccountService {
    private static final Logger bizLogger = LoggerFactory.getLogger("RSQ_REQUEST_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(RsqAccountService.class);

    //  计算密码需要加的盐值，暂时放在这里，以后放到配置文件中
    private static final String md5Salt = "3385A05EECE3B086E9369F86CBA4E478";

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
    private OrderEventDao orderEventDao;
    @Autowired
    private OrderStatusDao orderStatusDao;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    @Qualifier("rsqSyncCallBackQueue")
    private javax.jms.Queue rsqSyncCallBackQueue;
    @Autowired
    private AsyncEventBus asyncOrderChargeEventBus;

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
     * 同步部门
     * @param suiteKey
     * @param corpId
     * @param department
     * @return
     */
    public ServiceResult<Void> syncDepartment(String suiteKey, String corpId, LinkedHashMap<String,Object> department){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {
            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);
            // 公司
            CorpDO corpDO = corpDao.getCorpByCorpId(corpId);

            ServiceResult<ArrayList<DepartmentDO>> syncSr = rsqAccountRequestHelper.syncDepartment(suiteDO, corpDO, department);
            if(!syncSr.isSuccess()){
                return ServiceResult.failure(syncSr.getCode(), syncSr.getMessage());
            }
            ArrayList<DepartmentDO> departmentDOs = syncSr.getResult();

            for(DepartmentDO departmentDO: departmentDOs){
                corpDepartmentDao.updateRsqInfoById(departmentDO);
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
    public ServiceResult<StaffDO> createRsqTeamStaff(String suiteKey, StaffDO staffDO) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", staffDO.getCorpId()),
                LogFormatter.KeyValue.getNew("userID", staffDO.getUserId())
        ));
        try {

            //  如果staffDO的rsqUserId存在，则不重新发送请求创建
            if(null != staffDO.getRsqUserId()){
                return ServiceResult.success(staffDO);
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
            String password = generateRsqPassword(username);  //自动生成明文密码
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
            // 为控制并发，保证username和password与日事清系统一致，使用返回值作为rsqUsername和rsqPassword
            staffDO.setRsqUsername(user.getUsername());
            staffDO.setRsqPassword(generateRsqPassword(user.getUsername()));

            corpStaffDao.updateRsqInfo(staffDO);
            return ServiceResult.success(staffDO);

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

    /**
     * 移出公司员工
     * @param suiteKey
     * @param corpId            钉钉公司id
     * @param removeUserIds     目前公司里存在的用户字符串
     * @return
     */
    public ServiceResult<Void> removeResignedStaff(String suiteKey, String corpId, String removeUserIds) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("removeUserIds", removeUserIds)
        ));
        try {
            CorpDO corpDO = corpDao.getCorpByCorpId(corpId);
            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);

            ServiceResult rsqUserSr = rsqAccountRequestHelper.removeResignedStaff(suiteDO, corpDO, removeUserIds);
            if(!rsqUserSr.isSuccess()){
                return ServiceResult.failure(rsqUserSr.getCode(),rsqUserSr.getMessage());
            }
            return ServiceResult.success(null);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("removeUserIds", removeUserIds)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("removeUserIds", removeUserIds)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 设置staffDO是否是管理员
     * @param suiteKey
     * @param staffDO
     * @return
     */
    public ServiceResult<Void> updateRsqTeamAdmin(String suiteKey, StaffDO staffDO) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", staffDO.getCorpId()),
                LogFormatter.KeyValue.getNew("userID", staffDO.getUserId())
        ));
        try {

            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);


            ServiceResult<RsqUser> rsqUserSr = rsqAccountRequestHelper.setUserAdmin(suiteDO, staffDO);

            if(!rsqUserSr.isSuccess()){
                return ServiceResult.failure(rsqUserSr.getCode(), rsqUserSr.getMessage());
            }
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

    public String generateRsqPassword(String username){
        MessageDigest md5 = null;
        String src = username + md5Salt;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "生成用户密码异常",
                    LogFormatter.KeyValue.getNew("username", username)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "生成用户密码异常",
                    LogFormatter.KeyValue.getNew("username", username)
            ), e);
            return "123456";
        }
        byte[] bs = md5.digest(src.getBytes());
        return new String(new Hex().encode(bs)).substring(3,9);
//        return RandomStringUtils.randomAlphabetic(6);
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

            StringBuilder existUserIdsSb = new StringBuilder();
            Iterator it = list.iterator();
            while(it.hasNext()){
                StaffVO staffVO = (StaffVO)it.next();
                ServiceResult<StaffDO> staffSr = this.createRsqTeamStaff(suiteKey, StaffConverter.staffVO2StaffDO(staffVO));
                if(!staffSr.isSuccess()){
                    return ServiceResult.failure(staffSr.getCode(),staffSr.getMessage());
                }
                StaffDO staffDO = staffSr.getResult();
                existUserIdsSb.append(staffDO.getRsqUserId());
                existUserIdsSb.append(",");
            }
            String existUserIds = existUserIdsSb.toString();
            if(!"".equals(existUserIds)){
                ServiceResult staffSr = this.removeResignedStaff(suiteKey,corpId,existUserIds);
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
     * 设置corpId中的所有管理员
     * @param suiteKey
     * @param corpId
     * @return
     */
    public ServiceResult<Void> updateAllCorpAdmin(String suiteKey, String corpId){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {
            List<StaffDO> list = corpStaffDao.getStaffListByCorpIdAndIsAdmin(corpId, true);

            Iterator it = list.iterator();
            while(it.hasNext()){
                StaffDO staffDO = (StaffDO) it.next();
                ServiceResult deptSr = this.updateRsqTeamAdmin(suiteKey, staffDO);
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
     * 查看是否有需要充值的订单，如果有那么调用接口进行充值
     * @param suiteKey
     * @param corpId
     */
    private void checkOrderCharge(String suiteKey, String corpId){
        OrderEventDO dbEvent = orderEventDao.getOrderEventBySuiteKeyAndCorpIdAndLatest(suiteKey, corpId);
        if(dbEvent == null){
            return;
        }
        OrderStatusDO dbOrderStatus = orderStatusDao.getOrderStatusByOrderId(dbEvent.getOrderId());
        //  要么orderStatus不存在，要么orderStatus的状态为初始的状态，这两种情况都进行充值
        if(dbOrderStatus == null || SystemConstant.ORDER_STATUS_PAID.equals(dbOrderStatus.getStatus())){
            //  使用eventBus异步调用
            OrderChargeEvent event = new OrderChargeEvent();
            event.setSuiteKey(dbEvent.getSuiteKey());
            event.setOrderEventId(dbEvent.getId());
            asyncOrderChargeEventBus.post(event);
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
            //1  同步日事清企业
            ServiceResult<CorpVO> corpSr = this.createRsqTeam(suiteKey, corpId);
            if(!corpSr.isSuccess()){
                return ServiceResult.failure(corpSr.getCode(),corpSr.getMessage());
            }
            checkOrderCharge(suiteKey, corpId);

            //2  同步企业部门
            //根部门
            ServiceResult<DepartmentVO> rootSr = deptManageService.getDepartmentByCorpIdAndDeptId(corpId, 1L);
            if(!rootSr.isSuccess()){
                return ServiceResult.failure(rootSr.getCode(),rootSr.getMessage());
            }
            ServiceResult<LinkedHashMap<String,Object>> departmentSr = this.assembleDepartment(corpId,rootSr.getResult());
            if(!departmentSr.isSuccess()){
                return ServiceResult.failure(departmentSr.getCode(),departmentSr.getMessage());
            }
            ServiceResult syncSr = this.syncDepartment(suiteKey, corpId, departmentSr.getResult());
            if(!syncSr.isSuccess()){
                return ServiceResult.failure(syncSr.getCode(),syncSr.getMessage());
            }

            //3  新建企业部门成员
            ServiceResult<Void> rsqDeptStaffSr = this.createAllCorpStaff(suiteKey, corpId);
            if(!rsqDeptStaffSr.isSuccess()){
                return ServiceResult.failure(rsqDeptStaffSr.getCode(),rsqDeptStaffSr.getMessage());
            }

            //4  更新企业部门的管理员状态
            ServiceResult<Void> rsqAdminSr = this.updateAllCorpAdmin(suiteKey, corpId);
            if(!rsqAdminSr.isSuccess()){
                return ServiceResult.failure(rsqAdminSr.getCode(),rsqAdminSr.getMessage());
            }

            //5  当全部都同步成功后，发到corpAuthSuiteQueue队列中，由第三方异步处理
            jmsTemplate.send(rsqSyncCallBackQueue,new RsqSyncMessage(suiteKey, corpId));
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
    /**
     * 组装部门组织架构
     */
    public ServiceResult<LinkedHashMap<String,Object>> assembleDepartment(String corpId,DepartmentVO departmentVO){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpusId", corpId),
                LogFormatter.KeyValue.getNew("departmentId", departmentVO.toString())
        ));
        try {
            // 部门下所有子部门
            ServiceResult<List<DepartmentVO>> deptListSr = deptManageService.getDepartmentListByCorpIdAndParentId(corpId, departmentVO.getDeptId());
            if(!deptListSr.isSuccess()){
                return ServiceResult.failure(deptListSr.getCode(),deptListSr.getMessage());
            }
            List<DepartmentVO> deptList = deptListSr.getResult();
            // 当前部门Map
            LinkedHashMap<String,Object> departmentMap = new LinkedHashMap<String,Object>();
            departmentMap.put("id", departmentVO.getId());
            departmentMap.put("name", departmentVO.getName());
            departmentMap.put("deptId", departmentVO.getDeptId());
            departmentMap.put("parentId", departmentVO.getParentId()==null?0:departmentVO.getParentId());
            departmentMap.put("rsqId", departmentVO.getRsqId());
            departmentMap.put("outerId", departmentVO.getCorpId() + "--" + departmentVO.getDeptId());
            departmentMap.put("orderNum", departmentVO.getOrder());
            ArrayList<LinkedHashMap> child = new ArrayList<LinkedHashMap>();
            departmentMap.put("child", child);

            // 组装子部门
            for (DepartmentVO childDepartmentVO: deptList){
                ServiceResult<LinkedHashMap<String,Object>> childListSr = assembleDepartment(corpId,childDepartmentVO);
                if(!childListSr.isSuccess()){
                    return ServiceResult.failure(childListSr.getCode(),childListSr.getMessage());
                }
                child.add(childListSr.getResult());
            }
            return ServiceResult.success(departmentMap);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("corpusId", corpId),
                    LogFormatter.KeyValue.getNew("departmentId", departmentVO.toString())
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("corpusId", corpId),
                    LogFormatter.KeyValue.getNew("departmentId", departmentVO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());

        }
    }
}
