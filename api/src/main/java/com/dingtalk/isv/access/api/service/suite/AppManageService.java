package com.dingtalk.isv.access.api.service.suite;

import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.common.model.ServiceResult;

/**
 * 微应用相关的方法，此处的微应用指由isv创建的微应用，不与corp关联
 * Created by Wallace on 2016/11/18.
 */
public interface AppManageService {

    public ServiceResult<AppVO> getAppByAppId(Long appId);
}
