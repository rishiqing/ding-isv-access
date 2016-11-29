package com.dingtalk.isv.rsq.biz.httputil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.biz.corp.model.CorpDO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.suite.model.SuiteDO;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.common.util.HttpRequestHelper;
import com.dingtalk.isv.rsq.biz.model.RsqCorp;
import com.dingtalk.isv.rsq.biz.model.RsqUser;
import com.dingtalk.isv.rsq.biz.model.helper.RsqCorpConverter;
import com.dingtalk.isv.rsq.biz.model.helper.RsqUserConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与日事清服务器发送公司和账号创建请求的helper
 * Created by Wallace on 2016/11/19.
 */
public class RsqAccountRequestHelper {
    private static Logger logger = LoggerFactory.getLogger(RsqAccountRequestHelper.class);
    private static final Logger bizLogger = LoggerFactory.getLogger("HTTP_INVOKE_LOGGER");
    private HttpRequestHelper httpRequestHelper;
    private String rsqDomain;

    public HttpRequestHelper getHttpRequestHelper() {
        return httpRequestHelper;
    }

    public void setHttpRequestHelper(HttpRequestHelper httpRequestHelper) {
        this.httpRequestHelper = httpRequestHelper;
    }

    public String getRsqDomain() {
        return rsqDomain;
    }

    public void setRsqDomain(String rsqDomain) {
        this.rsqDomain = rsqDomain;
    }

    public ServiceResult<RsqCorp> createCorp(SuiteDO suiteDO, CorpDO corpDO){
        try {
            String url = getRsqDomain() + "/v2/tokenAuth/autoCreate/saveTeam?token=" + suiteDO.getRsqAppToken();
            Map params = new HashMap<String, String>();
            params.put("appName", suiteDO.getRsqAppName());
            params.put("name", corpDO.getCorpName());
            params.put("outerId", corpDO.getId());
            params.put("note", corpDO.getCorpId());
            String sr = httpRequestHelper.httpPostJson(url, JSON.toJSONString(params));
            JSONObject jsonObject = JSON.parseObject(sr);
            Long errCode = jsonObject.getLong("errcode");
            RsqCorp corp;

            if (Long.valueOf(0).equals(errCode)) {
                JSONObject data = jsonObject.getJSONObject("data");

                corp = RsqCorpConverter.JSON2RsqCorp(data);

                return ServiceResult.success(corp);
            }
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        } catch (Throwable e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteDO", suiteDO.toString()),
                    LogFormatter.KeyValue.getNew("corpDO", corpDO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    public ServiceResult<RsqUser> createUser(SuiteDO suiteDO, StaffDO staffDO){
        try {
            String url = getRsqDomain() + "/v2/tokenAuth/autoCreate/saveAccount?token=" + suiteDO.getRsqAppToken();
            Map params = new HashMap<String, String>();
            params.put("appName", suiteDO.getRsqAppName());
            params.put("username", staffDO.getRsqUsername());
            params.put("password", staffDO.getRsqPassword());
            params.put("realName", staffDO.getName());
            params.put("outerId", staffDO.getUserId());
            String sr = httpRequestHelper.httpPostJson(url, JSON.toJSONString(params));
            JSONObject jsonObject = JSON.parseObject(sr);
            Long errCode = jsonObject.getLong("errcode");
            RsqUser user;

            if (Long.valueOf(0).equals(errCode)) {
                JSONObject data = jsonObject.getJSONObject("data");

                user = RsqUserConverter.JSON2RsqUser(data);

                return ServiceResult.success(user);
            }
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        } catch (Throwable e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteDO", suiteDO.toString()),
                    LogFormatter.KeyValue.getNew("staffDO", staffDO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }
}
