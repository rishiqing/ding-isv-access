package com.dingtalk.isv.access.biz.corp.service;

import com.dingtalk.isv.access.biz.corp.enumtype.CorpLockType;
import com.dingtalk.isv.access.biz.corp.model.CorpLockDO;
import com.dingtalk.isv.common.model.ServiceResult;

/**
 * 公司相关的并发锁控制的service
 * Created by Wallace on 2016/12/6.
 */
public interface CorpLockService {

    /**
     * 请求锁
     * @return
     */
    public CorpLockDO requireLock(String corpId, CorpLockType type);

    /**
     * 释放锁
     * @return
     */
    public CorpLockDO releaseLock(String corpId, CorpLockType type);
}
