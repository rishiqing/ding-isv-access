package com.dingtalk.isv.access.biz.corp.model.helper;

import com.dingtalk.isv.access.api.model.corp.CorpVO;
import com.dingtalk.isv.access.api.model.corp.DepartmentVO;
import com.dingtalk.isv.access.biz.corp.model.CorpDO;
import com.dingtalk.isv.access.biz.corp.model.DepartmentDO;
import com.dingtalk.open.client.api.model.corp.Department;
import com.dingtalk.open.client.api.model.corp.DepartmentDetail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mint on 16-7-17.
 */
public class DepartmentConverter {

    public static DepartmentVO Department2DepartmentVO(String corpId, Department department){
        if(null==department){
            return null;
        }
        DepartmentVO departmentVO = new DepartmentVO();
        departmentVO.setCorpId(corpId);
        departmentVO.setDeptId(department.getId());
        departmentVO.setName(department.getName());
        departmentVO.setParentId(department.getParentid());
        departmentVO.setCreateDeptGroup(department.getCreateDeptGroup());
        departmentVO.setAutoAddUser(department.getAutoAddUser());
        return departmentVO;
    }

    public static DepartmentVO DepartmentDetail2DepartmentVO(String corpId, DepartmentDetail departmentDetail){
        if(null==departmentDetail){
            return null;
        }
        DepartmentVO departmentVO = new DepartmentVO();
        departmentVO.setCorpId(corpId);
        departmentVO.setDeptId(departmentDetail.getId());
        departmentVO.setName(departmentDetail.getName());
        departmentVO.setOrder(departmentDetail.getOrder());
        departmentVO.setParentId(departmentDetail.getParentid());
        departmentVO.setCreateDeptGroup(departmentDetail.getCreateDeptGroup());
        departmentVO.setAutoAddUser(departmentDetail.getAutoAddUser());
        departmentVO.setDeptHiding(departmentDetail.getDeptHiding());
        departmentVO.setDeptPerimits(departmentDetail.getDeptPerimits());
//        departmentVO.setUserPerimits(departmentDetail.getUserPermits());

        departmentVO.setDeptManagerUseridList(departmentDetail.getDeptManagerUseridList());
        departmentVO.setOrgDeptOwner(departmentDetail.getOrgDeptOwner());
        departmentVO.setOrder(departmentDetail.getOrder());
        return departmentVO;
    }

    public static DepartmentVO DepartmentDO2DepartmentVO(DepartmentDO departmentDO){
        if(null==departmentDO){
            return null;
        }
        DepartmentVO departmentVO = new DepartmentVO();
        departmentVO.setId(departmentDO.getId());
        departmentVO.setDeptId(departmentDO.getDeptId());
        departmentVO.setGmtCreate(departmentDO.getGmtCreate());
        departmentVO.setGmtModified(departmentDO.getGmtModified());
        departmentVO.setCorpId(departmentDO.getCorpId());
        departmentVO.setName(departmentDO.getName());
        departmentVO.setOrder(departmentDO.getOrder());
        departmentVO.setParentId(departmentDO.getParentId());
        departmentVO.setCreateDeptGroup(departmentDO.getCreateDeptGroup());
        departmentVO.setAutoAddUser(departmentDO.getAutoAddUser());
        departmentVO.setDeptHiding(departmentDO.getDeptHiding());
        departmentVO.setDeptPerimits(departmentDO.getDeptPerimits());
        departmentVO.setUserPerimits(departmentDO.getUserPerimits());

        departmentVO.setDeptManagerUseridList(departmentDO.getDeptManagerUseridList());
        departmentVO.setOrgDeptOwner(departmentDO.getOrgDeptOwner());
        departmentVO.setOrder(departmentDO.getOrder());

        departmentVO.setRsqId(departmentDO.getRsqId());
        return departmentVO;
    }

    public static DepartmentDO DepartmentVO2DepartmentDO(DepartmentVO departmentVO){
        if(null==departmentVO){
            return null;
        }
        DepartmentDO departmentDO = new DepartmentDO();
        departmentDO.setDeptId(departmentVO.getDeptId());
        departmentDO.setGmtCreate(departmentVO.getGmtCreate());
        departmentDO.setGmtModified(departmentVO.getGmtModified());
        departmentDO.setCorpId(departmentVO.getCorpId());
        departmentDO.setName(departmentVO.getName());
        departmentDO.setOrder(departmentVO.getOrder());
        departmentDO.setParentId(departmentVO.getParentId());
        departmentDO.setCreateDeptGroup(departmentVO.getCreateDeptGroup());
        departmentDO.setAutoAddUser(departmentVO.getAutoAddUser());
        departmentDO.setDeptHiding(departmentVO.getDeptHiding());
        departmentDO.setDeptPerimits(departmentVO.getDeptPerimits());
        departmentDO.setUserPerimits(departmentVO.getUserPerimits());

        departmentDO.setDeptManagerUseridList(departmentVO.getDeptManagerUseridList());
        departmentDO.setOrgDeptOwner(departmentVO.getOrgDeptOwner());
        departmentDO.setOrder(departmentVO.getOrder());

        departmentDO.setRsqId(departmentVO.getRsqId());
        return departmentDO;
    }

    public static List<DepartmentVO> DepartmentList2DepartmentVOList(String corpId, List<Department> departments){

        if(null==departments){
            return null;
        }

        List<DepartmentVO> voList = new ArrayList<DepartmentVO>();

        Iterator<Department> it = departments.iterator();
        while (it.hasNext()){
            Department d = it.next();
            voList.add(DepartmentConverter.Department2DepartmentVO(corpId, d));
        }

        return voList;
    }

    public static List<DepartmentVO> DepartmentDOList2DepartmentVOList(List<DepartmentDO> departments){

        if(null==departments){
            return null;
        }

        List<DepartmentVO> voList = new ArrayList<DepartmentVO>();

        Iterator<DepartmentDO> it = departments.iterator();
        while (it.hasNext()){
            DepartmentDO d = it.next();
            voList.add(DepartmentConverter.DepartmentDO2DepartmentVO(d));
        }

        return voList;
    }

}
