package com.dingtalk.isv.rsq.biz.model.helper;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.rsq.biz.model.RsqCorp;

/**
 * Created by Wallace on 2016/11/29.
 */
public class RsqCorpConverter {
    public static RsqCorp JSON2RsqCorp(JSONObject json){
        RsqCorp corp = new RsqCorp();
        corp.setId(json.getLong("id"));
        corp.setName(json.getString("name"));
        if(json.containsKey("outerId")){
            corp.setOuterId(json.getString("outerId"));
        }
        if(json.containsKey("note")){
            corp.setNote(json.getString("note"));
        }
        if(json.containsKey("fromApp")){
            corp.setFromApp(json.getString("fromApp"));
        }
        return corp;
    }
}
