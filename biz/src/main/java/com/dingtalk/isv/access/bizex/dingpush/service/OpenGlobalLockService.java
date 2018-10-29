package com.dingtalk.isv.access.bizex.dingpush.service;

import com.dingtalk.isv.access.bizex.dingpush.model.OpenGlobalLock;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Wallace Mao
 * Date: 2018-10-29 0:12
 */
public interface OpenGlobalLockService {
    OpenGlobalLock requireOpenGlobalLock(String lockKey);

    void releaseOpenGlobalLock(String lockKey);
}
