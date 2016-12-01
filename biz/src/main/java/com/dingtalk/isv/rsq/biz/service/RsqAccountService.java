package com.dingtalk.isv.rsq.biz.service;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.model.corp.CorpVO;
import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.model.suite.SuiteVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.api.service.suite.SuiteManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpStaffDao;
import com.dingtalk.isv.access.biz.corp.model.CorpDO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.helper.CorpConverter;
import com.dingtalk.isv.access.biz.corp.model.helper.StaffConverter;
import com.dingtalk.isv.access.biz.suite.dao.SuiteDao;
import com.dingtalk.isv.access.biz.suite.model.SuiteDO;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.exceptions.RsqIntegrationException;
import com.dingtalk.isv.rsq.biz.httputil.RsqAccountRequestHelper;
import com.dingtalk.isv.rsq.biz.model.RsqCorp;
import com.dingtalk.isv.rsq.biz.model.RsqUser;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
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
    private CorpStaffDao corpStaffDao;
    @Autowired
    private RsqAccountRequestHelper rsqAccountRequestHelper;
    @Autowired
    private StaffManageService staffManageService;

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
     * 创建公司员工
     * @param suiteKey
     * @param corpId
     * @param userId
     * @return
     */
    public ServiceResult<StaffVO> createRsqTeamStaff(String suiteKey, String corpId, String userId) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("userId", userId)
        ));

        try {
            //  生成用户信息
            StaffDO staffDO = corpStaffDao.getStaffByCorpIdAndUserId(corpId, userId);
            StaffVO staffVO;
            if(null == staffDO){
                CorpDO corpDO = corpDao.getCorpByCorpId(corpId);
                if(null == corpDO || null == corpDO.getRsqId()){
                    throw new RsqIntegrationException("rsqId not found in corpDO: " + corpDO);
                }
                staffVO = staffManageService.getStaff(userId, corpId, suiteKey).getResult();
                staffDO = StaffConverter.staffVO2StaffDO(staffVO);

                SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);

                //  保存用户信息到日事清系统
                //  TODO  注册到日事清用户的方法
                String username = generateRsqUsername(suiteDO.getRsqAppName());  //TODO 自动生成用户名
                String password = generateRsqPassword();  //TODO 自动生成明文密码
                staffDO.setRsqUsername(username);
                staffDO.setRsqPassword(password);
                ServiceResult<RsqUser> rsqUserSr = rsqAccountRequestHelper.createUser(suiteDO, staffDO, corpDO);

                if(!rsqUserSr.isSuccess()){
                    return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
                }
                RsqUser user = rsqUserSr.getResult();
                staffDO.setRsqUserId(String.valueOf(user.getId()));

                corpStaffDao.saveOrUpdateCorpStaff(staffDO);
                return ServiceResult.success(StaffConverter.staffDO2StaffVO(staffDO));
            }else{
                //  如果系统中已存在，看是否需要更新
                //  暂时不执行操作，后续改成隔一段时间更新用户信息
                staffVO = StaffConverter.staffDO2StaffVO(staffDO);
                return ServiceResult.success(staffVO);
            }

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("userId", userId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常",
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("userId", userId)
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
}
