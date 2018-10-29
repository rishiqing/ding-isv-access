package com.dingtalk.isv.access.bizex.dingpush.service;

import com.dingtalk.isv.access.bizex.dingpush.model.OpenSyncBizData;

/**
 * @author Wallace Mao
 * Date: 2018-10-29 10:23
 */
public interface DbAuthCheckService {
    void checkDingEvent();

    void handleSyncData(OpenSyncBizData data);
}
