package com.dingtalk.isv.access.web.controller.suite.manage;

import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.api.service.suite.AppManageService;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.HttpResult;
import com.dingtalk.isv.common.model.ServiceResult;
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

    @ResponseBody
    @RequestMapping(value = "/staff/authCode/{appId}", method = RequestMethod.GET)
    public Map<String, Object> getStaffByAuthCode(
            @PathVariable("appid") Long appId,
            @RequestParam("corpid") String corpId,
            @RequestParam("code") String code
    ){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("appid", appId),
                LogFormatter.KeyValue.getNew("corpid", corpId),
                LogFormatter.KeyValue.getNew("code", code)
        ));
        //  根据appId获取suiteKey
        ServiceResult<AppVO> appVOSr = appManageService.getAppByAppId(appId);
        String suiteKey = appVOSr.getResult().getSuiteKey();
        try {
            //  请求钉钉服务器获取当前登录的staff信息
            ServiceResult<LoginUserVO> loginUserVOSr = staffManageService.getStaffByAuthCode(suiteKey, corpId, code);
            LoginUserVO loginUserVO = loginUserVOSr.getResult();
            //  生成用户信息
            ServiceResult<StaffVO> staffVOSr = staffManageService.getStaffByCorpIdAndUserId(corpId, loginUserVO.getUserId());
            StaffVO staffVO = staffVOSr.getResult();
            if(null == staffVO){
                staffVO = staffManageService.getStaff(loginUserVO.getUserId(), corpId, suiteKey).getResult();
                //  保存用户信息到日事清系统
                //  TODO  注册到日事清用户的方法
                //  String username = "xxxxxx";  //TODO 自动生成用户名
                //  String password = "******";  //TODO 自动生成明文密码
                //  RsqStaff rsqStaff = new rsqStaff
                //  rsqStaff = rsqStaffManagerService.registerUser();
                //  保存用户信息到auth服务器
                //  staffVO.setRsqId(rsqStaff.getId());
                //  staffVO.setRsqUsername(rsqStaff.getUsername());
                //  staffVO.setRsqSecret(rsqStaff.getSecret());
                  staffManageService.saveOrUpdateCorpStaff(staffVO);
            }else{
                //  如果系统中已存在，看是否需要更新
            }


            //  返回用户，只保留必要信息即可
            Map<String,Object> jsapiConfig = new HashMap<String, Object>();
            jsapiConfig.put("user",staffVO);
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
