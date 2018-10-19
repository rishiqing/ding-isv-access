package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.StaffPopupLogDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("staffPopupLogDao")
public interface StaffPopupLogDao {

	/**
	 * 保存staffPopupLogDO
	 * @param staffPopupLogDO
	 */
	public void saveOrUpdateStaffPopupLog(StaffPopupLogDO staffPopupLogDO);

	public StaffPopupLogDO getStaffPopupLogListBySuiteKeyAndCorpIdAndUserId(
			@Param("suiteKey") String suiteKey,
			@Param("corpId") String corpId,
			@Param("userId") String userId
	);
}

