package com.dingtalk.isv.access.api.service.corp;

import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.common.model.ServiceResult;

import java.util.List;

/**
 * Created by lifeng.zlf on 2016/3/16.
 */
public interface DeptManageService {

    /**
     * 获取部门列表
     * @param parentId 父部门的id，null表示获取根部门
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<List<DepartmentVO>> getDeptList(Long parentId, String corpId, String suiteKey);
    /**
     * 获取部门
     * @param deptId
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<DepartmentVO> getDept(Long deptId, String corpId, String suiteKey);

    /**
     * 保存或者更新公司部门信息到本地
     * @param departmentVO
     * @return
     */
    public ServiceResult<Void> saveOrUpdateCorpDepartment(DepartmentVO departmentVO);

    /**
     * 根据corpId和deptId从第三方授权服务器本地获取部门
     * @param corpId
     * @param deptId
     * @return
     */
    public ServiceResult<DepartmentVO> getDepartmentByCorpIdAndDeptId(String corpId, Long deptId);

    /**
     * 根据corpId从本地数据库获取所有部门
     * @param corpId
     * @return
     */
    public ServiceResult<List<DepartmentVO>> getDepartmentListByCorpId(String corpId);

    /**
     * 根据parentId从本地数据库获取所有部门
     * @param parentId
     * @return
     */
    public ServiceResult<List<DepartmentVO>> getDepartmentListByCorpIdAndParentId(String corpId, Long parentId);
    /**
     * 递归调用获取并保存id为parentId的子部门
     * @param parentId
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> getAndSaveRecursiveSubDepartment(Long parentId, String corpId, String suiteKey);

    /**
     * 获取并将钉钉企业的组织结构信息保存到本地
     * @param suiteKey
     * @param corpId
     * @return
     */
    public ServiceResult<Void> getAndSaveAllCorpOrg(String suiteKey, String corpId);

    /**
     * 从钉钉服务器获取department信息并保持或更新到本地
     * @param deptId
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> getAndSaveDepartment(Long deptId, String corpId, String suiteKey);

    /**
     * 根据corpId和deptId删除部门
     * @param corpId
     * @param deptId
     * @return
     */
    public ServiceResult<Void> deleteDepartmentByCorpIdAndUserId(String corpId, Long deptId);
}
