package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.StaffIdsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

	/**
	 * 根据corpId查询用户列表
	 * @param corpId
	 * @return
	 */
	public List<StaffDO> getStaffListByCorpId(@Param("corpId") String corpId);

	/**
	 * 根据corpId查询用户id列表
	 * @param corpId
	 * @return
	 */
	public List<String> getStaffUserIdListByCorpId(@Param("corpId") String corpId);

	/**
	 * 更新第三方应用id的信息
	 * @param staffDO
	 * @return
	 */
	public void updateRsqInfo(StaffDO staffDO);

	/**
	 * 保存用户的删除信息，当用户被删除后，为保存用户与日事清的关联信息，需要将用于移入删除表
	 * @param staffDO
	 */
	public void saveStaffDeleted(StaffDO staffDO);

	/**
	 * 根据staff的rsqId获取到userId
	 * @param rsqIds
	 * @return
	 */
	public List<StaffIdsDO> getUserIdFromRsqId(
			@Param("corpId") String corpId,
			@Param("rsqIds") String[] rsqIds);
	/**
	 * 根据staff的rsqId获取到userId
	 * @param userIds
	 * @return
	 */
	public List<StaffIdsDO> getRsqIdFromUserId(
			@Param("corpId") String corpId,
			@Param("userIds") String[] userIds);

	/**
	 * 更新unionId
	 * @param staffDO
	 * @return
	 */
	public void updateUnionId(StaffDO staffDO);

}

