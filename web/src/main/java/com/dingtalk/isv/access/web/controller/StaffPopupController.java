package com.dingtalk.isv.access.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.suite.AppManageService;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.HttpResult;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.client.PopupService;
import com.dingtalk.isv.rsq.biz.model.PopupInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wallace Mao
 * Date: 2018-10-19 17:11
 */
@Controller
public class StaffPopupController {
    private static final Logger mainLogger = LoggerFactory.getLogger(IdMapController.class);
    private static final Logger bizLogger = LoggerFactory.getLogger("STAFF_MANAGE_LOGGER");

    @Resource
    private HttpResult httpResult;
    @Autowired
    private PopupService popupService;
    @Autowired
    private AppManageService appManageService;
    @Resource(name="isvGlobal")
    private Map<String, String> isvGlobal;

    /**
     * 获取企业充值信息
     * @param corpId
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/corp/user/popup", method = {RequestMethod.GET})
    public Map<String, Object> fetchCorpChargeInfo(
            @RequestParam("corpId") String corpId,
            @RequestParam("userId") String userId
    ) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("userId", userId)
        ));
        try{
            Long appId = Long.valueOf(isvGlobal.get("appId"));
            ServiceResult<AppVO> appVOSr = appManageService.getAppByAppId(appId);
            String suiteKey = appVOSr.getResult().getSuiteKey();

            PopupInfoVO popupInfo = popupService.getPopupInfo(suiteKey, corpId, userId);
            return httpResult.getSuccess(popupInfo.toMap());
        }catch(Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统错误",
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("userId", userId)
            ),e);
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 获取记录打卡静默期
     * @param corpId
     * @param json
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/corp/user/popup", method = {RequestMethod.POST})
    public Map<String, Object> submitPopup(
            @RequestParam("corpId") String corpId,
            @RequestParam("userId") String userId,
            @RequestBody JSONObject json
    ) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("userId", userId),
                LogFormatter.KeyValue.getNew("json", json)
        ));
        try{
            Long appId = Long.valueOf(isvGlobal.get("appId"));
            ServiceResult<AppVO> appVOSr = appManageService.getAppByAppId(appId);
            String suiteKey = appVOSr.getResult().getSuiteKey();

            String type = json.getString("popupType");
            popupService.logStaffPopup(suiteKey, corpId, userId, type);
            Map<String, Object> map = new HashMap<String, Object>();
            return httpResult.getSuccess(map);
        }catch(Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统错误",
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("userId", userId),
                    LogFormatter.KeyValue.getNew("json", json)
            ),e);
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
}
