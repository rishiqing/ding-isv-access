package com.dingtalk.isv.access.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.dingtalk.isv.access.biz.corp.dao.CorpStaffDao;
import com.dingtalk.isv.access.biz.corp.model.StaffIdsDO;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wallace on 2017/1/13.
 */
@Controller
public class IdMapController {
    private static final Logger mainLogger = LoggerFactory.getLogger(IdMapController.class);
    private static final Logger bizLogger = LoggerFactory.getLogger("CONTROLLER_ISV_ID_MAP_LOGGER");

    @Autowired
    private CorpStaffDao corpStaffDao;
    @Resource
    private HttpResult httpResult;

    @ResponseBody
    @RequestMapping(value = "/idmap/userid2rsqid", method = {RequestMethod.POST})
    public Map<String, Object> userId2RsqId(
            @RequestParam("corpid") String corpId,
            @RequestBody JSONArray json
    ) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("json", json),
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try{
            List<StaffIdsDO> list = corpStaffDao.getRsqIdFromUserId(corpId, json.toArray(new String[]{}));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("result", list);

            return httpResult.getSuccess(map);
        }catch(Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统错误",
                    LogFormatter.KeyValue.getNew("json", json),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ),e);
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/idmap/rsqid2userid", method = {RequestMethod.POST})
    public Map<String, Object> rsqId2UserId(
            @RequestParam("corpid") String corpId,
            @RequestBody JSONArray json
    ) {
        bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
                LogFormatter.KeyValue.getNew("json", json),
                LogFormatter.KeyValue.getNew("corpId", corpId)
        ));
        try{
            Object[] objArray = json.toArray();
            String[] idArray = new String[objArray.length];
            for(int i=0;i<objArray.length;i++){
                idArray[i] = String.valueOf(objArray[i]);
            }
            List<StaffIdsDO> list = corpStaffDao.getUserIdFromRsqId(corpId, idArray);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("result", list);

            return httpResult.getSuccess(map);
        }catch(Exception e){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统错误",
                    LogFormatter.KeyValue.getNew("json", json),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ),e);
            return httpResult.getFailure(ServiceResultCode.SYS_ERROR.getErrCode(),ServiceResultCode.SYS_ERROR.getErrMsg());
        }
    }
}
