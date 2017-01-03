package com.dingtalk.isv.access.biz.service.corp;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.api.model.corp.LoginUserVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.service.corp.DeptManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.biz.base.BaseTestCase;
import com.dingtalk.isv.common.model.ServiceResult;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mint on 16-1-22.
 */
public class DeptManageServiceTest extends BaseTestCase {
    @Resource
    private DeptManageService deptManageService;

    @Test
    public void test_getDeptList() {
        String suiteKey= "suitee7uslxed43cjwsm7";
        String corpId = "dinga5892228289863f535c2f4657eb6378f";
        Long deptId = 0L;
        ServiceResult<List<DepartmentVO>> sr = deptManageService.getDeptList(deptId, corpId, suiteKey);
        System.out.println("result list:" + JSON.toJSON(sr.getResult()));
    }

    @Test
    public void test_getDept() {
        String suiteKey= "suitee7uslxed43cjwsm7";
        String corpId = "dinga5892228289863f535c2f4657eb6378f";
        Long deptId = 1L;
        ServiceResult<DepartmentVO> sr = deptManageService.getDept(deptId, corpId, suiteKey);
        System.out.println("result department:" + JSON.toJSON(sr.getResult()));
    }

    @Test
    public void test_getAllDeptList() {
        String suiteKey= "suitee7uslxed43cjwsm7";
        String corpId = "dinga5892228289863f535c2f4657eb6378f";

        //  先获取根部门
        ServiceResult<DepartmentVO> rootSr = deptManageService.getDept(1L, corpId, suiteKey);
        DepartmentVO root = rootSr.getResult();
        System.out.println("result list:" + JSON.toJSON(root));
        ServiceResult<Void> rootSaveSr = deptManageService.saveOrUpdateCorpDepartment(root);
        if(!rootSaveSr.isSuccess()){
            System.out.println("save result not success:");
        }else{
            System.out.println("save result success:");
        }

        //  根据根部门递归获取子部门
        ServiceResult<Void> sr = deptManageService.getAndSaveRecursiveSubDepartment(root.getDeptId(), corpId, suiteKey);

        if(!sr.isSuccess()){
            System.out.println("result not success:");
        }else{
            System.out.println("result success:");
        }
    }

}
