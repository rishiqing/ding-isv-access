package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.CorpLockDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("corpLockDao")
public interface CorpLockDao {


	/**
	 * 创建或更新一个lock
	 * @param corpLockDO
	 */
	public void saveOrUpdateCorpLock(CorpLockDO corpLockDO);

	/**
	 * 根据lock_key查询lock
	 * @param lockKey
	 * @return
	 */
	public CorpLockDO getCorpByLockKey(@Param("lockKey") String lockKey);
}

