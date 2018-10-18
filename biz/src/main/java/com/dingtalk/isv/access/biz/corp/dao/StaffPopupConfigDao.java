package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.StaffPopupConfigDO;
import org.springframework.stereotype.Repository;

@Repository("staffPopupConfigDao")
public interface StaffPopupConfigDao {

	/**
	 * 保存staffPopupConfigDO
	 * @param staffPopupConfigDO
	 */
	public void saveOrUpdateStaffPopupConfig(StaffPopupConfigDO staffPopupConfigDO);
}

