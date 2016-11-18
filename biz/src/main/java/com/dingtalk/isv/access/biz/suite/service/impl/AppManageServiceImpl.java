package com.dingtalk.isv.access.biz.suite.service.impl;

import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.suite.AppManageService;
import com.dingtalk.isv.access.biz.suite.dao.AppDao;
import com.dingtalk.isv.access.biz.suite.model.AppDO;
import com.dingtalk.isv.access.biz.suite.model.helper.AppConverter;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Wallace on 2016/11/18.
 */
public class AppManageServiceImpl implements AppManageService {
    private static final Logger bizLogger = LoggerFactory.getLogger("SUITE_MANAGE_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(AppManageServiceImpl.class);

    @Autowired
    private AppDao appDao;

    @Override
    public ServiceResult<AppVO> getAppByAppId(Long appId) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("appId", appId)
        ));
        try {
            AppDO appDO = appDao.getAppByAppId(appId);
            if (null == appDO) {
                return ServiceResult.success(null);
            }
            AppVO appVO = AppConverter.appDO2AppVO(appDO);
            return ServiceResult.success(appVO);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("appId", appId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("appId", appId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
}
