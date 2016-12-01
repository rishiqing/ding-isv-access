package com.dingtalk.isv.access.biz.corp.model.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.StaffResult;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;

import java.util.List;
import java.util.Map;

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

        staffDO.setOrderInDepts(JSON.toJSONString(staffVO.getOrderInDepts()));
        staffDO.setAdmin(staffVO.getIsAdmin());
        staffDO.setBoss(staffVO.getIsBoss());
        staffDO.setDingId(staffVO.getDingId());
        staffDO.setIsLeaderInDepts(JSON.toJSONString(staffVO.getIsLeaderInDepts()));
        staffDO.setHide(staffVO.getIsHide());
        staffDO.setDepartment(JSON.toJSONString(staffVO.getDepartment()));
        staffDO.setPosition(staffVO.getPosition());
        staffDO.setAvatar(staffVO.getAvatar());
        staffDO.setJobnumber(staffVO.getJobnumber());
        staffDO.setExtattr(JSON.toJSONString(staffVO.getExtattr()));
        staffDO.setSys(staffVO.getSys());
        staffDO.setSysLevel(staffVO.getSysLevel());

        staffDO.setRsqUserId(staffVO.getRsqUserId());
        staffDO.setRsqUsername(staffVO.getRsqUsername());
        staffDO.setRsqPassword(staffVO.getRsqPassword());
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
        staffVO.setOrderInDepts((Map<Long, Long>)JSON.parse(staffDO.getOrderInDepts()));
        staffVO.setIsAdmin(staffDO.getAdmin());
        staffVO.setIsBoss(staffDO.getBoss());
        staffVO.setDingId(staffDO.getDingId());
        staffVO.setIsLeaderInDepts((Map<Long, Boolean>)JSON.parse(staffDO.getIsLeaderInDepts()));
        staffVO.setIsHide(staffDO.getHide());
        staffVO.setDepartment((List<Long>)JSON.parse(staffDO.getDepartment()));
        staffVO.setPosition(staffDO.getPosition());
        staffVO.setAvatar(staffDO.getAvatar());
        staffVO.setJobnumber(staffDO.getJobnumber());
        staffVO.setExtattr((Map<String, String>)JSON.parse(staffDO.getExtattr()));
        staffVO.setSys(staffDO.getSys());
        staffVO.setSysLevel(staffDO.getSysLevel());

        staffVO.setRsqUserId(staffDO.getRsqUserId());
        staffVO.setRsqUsername(staffDO.getRsqUsername());
        staffVO.setRsqPassword(staffDO.getRsqPassword());
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
        staffResult.setRsqUsername(staffVO.getRsqUsername());
        staffResult.setRsqPassword(staffVO.getRsqPassword());
        return staffResult;
    }
}
