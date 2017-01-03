package com.dingtalk.isv.access.biz.corp.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.corp.CorpTokenVO;
import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;

import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpDepartmentDao;
import com.dingtalk.isv.access.biz.corp.model.DepartmentDO;
import com.dingtalk.isv.access.biz.corp.model.helper.DepartmentConverter;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.corp.Department;
import com.dingtalk.open.client.api.model.corp.DepartmentDetail;
import com.dingtalk.open.client.api.service.corp.CorpDepartmentService;
import com.dingtalk.open.client.common.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wallace on 2016/1/26.
 */
public class DeptManageServiceImpl implements DeptManageService {
    private static final Logger bizLogger = LoggerFactory.getLogger("CORP_MANAGE_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(CorpManageServiceImpl.class);
    @Autowired
    private CorpDepartmentDao corpDepartmentDao;
    @Autowired
    private CorpDepartmentService corpDepartmentService;
    @Autowired
    private CorpManageService corpManageService;

    @Override
    public ServiceResult<List<DepartmentVO>> getDeptList(Long parentId, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            ServiceResult<CorpTokenVO> corpTokenSr = corpManageService.getCorpToken(suiteKey, corpId);
            List<Department> list = corpDepartmentService.getDeptList(corpTokenSr.getResult().getCorpToken(), parentId.toString());
            List<DepartmentVO> voList = DepartmentConverter.DepartmentList2DepartmentVOList(corpId, list);
            return ServiceResult.success(voList);
        } catch (ServiceException e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "查询部门失败",
                    String.valueOf(e.getCode()),
                    e.getMessage(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<DepartmentVO> getDept(Long deptId, String corpId, String suiteKey) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("deptId", deptId),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            ServiceResult<CorpTokenVO> corpTokenSr = corpManageService.getCorpToken(suiteKey, corpId);
            DepartmentDetail departmentDetail = corpDepartmentService.getDeptDetail(corpTokenSr.getResult().getCorpToken(), deptId.toString());
            DepartmentVO departmentVO = DepartmentConverter.DepartmentDetail2DepartmentVO(corpId, departmentDetail);
            return ServiceResult.success(departmentVO);
        } catch (ServiceException e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "查询部门失败",
                    String.valueOf(e.getCode()),
                    e.getMessage(),
                    LogFormatter.KeyValue.getNew("deptId", deptId),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("deptId", deptId),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("deptId", deptId),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    /**
     * 获取corpId的公司的所有部门并保存
     * @param corpId
     * @param suiteKey
     * @return
     */
//    public ServiceResult<Void> getAndSaveAllCorpDepartment(String corpId, String suiteKey){
//
//    }

    /**
     * 递归调用获取并保存id为parentId的子部门
     * @param parentId
     * @param corpId
     * @param suiteKey
     * @return
     */
    @Override
    public ServiceResult<Void> getAndSaveRecursiveSubDepartment(Long parentId, String corpId, String suiteKey){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("parentId", parentId),
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try{
            ServiceResult<List<DepartmentVO>> sr =  getDeptList(parentId, corpId, suiteKey);
            if(!sr.isSuccess()){
                return ServiceResult.failure(sr.getCode(), sr.getMessage());
            }
            List<DepartmentVO> deptList = sr.getResult();
            if(0 == deptList.size()){
                return ServiceResult.success(null);
            }

            Iterator<DepartmentVO> it = deptList.iterator();
            while(it.hasNext()){
                DepartmentVO dept = it.next();
                ServiceResult<Void> saveSr = saveOrUpdateCorpDepartment(dept);
                if(!saveSr.isSuccess()){
                    return ServiceResult.failure(saveSr.getCode(), saveSr.getMessage());
                }
            }

            Iterator<DepartmentVO> it2 = deptList.iterator();
            while(it2.hasNext()){
                DepartmentVO dept = it2.next();
                ServiceResult<Void> subSr = getAndSaveRecursiveSubDepartment(dept.getDeptId(), corpId, suiteKey);
                if(!subSr.isSuccess()){
                    return ServiceResult.failure(subSr.getCode(), subSr.getMessage());
                }
            }

            return ServiceResult.success(null);

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("parentId", parentId),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("parentId", parentId),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @Override
    public ServiceResult<Void> saveOrUpdateCorpDepartment(DepartmentVO departmentVO) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("departmentVO", JSON.toJSONString(departmentVO))
        ));
        try {
            DepartmentDO departmentDO = DepartmentConverter.DepartmentVO2DepartmentDO(departmentVO);
            corpDepartmentDao.saveOrUpdateCorpDepartment(departmentDO);
            return ServiceResult.success(null);
        } catch (Exception e) {
            String jsonStr = JSON.toJSONString(departmentVO);
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("departmentVO", jsonStr)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("departmentVO", jsonStr)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    public ServiceResult<List<DepartmentVO>> getDepartmentListByCorpId(String corpId){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try {
            List<DepartmentDO> list = corpDepartmentDao.getDepartmentListByCorpId(corpId);

            List<DepartmentVO> voList = DepartmentConverter.DepartmentDOList2DepartmentVOList(list);
            return ServiceResult.success(voList);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    @Override
    public ServiceResult<DepartmentVO> getDepartmentByCorpIdAndDeptId(String corpId, String deptId) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("deptId", deptId)
        ));
        try {

            DepartmentDO departmentDO = corpDepartmentDao.getDepartmentByCorpIdAndDeptId(corpId, deptId);
            if (null == departmentDO) {
                return ServiceResult.success(null);
            }
            DepartmentVO departmentVO = DepartmentConverter.DepartmentDO2DepartmentVO(departmentDO);
            return ServiceResult.success(departmentVO);
        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("deptId", deptId)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("deptId", deptId)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());
        }
    }

    /**
     * 获取并将钉钉企业的组织结构信息保存到本地
     * @param suiteKey
     * @param corpId
     * @return
     */
    @Override
    public ServiceResult<Void> getAndSaveAllCorpOrg(String suiteKey, String corpId){
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("corpId", corpId),
                LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
        ));
        try {
            //  先获取根部门
            ServiceResult<DepartmentVO> rootSr = getDept(1L, corpId, suiteKey);
            if(!rootSr.isSuccess()){
                return ServiceResult.failure(rootSr.getCode(), rootSr.getMessage());
            }
            DepartmentVO root = rootSr.getResult();
            System.out.println("result list:" + JSON.toJSON(root));
            ServiceResult<Void> rootSaveSr = saveOrUpdateCorpDepartment(root);
            if(!rootSaveSr.isSuccess()){
                return ServiceResult.failure(rootSaveSr.getCode(), rootSaveSr.getMessage());
            }

            //  根据根部门递归获取子部门
            ServiceResult<Void> sr = getAndSaveRecursiveSubDepartment(root.getDeptId(), corpId, suiteKey);

            if(!sr.isSuccess()){
                return ServiceResult.failure(sr.getCode(), sr.getMessage());
            }

            return ServiceResult.success(null);
        } catch (Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            mainLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("corpId", corpId),
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey)
            ), e);
            return ServiceResult.failure(ServiceResultCode.SYS_ERROR.getErrCode(), ServiceResultCode.SYS_ERROR.getErrCode());

        }
    }
}
