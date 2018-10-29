package com.dingtalk.isv.access.bizex.dingpush.dao;

import com.dingtalk.isv.access.bizex.dingpush.model.OpenGlobalLock;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("openGlobalLockDao")
public interface OpenGlobalLockDao {

	public void saveOrUpdateOpenGlobalLock(OpenGlobalLock lock);

	public OpenGlobalLock getOpenGlobalLockByLockKey(@Param("lockKey") String lockKey);

	public void updateStatus(OpenGlobalLock lock);

}

