package com.dingtalk.isv.rsq.biz.model.helper;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.rsq.biz.model.RsqCorp;
import com.dingtalk.isv.rsq.biz.model.RsqDepartment;

/**
 * Created by Wallace on 2016/11/29.
 */
public class RsqDepartmentConverter {
    public static RsqDepartment JSON2RsqDepartment(JSONObject json){
        RsqDepartment dept = new RsqDepartment();
        dept.setId(json.getLong("id"));
        dept.setName(json.getString("name"));
        if(json.containsKey("outerId")){
            dept.setOuterId(json.getString("outerId"));
        }
        if(json.containsKey("parentId")){
            dept.setParentId(json.getString("parentId"));
        }
        if(json.containsKey("teamId")){
            dept.setTeamId(json.getString("teamId"));
        }
        if(json.containsKey("orderNum")){
            dept.setOrderNum(json.getLong("orderNum"));
        }
        if(json.containsKey("fromApp")){
            dept.setFromApp(json.getString("fromApp"));
        }
        return dept;
    }
}
