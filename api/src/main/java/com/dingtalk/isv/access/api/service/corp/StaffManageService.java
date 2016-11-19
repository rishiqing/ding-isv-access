package com.dingtalk.isv.access.api.service.corp;
import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;

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

}
