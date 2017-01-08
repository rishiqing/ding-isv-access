package com.dingtalk.isv.access.web.controller.suite.manage;

import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.api.service.suite.AppManageService;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.helper.StaffConverter;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.HttpResult;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.service.RsqAccountService;
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
    private StaffManageService staffManageService;
    @Autowired
    private AppManageService appManageService;
    @Resource
    private HttpResult httpResult;

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

}
