package com.dingtalk.isv.access.bizex.dingpush.service.impl;

import com.dingtalk.isv.access.bizex.dingpush.dao.OpenGlobalLockDao;
import com.dingtalk.isv.access.bizex.dingpush.model.OpenGlobalLock;
import com.dingtalk.isv.access.bizex.dingpush.service.OpenGlobalLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author Wallace Mao
 * Date: 2018-10-29 0:13
 */
public class OpenGlobalLockServiceImpl implements OpenGlobalLockService {
    private static final String LOCK_STATUS_OPEN= "open";
    private static final String LOCK_STATUS_LOCKED= "locked";
    @Autowired
    private OpenGlobalLockDao openGlobalLockDao;

    /**
     * 请求锁，步骤如下：
     * 1  开启事务
     * 2  使用select ... for update，获取指定lockKey的记录
     * 3  如果记录不存在，那么插入初始化记录，并设置默认的status为“open”，设置lock为新插入的lock
     * 4  如果记录存在，那么设置lock为查询到的lock
     * 至此，成功设置lock为lock记录，下面开始判断lock记录的状态
     * 5  如果lock的status不为"open"，那么说明已经被其他线程锁住直接返回null，
     * 6  如果lock的status为"open"，那么首先更新lock，将status设置为“locked”，返回lock
     * 结束事务
     * @param lockKey
     * @return
     */
    @Override
    @Transactional(value = "transactionManagerEx")
    public OpenGlobalLock requireOpenGlobalLock(String lockKey){
        OpenGlobalLock lock = openGlobalLockDao.getOpenGlobalLockByLockKey(lockKey);
        if(lock == null){
            lock = saveDefaultLock(lockKey);
        }

        if(!LOCK_STATUS_OPEN.equals(lock.getStatus())){
            return null;
        }
        lock.setStatus(LOCK_STATUS_LOCKED);
        openGlobalLockDao.updateStatus(lock);
        return lock;
    }

    @Override
    @Transactional(value = "transactionManagerEx")
    public void releaseOpenGlobalLock(String lockKey){
        OpenGlobalLock lock = openGlobalLockDao.getOpenGlobalLockByLockKey(lockKey);
        if(lock == null){
            return;
        }
        lock.setStatus(LOCK_STATUS_OPEN);
        openGlobalLockDao.updateStatus(lock);
    }

    private OpenGlobalLock saveDefaultLock(String lockKey){
        OpenGlobalLock lock = new OpenGlobalLock();
        lock.setLockKey(lockKey);
        lock.setStatus(LOCK_STATUS_OPEN);
        openGlobalLockDao.saveOrUpdateOpenGlobalLock(lock);
        return lock;
    }
}
