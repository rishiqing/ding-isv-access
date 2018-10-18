package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.StaffPopupLogDO;
import org.springframework.stereotype.Repository;

@Repository("staffPopupLogDao")
public interface StaffPopupLogDao {

	/**
	 * 保存staffPopupLogDO
	 * @param staffPopupLogDO
	 */
	public void saveOrUpdateStaffPopupLog(StaffPopupLogDO staffPopupLogDO);
}

