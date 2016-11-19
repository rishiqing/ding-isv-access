package com.dingtalk.isv.access.biz.corp.model.helper;

import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.StaffResult;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;

/**
 * Created by mint on 16-1-22.
 */
public class StaffConverter {


    public static StaffVO corpUser2StaffVO(CorpUserDetail corpUserDetail, String corpId){
        if(null==corpUserDetail){
            return null;
        }
        StaffVO staffVO = new StaffVO();
        staffVO.setCorpId(corpId);
        staffVO.setStaffId(corpUserDetail.getUserid());
        staffVO.setName(corpUserDetail.getName());
        staffVO.setTel(corpUserDetail.getTel());
        staffVO.setWorkPlace(corpUserDetail.getWorkPlace());
        staffVO.setRemark(corpUserDetail.getRemark());
        staffVO.setMobile(corpUserDetail.getMobile());
        staffVO.setEmail(corpUserDetail.getEmail());
        staffVO.setActive(corpUserDetail.getActive());
        //staffVO.setOrderInDepts(corpUser.getOrderInDepts());
        staffVO.setIsAdmin(corpUserDetail.getIsAdmin());
        staffVO.setIsBoss(corpUserDetail.getIsBoss());
        staffVO.setDingId(corpUserDetail.getDingId());
        //staffVO.setIsLeaderInDepts(corpUser.getIsLeaderInDepts());
        staffVO.setIsHide(corpUserDetail.getIsHide());
        staffVO.setDepartment(corpUserDetail.getDepartment());
        staffVO.setPosition(corpUserDetail.getPosition());
        staffVO.setAvatar(corpUserDetail.getAvatar());
        staffVO.setJobnumber(corpUserDetail.getJobnumber());
        staffVO.setExtattr(corpUserDetail.getExtattr());
        return staffVO;
    }

    public static StaffDO staffVO2StaffDO(StaffVO staffVO){
        if(null == staffVO){
            return null;
        }
        StaffDO staffDO = new StaffDO();
        staffDO.setGmtCreate(staffVO.getGmtCreate());
        staffDO.setGmtModified(staffVO.getGmtModified());
        staffDO.setCorpId(staffVO.getCorpId());
        staffDO.setUserId(staffVO.getStaffId());
        staffDO.setName(staffVO.getName());
        staffDO.setTel(staffVO.getTel());
        staffDO.setWorkPlace(staffVO.getWorkPlace());
        staffDO.setRemark(staffVO.getRemark());
        staffDO.setMobile(staffVO.getMobile());
        staffDO.setEmail(staffVO.getEmail());
        staffDO.setActive(staffVO.getActive());
        staffDO.setOrderInDepts(staffVO.getOrderInDepts());
        staffDO.setAdmin(staffVO.getIsAdmin());
        staffDO.setBoss(staffVO.getIsBoss());
        staffDO.setDingId(staffVO.getDingId());
        staffDO.setIsLeaderInDepts(staffVO.getIsLeaderInDepts());
        staffDO.setHide(staffVO.getIsHide());
        staffDO.setDepartment(staffVO.getDepartment());
        staffDO.setPosition(staffVO.getPosition());
        staffDO.setAvatar(staffVO.getAvatar());
        staffDO.setJobnumber(staffVO.getJobnumber());
        staffDO.setExtattr(staffVO.getExtattr());
        staffDO.setSys(staffVO.getSys());
        staffDO.setSysLevel(staffVO.getSysLevel());
        return staffDO;
    }
    
    public static StaffVO staffDO2StaffVO(StaffDO staffDO){
        if(null == staffDO){
            return null;
        }
        StaffVO staffVO = new StaffVO();
        staffVO.setGmtCreate(staffDO.getGmtCreate());
        staffVO.setGmtModified(staffDO.getGmtModified());
        staffVO.setCorpId(staffDO.getCorpId());
        staffVO.setStaffId(staffDO.getUserId());
        staffVO.setName(staffDO.getName());
        staffVO.setTel(staffDO.getTel());
        staffVO.setWorkPlace(staffDO.getWorkPlace());
        staffVO.setRemark(staffDO.getRemark());
        staffVO.setMobile(staffDO.getMobile());
        staffVO.setEmail(staffDO.getEmail());
        staffVO.setActive(staffDO.getActive());
        staffVO.setOrderInDepts(staffDO.getOrderInDepts());
        staffVO.setIsAdmin(staffDO.getAdmin());
        staffVO.setIsBoss(staffDO.getBoss());
        staffVO.setDingId(staffDO.getDingId());
        staffVO.setIsLeaderInDepts(staffDO.getIsLeaderInDepts());
        staffVO.setIsHide(staffDO.getHide());
        staffVO.setDepartment(staffDO.getDepartment());
        staffVO.setPosition(staffDO.getPosition());
        staffVO.setAvatar(staffDO.getAvatar());
        staffVO.setJobnumber(staffDO.getJobnumber());
        staffVO.setExtattr(staffDO.getExtattr());
        staffVO.setSys(staffDO.getSys());
        staffVO.setSysLevel(staffDO.getSysLevel());
        return staffVO;
    }

    public static StaffResult staffVO2StaffResult(StaffVO staffVO){
        if(null == staffVO){
            return null;
        }
        StaffResult staffResult = new StaffResult();
        staffResult.setCorpId(staffVO.getCorpId());
        staffResult.setUserId(staffVO.getStaffId());
        staffResult.setName(staffVO.getName());
        staffResult.setOrderInDepts(staffVO.getOrderInDepts());
        staffResult.setAdmin(staffVO.getIsAdmin());
        staffResult.setBoss(staffVO.getIsBoss());
        staffResult.setDingId(staffVO.getDingId());
        staffResult.setIsLeaderInDepts(staffVO.getIsLeaderInDepts());
        staffResult.setHide(staffVO.getIsHide());
        staffResult.setDepartment(staffVO.getDepartment());
        staffResult.setPosition(staffVO.getPosition());
        staffResult.setAvatar(staffVO.getAvatar());
        staffResult.setJobnumber(staffVO.getJobnumber());
        staffResult.setExtattr(staffVO.getExtattr());
        return staffResult;
    }
}
