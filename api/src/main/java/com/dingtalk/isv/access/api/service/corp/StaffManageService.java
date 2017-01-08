package com.dingtalk.isv.access.api.service.corp;
import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.CorpUserDetailList;

import java.util.List;

/**
 * 员工管理
 * Created by 浩倡 on 16-1-17.
 */
public interface StaffManageService {

    /**
     * 查询一个员工 staffId和corpId唯一确定个一个员工
     * @param staffId
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<StaffVO> getStaff(String staffId, String corpId, String suiteKey);

    /**
     * 查询一个部门的所有员工详情
     * @param deptId
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<CorpUserDetailList> getStaffByDepartment(Long deptId, String corpId, String suiteKey, Long offset, Integer size, String order);

    /**
     * 获取当前登录用户
     *
     * @param suitKey
     * @param corpId
     * @param code
     * @return
     */
    ServiceResult<LoginUserVO> getStaffByAuthCode(String suitKey, String corpId, String code);

    /**
     * 保存或者更新公司成员信息到第三方授权服务器
     * @param staffVO
     * @return
     */
    public ServiceResult<Void> saveOrUpdateCorpStaff(StaffVO staffVO);

    /**
     * 根据corpId和userId从第三方授权服务器本地获取成员
     * @param corpId
     * @param userId
     * @return
     */
    public ServiceResult<StaffVO> getStaffByCorpIdAndUserId(String corpId, String userId);

    /**
     * 获取部门成员，并且保持部门成员到本地
     * @param deptId
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> getAndSaveStaffByDepartment(Long deptId, String corpId, String suiteKey);

    /**
     * 获取并将钉钉企业的组织结构信息保存到本地
     * @param suiteKey
     * @param corpId
     * @return
     */
    public ServiceResult<Void> getAndSaveAllCorpOrgStaff(String suiteKey, String corpId);

    /**
     * 从钉钉服务器获取staff信息并保持或更新到本地
     * @param staffId
     * @param corpId
     * @param suiteKey
     * @return
     */
    public ServiceResult<Void> getAndSaveStaff(String staffId, String corpId, String suiteKey);

    /**
     * 根据corpId和userId删除用户
     * @param corpId
     * @param userId
     * @return
     */
    public ServiceResult<Void> deleteStaffByCorpIdAndUserId(String corpId, String userId);

    /**
     * 根据corpId获取一个公司的staff列表
     * @param corpId
     * @return
     */
    public ServiceResult<List<StaffVO>> getStaffListByCorpId(String corpId);

    }
