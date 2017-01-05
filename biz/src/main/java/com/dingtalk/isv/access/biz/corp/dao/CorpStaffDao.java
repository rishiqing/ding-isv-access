package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("corpStaffDao")
public interface CorpStaffDao {


	/**
	 * 保持公司用户信息
	 * @param staffDO
	 */
	public void saveOrUpdateCorpStaff(StaffDO staffDO);

	/**
	 * 根据corpId和userId查询用户
	 * @param corpId
	 * @param userId
	 * @return
	 */
	public StaffDO getStaffByCorpIdAndUserId(@Param("corpId") String corpId, @Param("userId") String userId);


	/**
	 * 根据corpId和userId删除用户
	 * @param corpId
	 * @param userId
	 * @return
	 */
	public void deleteStaffByCorpIdAndUserId(@Param("corpId") String corpId, @Param("userId") String userId);
}

