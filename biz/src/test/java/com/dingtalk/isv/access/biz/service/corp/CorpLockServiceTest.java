package com.dingtalk.isv.access.biz.service.corp;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.model.corp.CorpJSAPITicketVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.access.biz.base.BaseTestCase;
import com.dingtalk.isv.access.biz.corp.enumtype.CorpLockType;
import com.dingtalk.isv.access.biz.corp.model.CorpLockDO;
import com.dingtalk.isv.access.biz.corp.service.CorpLockService;
import com.dingtalk.isv.access.biz.dingutil.ConfOapiRequestHelper;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.isv.CorpAuthToken;
import com.dingtalk.open.client.api.service.isv.IsvService;
import org.eclipse.core.runtime.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by mint on 16-1-22.
 */
public class CorpLockServiceTest extends BaseTestCase {

    private static final Logger bizLogger = LoggerFactory.getLogger("CORP_LOCK_LOGGER");



    @Resource
    private CorpManageService corpManageService;
    @Resource
    private CorpLockService corpLockService;
    @Resource
    private ConfOapiRequestHelper confOapiRequestHelper;

    @Test
    public void test_transaction() {
        try{
            CorpLockDO lock = corpLockService.releaseLock("ddd", CorpLockType.TOKEN);
            System.out.println(lock);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void test_jsapiticket() {
        String suiteKey = "suitee7uslxed43cjwsm7";
        String corpId = "dinga5892228289863f535c2f4657eb6378f";
        String token = "9468d7e3d82731dfb8ebdfdcc336da7c";
        ServiceResult<CorpJSAPITicketVO> jsAPITicketSr = confOapiRequestHelper.getJSTicket(suiteKey, corpId, token);
        System.out.println("-----" + jsAPITicketSr.getResult());

    }




//    @Test
//    public void test_getCorpChannelToken() {
//        String corpId = "ding4ed6d279061db5e7";//1069022
//        String suiteKey="suite4rkgtvvhr1neumx2";//16001
//        ServiceResult<CorpChannelTokenVO> sr = corpManageService.getCorpChannelToken(suiteKey,corpId);
//        System.err.println(JSON.toJSONString(sr));
//    }
//
//
//
//    @Test
//    public void test_getCorpChannelJSAPITicket() {
//        String corpId = "ding4ed6d279061db5e7";//1069022
//        String suiteKey="suite4rkgtvvhr1neumx2";//16001
//        ServiceResult<CorpChannelJSAPITicketVO> sr = corpManageService.getCorpChannelJSAPITicket(suiteKey,corpId);
//        System.err.println(JSON.toJSONString(sr));
//    }



}
