package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.CorpChargeStatusDO;
import org.springframework.stereotype.Repository;

@Repository("corpChargeStatusDao")
public interface CorpChargeStatusDao {

	/**
	 * 保存corpChargeStatusDO
	 * @param corpChargeStatusDO
	 */
	public void saveOrUpdateCorpChargeStatus(CorpChargeStatusDO corpChargeStatusDO);
}

