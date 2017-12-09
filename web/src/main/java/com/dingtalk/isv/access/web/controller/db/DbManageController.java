package com.dingtalk.isv.access.web.controller.db;

import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpDao;
import com.dingtalk.isv.access.biz.corp.model.CorpDO;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.service.RsqAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user 毛文强
 * Date: 2017/12/9
 * Time: 0:51
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class DbManageController {
    private static final Logger bizLogger = LoggerFactory.getLogger("STAFF_MANAGE_LOGGER_APPENDER");

    @Autowired
    StaffManageService staffManageService;
    @Autowired
    RsqAccountService rsqAccountService;
    @Autowired
    CorpDao corpDao;

    /**
     * 从钉钉根据corpId获取corp的admin信息,并更新到本地
     * @param corpId
     * @return
     */
    @RequestMapping("/corp/corpAdmin/fetch")
    @ResponseBody
    public String fetchCorpAdmin(
            @RequestParam(value = "corpId") String corpId,
            @RequestParam(value = "suiteKey") String suiteKey
    ) {
        ServiceResult<Void> sr = staffManageService.getAndSaveCorpAdminList(corpId, suiteKey);
        if(!sr.isSuccess()){
            return "<<failed>>";
        }
        return "<<success>>" + new Date();
    }

    /**
     * 将指定corpId的admin信息同步更新到日事清
     * @param corpId
     * @return
     */
    @RequestMapping("/corp/corpAdmin/rsqSync")
    @ResponseBody
    public String syncRsqCorpAdmin(
            @RequestParam(value = "corpId") String corpId,
            @RequestParam(value = "suiteKey") String suiteKey
    ) {
        ServiceResult<Void> sr = rsqAccountService.updateAllCorpAdmin(suiteKey, corpId);
        if(!sr.isSuccess()){
            return "<<failed>>";
        }
        return "<<success>>" + new Date();
    }

    /**
     * 将系统中所有的corp的admin信息都获取到本地,然后同步到日事清
     * @return
     */
    @RequestMapping("/corp/corpAdmin/syncAll")
    @ResponseBody
    public String syncAllCorpAdmin(
            @RequestParam(value = "suiteKey") String suiteKey,
            @RequestParam(value = "fromId") Long fromId,
            @RequestParam(value = "toId") Long toId
    ) {
        List<CorpDO> corpList = corpDao.getCorpByIdRange(fromId, toId);
        Iterator it = corpList.iterator();
        while(it.hasNext()){
            CorpDO corpDO = (CorpDO)it.next();
            String corpId = corpDO.getCorpId();
            ServiceResult<Void> sr = staffManageService.getAndSaveCorpAdminList(corpId, suiteKey);
            if(!sr.isSuccess()){
                bizLogger.error("error in get and save corp admin list: corpId: " + corpId);
                continue;
            }
            sr = rsqAccountService.updateAllCorpAdmin(suiteKey, corpId);
            if(!sr.isSuccess()){
                bizLogger.error("error in update all corp admin list: corpId: " + corpId);
            }
        }
        return "<<success>>" + new Date();
    }
}
