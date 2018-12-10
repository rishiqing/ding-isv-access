package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.biz.corp.model.DepartmentDO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("corpDepartmentDao")
public interface CorpDepartmentDao {


	/**
	 * 保持公司部门信息
	 * @param departmentDO
	 */
	public void saveOrUpdateCorpDepartment(DepartmentDO departmentDO);

	/**
	 * 根据corpId和deptId查询部门
	 * @param corpId
	 * @param deptId
	 * @return
	 */
	public DepartmentDO getDepartmentByCorpIdAndDeptId(@Param("corpId") String corpId, @Param("deptId") Long deptId);

	/**
	 * 根据corpId查询部门列表
	 * @param corpId
	 * @return
	 */
	public List<DepartmentDO> getDepartmentListByCorpId(@Param("corpId") String corpId);

	/**
	 * 根据corpId查询部门列表
	 * @param parentId
	 * @return
	 */
	public List<DepartmentDO> getDepartmentListByParentId(@Param("corpId") String corpId, @Param("parentId") Long parentId);

	/**
	 * 根据corpId和deptId删除部门
	 * @param corpId
	 * @param deptId
	 * @return
	 */
	public void deleteDepartmentByCorpIdAndDeptId(@Param("corpId") String corpId, @Param("deptId") Long deptId);

	/**
	 * 更新第三方应用id信息
	 * @param departmentDO
	 * @return
	 */
	public void updateRsqInfo(DepartmentDO departmentDO);

	/**
	 * 通过id更新第三方应用id信息
	 * @param departmentDO
	 * @return
	 */
	public void updateRsqInfoById(DepartmentDO departmentDO);

}

