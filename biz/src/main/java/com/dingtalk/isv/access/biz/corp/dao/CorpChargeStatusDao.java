package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.CorpChargeStatusDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("corpChargeStatusDao")
public interface CorpChargeStatusDao {

	/**
	 * 保存corpChargeStatusDO
	 * @param corpChargeStatusDO
	 */
	public void saveOrUpdateCorpChargeStatus(CorpChargeStatusDO corpChargeStatusDO);

	public CorpChargeStatusDO getCorpChargeStatusBySuiteKeyAndCorpId(@Param("suiteKey") String suiteKey, @Param("corpId") String corpId);
}

