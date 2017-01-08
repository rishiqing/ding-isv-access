package com.dingtalk.isv.rsq.biz.httputil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.corp.CorpVO;
import com.dingtalk.isv.access.biz.corp.model.CorpDO;
import com.dingtalk.isv.access.biz.corp.model.DepartmentDO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.suite.model.SuiteDO;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.common.util.HttpRequestHelper;
import com.dingtalk.isv.rsq.biz.model.RsqCorp;
import com.dingtalk.isv.rsq.biz.model.RsqDepartment;
import com.dingtalk.isv.rsq.biz.model.RsqUser;
import com.dingtalk.isv.rsq.biz.model.helper.RsqCorpConverter;
import com.dingtalk.isv.rsq.biz.model.helper.RsqDepartmentConverter;
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

    /**
     * 创建公司
     * @param suiteDO
     * @param corpDO
     * @return
     */
    public ServiceResult<RsqCorp> createCorp(SuiteDO suiteDO, CorpDO corpDO){
        try {
            String url = getRsqDomain() + "/task/v2/tokenAuth/autoCreate/createTeam?token=" + suiteDO.getRsqAppToken();
            Map params = new HashMap<String, String>();
            params.put("appName", suiteDO.getRsqAppName());
            params.put("name", corpDO.getCorpName());
            params.put("outerId", corpDO.getCorpId());
            params.put("note", corpDO.getId());
            String sr = httpRequestHelper.httpPostJson(url, JSON.toJSONString(params));
            JSONObject jsonObject = JSON.parseObject(sr);

            if (jsonObject.containsKey("errcode") && 0 != jsonObject.getLong("errcode")) {
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
            }

            RsqCorp corp = RsqCorpConverter.JSON2RsqCorp(jsonObject);

            return ServiceResult.success(corp);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteDO", suiteDO.toString()),
                    LogFormatter.KeyValue.getNew("corpDO", corpDO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    /**
     * 创建部门
     * @param suiteDO
     * @param corpDO
     * @return
     */
    public ServiceResult<RsqDepartment> createDepartment(SuiteDO suiteDO, CorpDO corpDO, DepartmentDO departmentDO, DepartmentDO parentDepartmentDO){
        try {

            String url = getRsqDomain() + "/task/v2/tokenAuth/autoCreate/createDepartment?token=" + suiteDO.getRsqAppToken();
            Map params = new HashMap<String, String>();
            params.put("name", departmentDO.getName());
            params.put("teamId", corpDO.getRsqId());
            params.put("orderNum", departmentDO.getOrder());

            params.put("outerId", departmentDO.getCorpId() + "--" + departmentDO.getDeptId());

            String parentId = "0";
            if(null != parentDepartmentDO){
                parentId = parentDepartmentDO.getRsqId();
            }
            params.put("parentId", parentId);

            String sr = httpRequestHelper.httpPostJson(url, JSON.toJSONString(params));
            JSONObject jsonObject = JSON.parseObject(sr);

            if (jsonObject.containsKey("errcode") && 0 != jsonObject.getLong("errcode")) {
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
            }

            RsqDepartment dept = RsqDepartmentConverter.JSON2RsqDepartment(jsonObject);

            return ServiceResult.success(dept);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteDO", suiteDO.toString()),
                    LogFormatter.KeyValue.getNew("departmentDO", departmentDO.toString()),
                    LogFormatter.KeyValue.getNew("corpDO", corpDO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 更新部门
     * @param suiteDO
     * @return
     */
    public ServiceResult<RsqDepartment> updateDepartment(SuiteDO suiteDO, DepartmentDO departmentDO, DepartmentDO parentDepartmentDO){
        try {

            String url = getRsqDomain() + "/task/v2/tokenAuth/autoCreate/updateDepartment?token=" + suiteDO.getRsqAppToken();
            Map params = new HashMap<String, String>();
            params.put("id", departmentDO.getRsqId());
            params.put("name", departmentDO.getName());

            String parentId = "0";
            if(null != parentDepartmentDO){
                parentId = parentDepartmentDO.getRsqId();
            }
            params.put("parentId", parentId);

            params.put("orderNum", departmentDO.getOrder());

            String sr = httpRequestHelper.httpPostJson(url, JSON.toJSONString(params));
            JSONObject jsonObject = JSON.parseObject(sr);

            if (jsonObject.containsKey("errcode") && 0 != jsonObject.getLong("errcode")) {
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
            }

            RsqDepartment dept = RsqDepartmentConverter.JSON2RsqDepartment(jsonObject);

            return ServiceResult.success(dept);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteDO", suiteDO.toString()),
                    LogFormatter.KeyValue.getNew("departmentDO", departmentDO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    /**
     * 删除部门
     * @param suiteDO
     * @return
     */
    public ServiceResult<RsqDepartment> deleteDepartment(SuiteDO suiteDO, DepartmentDO departmentDO){
        try {

            String url = getRsqDomain() + "/task/v2/tokenAuth/autoCreate/deleteDepartment?token=" + suiteDO.getRsqAppToken();
            Map params = new HashMap<String, String>();
            params.put("id", departmentDO.getRsqId());

            String sr = httpRequestHelper.httpPostJson(url, JSON.toJSONString(params));
            JSONObject jsonObject = JSON.parseObject(sr);

            if (jsonObject.containsKey("errcode") && 0 != jsonObject.getLong("errcode")) {
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
            }

            RsqDepartment dept = RsqDepartmentConverter.JSON2RsqDepartment(jsonObject);

            return ServiceResult.success(dept);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteDO", suiteDO.toString()),
                    LogFormatter.KeyValue.getNew("departmentDO", departmentDO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    /**
     * 创建新用户
     * @param suiteDO
     * @param staffDO
     * @param corpDO
     * @param others 需要在others的map中包括一个"rsqDepartment"的key值，表示日事清应用中用户所属的部门id的列表
     * @return
     */
    public ServiceResult<RsqUser> createUser(SuiteDO suiteDO, StaffDO staffDO, CorpDO corpDO, Map others){
        try {
            String url = getRsqDomain() + "/task/v2/tokenAuth/autoCreate/createUser?token=" + suiteDO.getRsqAppToken();

            JSONObject params = new JSONObject();
            params.put("appName", suiteDO.getRsqAppName());
            params.put("username", staffDO.getRsqUsername());
            params.put("password", staffDO.getRsqPassword());
            params.put("realName", staffDO.getName());
            params.put("outerId", corpDO.getCorpId() + "--" + staffDO.getUserId());
            params.put("teamId", corpDO.getRsqId());
            params.put("department", others.get("rsqDepartment"));

            String sr = httpRequestHelper.httpPostJson(url, JSON.toJSONString(params));
            JSONObject jsonObject = JSON.parseObject(sr);

            if (jsonObject.containsKey("errcode") && 0 != jsonObject.getLong("errcode")) {
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
            }

            RsqUser user = RsqUserConverter.JSON2RsqUser(jsonObject);

            return ServiceResult.success(user);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteDO", suiteDO.toString()),
                    LogFormatter.KeyValue.getNew("staffDO", staffDO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    /**
     * 更新用户
     * @param suiteDO
     * @param staffDO
     * @param others 如果用户的部门信息有更新，需要在others的map中包括一个"rsqDepartment"的key值
     * @return
     */
    public ServiceResult<RsqUser> updateUser(SuiteDO suiteDO, StaffDO staffDO, Map others){
        try {
            String url = getRsqDomain() + "/task/v2/tokenAuth/autoCreate/updateUser?token=" + suiteDO.getRsqAppToken();
            Map params = new HashMap<String, String>();
            params.put("id", staffDO.getRsqUserId());
            params.put("realName", staffDO.getName());

            if(others.containsKey("rsqDepartment")){
                params.put("department", others.get("rsqDepartment"));
            }

            String sr = httpRequestHelper.httpPostJson(url, JSON.toJSONString(params));
            JSONObject jsonObject = JSON.parseObject(sr);

            if (jsonObject.containsKey("errcode") && 0 != jsonObject.getLong("errcode")) {
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
            }

            RsqUser user = RsqUserConverter.JSON2RsqUser(jsonObject);

            return ServiceResult.success(user);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteDO", suiteDO.toString()),
                    LogFormatter.KeyValue.getNew("staffDO", staffDO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    /**
     * 标记用户为管理员
     * @param suiteDO
     * @param staffDO
     * @return
     */
    public ServiceResult<RsqUser> setUserAdmin(SuiteDO suiteDO, StaffDO staffDO){
        try {
            String url = getRsqDomain() + "/task/v2/tokenAuth/autoCreate/userSetAdmin?token=" + suiteDO.getRsqAppToken();
            Map params = new HashMap<String, String>();
            params.put("id", staffDO.getRsqUserId());
            params.put("isAdmin", staffDO.getAdmin());

            String sr = httpRequestHelper.httpPostJson(url, JSON.toJSONString(params));
            JSONObject jsonObject = JSON.parseObject(sr);

            if (jsonObject.containsKey("errcode") && 0 != jsonObject.getLong("errcode")) {
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
            }

            RsqUser user = RsqUserConverter.JSON2RsqUser(jsonObject);

            return ServiceResult.success(user);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteDO", suiteDO.toString()),
                    LogFormatter.KeyValue.getNew("staffDO", staffDO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    /**
     * 标记用户为管理员
     * @param suiteDO
     * @param staffDO
     * @return
     */
    public ServiceResult<Void> removeUser(SuiteDO suiteDO, StaffDO staffDO){
        try {
            String url = getRsqDomain() + "/task/v2/tokenAuth/autoCreate/userLeaveTeam?token=" + suiteDO.getRsqAppToken();
            Map params = new HashMap<String, String>();
            params.put("id", staffDO.getRsqUserId());

            String sr = httpRequestHelper.httpPostJson(url, JSON.toJSONString(params));
            JSONObject jsonObject = JSON.parseObject(sr);

            if (jsonObject.containsKey("errcode") && 0 != jsonObject.getLong("errcode")) {
                return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
            }

            return ServiceResult.success(null);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    LogFormatter.KeyValue.getNew("suiteDO", suiteDO.toString()),
                    LogFormatter.KeyValue.getNew("staffDO", staffDO.toString())
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }
}
