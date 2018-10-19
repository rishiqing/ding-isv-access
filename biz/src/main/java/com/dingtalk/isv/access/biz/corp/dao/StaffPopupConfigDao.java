package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.StaffPopupConfigDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("staffPopupConfigDao")
public interface StaffPopupConfigDao {

	/**
	 * 保存staffPopupConfigDO
	 * @param staffPopupConfigDO
	 */
	public void saveOrUpdateStaffPopupConfig(StaffPopupConfigDO staffPopupConfigDO);

	/**
	 * 获取指定suiteKey下的所有popupConfig
	 * @param suiteKey
	 * @return
	 */
	public List<StaffPopupConfigDO> getStaffPopupConfigListBySuiteKeyAndIsDisabled(
			@Param("suiteKey") String suiteKey,
			@Param("isDisabled") Boolean isDisabled
	);
}

