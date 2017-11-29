package com.dingtalk.isv.access.web.controller.suite.manage;

import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.model.event.CorpOrgSyncEvent;
import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.api.service.suite.AppManageService;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.helper.StaffConverter;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.HttpResult;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.service.RsqAccountService;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wallace on 2016/11/18.
 */
@Controller
public class StaffManageController {
    private static final Logger bizLogger = LoggerFactory.getLogger("STAFF_MANAGE_LOGGER_APPENDER");
    private static final Logger    mainLogger = LoggerFactory.getLogger(StaffManageController.class);

    @Autowired
    private DeptManageService deptManageService;
    @Autowired
    private StaffManageService staffManageService;
    @Autowired
    private AppManageService appManageService;
    @Resource
    private HttpResult httpResult;
    @Autowired
    private EventBus asyncCorpOrgSyncEventBus;

    @Autowired
    private RsqAccountService rsqAccountService;

    @ResponseBody
    @RequestMapping(value = "/staff/userId", method = RequestMethod.GET)
    public Map<String, Object> getStaffByUserId(
            @RequestParam("appid") Long appId,
            @RequestParam("corpid") String corpId,
            @RequestParam("userid") String userId
    ){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("appid", appId),
                LogFormatter.KeyValue.getNew("corpid", corpId),
                LogFormatter.KeyValue.getNew("userid", userId)
        ));
        try {

            //  请求钉钉服务器获取当前登录的staff信息
            ServiceResult<StaffVO> staffVOSr = staffManageService.getStaffByCorpIdAndUserId(corpId, userId);

            if(!staffVOSr.isSuccess()){
                return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
            }

            StaffVO staffVO = staffVOSr.getResult();
            if(null == staffVO){
                return httpResult.getFailure(ServiceResultCode.CUSTOM_NOT_FIND.getErrCode(),ServiceResultCode.CUSTOM_NOT_FIND.getErrMsg());
            }

            //  返回用户，只保留必要信息即可
            Map<String,Object> jsapiConfig = new HashMap<String, Object>();
            jsapiConfig.put("user", StaffConverter.staffVO2StaffResult(staffVOSr.getResult()));

            return httpResult.getSuccess(jsapiConfig);
        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("appid", appId),
                    LogFormatter.KeyValue.getNew("corpid", corpId),
                    LogFormatter.KeyValue.getNew("userid", userId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("appid", appId),
                    LogFormatter.KeyValue.getNew("corpid", corpId),
                    LogFormatter.KeyValue.getNew("userid", userId)
            ));
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }

    }

    /**
     * 根据dept获取用户信息
     * @param suiteKey
     * @param corpId
     * @param deptId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/corp/dept/staff", method = RequestMethod.GET)
    public String getAndSaveAllCorpOrgStaff(
            @RequestParam("suiteKey") String suiteKey,
            @RequestParam("corpId") String corpId,
            @RequestParam("deptId") Long deptId
    ){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("deptId", deptId)
        ));
        try {
            ServiceResult<DepartmentVO> deptSr = deptManageService.getDepartmentByCorpIdAndDeptId(corpId, deptId);
            if(!deptSr.isSuccess()){
                return "get department failed:" + deptSr.getCode();
            }
            DepartmentVO d = deptSr.getResult();
            ServiceResult<Void> sr = staffManageService.getAndSaveStaffByDepartment(d.getDeptId(), corpId, suiteKey);
            if(!sr.isSuccess()){
                return "save department not success:" + sr.getCode();
            }
            return "success suiteKey: " + suiteKey + ", corpId: " + corpId + ", deptId: " + deptId;
        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("deptId", deptId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("deptId", deptId)
            ));
            return "failed";
        }
    }

    /**
     * 将指定corpId的corp中所有用户同步到日事清
     * @param suiteKey
     * @param corpId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/rsq/corp/staff/all", method = RequestMethod.GET)
    public String postSyncEvent(
            @RequestParam("suiteKey") String suiteKey,
            @RequestParam("corpId") String corpId
    ){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {
            //异步，更新钉钉的组织机构以及用户信息到本地，然后与ISV更新组织机构和人员信息
            CorpOrgSyncEvent corpOrgSyncEvent = new CorpOrgSyncEvent();
            corpOrgSyncEvent.setSuiteKey(suiteKey);
            corpOrgSyncEvent.setCorpId(corpId);
            asyncCorpOrgSyncEventBus.post(corpOrgSyncEvent);
            return "success suiteKey: " + suiteKey + ", corpId: " + corpId;
        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ));
            return "failed";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/staff/authCode", method = RequestMethod.GET)
    public Map<String, Object> getStaffByAuthCode(
            @RequestParam("appid") Long appId,
            @RequestParam("corpid") String corpId,
            @RequestParam("code") String code
    ){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("appid", appId),
                LogFormatter.KeyValue.getNew("corpid", corpId),
                LogFormatter.KeyValue.getNew("code", code)
        ));
        try {
            //  根据appId获取suiteKey
            ServiceResult<AppVO> appVOSr = appManageService.getAppByAppId(appId);
            String suiteKey = appVOSr.getResult().getSuiteKey();

            //  请求钉钉服务器获取当前登录的staff信息
            ServiceResult<LoginUserVO> loginUserVOSr = staffManageService.getStaffByAuthCode(suiteKey, corpId, code);
            LoginUserVO loginUserVO = loginUserVOSr.getResult();

//            ServiceResult<StaffVO> staffVOSr = rsqAccountService.createRsqTeamStaff(suiteKey, corpId, loginUserVO);
            ServiceResult<StaffVO> staffVOSr = staffManageService.getStaffByCorpIdAndUserId(corpId, loginUserVO.getUserId());

            if(!staffVOSr.isSuccess()){
                return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
            }

            //  返回用户，只保留必要信息即可
            Map<String,Object> jsapiConfig = new HashMap<String, Object>();
            jsapiConfig.put("user", StaffConverter.staffVO2StaffResult(staffVOSr.getResult()));
            return httpResult.getSuccess(jsapiConfig);
        }catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("appid", appId),
                    LogFormatter.KeyValue.getNew("corpid", corpId),
                    LogFormatter.KeyValue.getNew("code", code)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("appid", appId),
                    LogFormatter.KeyValue.getNew("corpid", corpId),
                    LogFormatter.KeyValue.getNew("code", code)
            ));
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }

    }

    public static void main(String[] args) {
//        Long v1 = 30L;
//        Integer v2 = 30;
//        assert v1.equals(v2);

        Double v3 = 3.3d;
        Float v4 = 3.3f;
        assert v3.equals(v4);
    }
}
