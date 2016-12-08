package com.dingtalk.isv.access.biz.service.corp;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.model.corp.CorpChannelJSAPITicketVO;
import com.dingtalk.isv.access.api.model.corp.CorpChannelTokenVO;
import com.dingtalk.isv.access.api.model.corp.CorpJSAPITicketVO;
import com.dingtalk.isv.access.api.model.corp.CorpTokenVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.access.biz.base.BaseTestCase;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.open.client.api.model.isv.CorpAuthToken;
import com.dingtalk.open.client.api.service.isv.IsvService;
import com.dingtalk.open.client.common.ServiceException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by mint on 16-1-22.
 */
public class CorpManageServiceTest extends BaseTestCase {

    private static final Logger bizLogger = LoggerFactory.getLogger("CORP_MANAGE_LOGGER");



    @Resource
    private CorpManageService corpManageService;
    @Resource
    private IsvService isvService;
    @Test
    public void test_getCorpToken() {
        //  多线程测试
        final String corpId = "dinga5892228289863f535c2f4657eb6378f";
        final String suiteKey = "suitee7uslxed43cjwsm7";
        final String suiteToken = "88ca40e9018433989cedf7dbe58f6127";
        final String permanentCode = "AzEi91_1OCfKJuFVoj-9pn8jftQEXCFaI5ALgUCtiyRhLeajIwlM0AZSmfxNjqqD";

        try {
            System.out.println(new Date().getTime() + "---start---" + ":");
            CorpAuthToken corpAuthToken;
            corpAuthToken = isvService.getCorpToken(suiteToken, corpId, permanentCode);
            System.out.println(new Date().getTime() + "---end---" + ":" + JSON.toJSON(corpAuthToken));

        } catch (Exception e) {
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" + e.toString(),
                    LogFormatter.KeyValue.getNew("suiteToken", suiteToken),
                    LogFormatter.KeyValue.getNew("corpId", corpId)
            ), e);
            e.printStackTrace();
        }

//        new Thread(){
//            public void run() {
//
//                bizLogger.info(LogFormatter.getKVLogData(LogFormatter.LogEvent.START,
//                        LogFormatter.KeyValue.getNew("suiteToken", suiteToken),
//                        LogFormatter.KeyValue.getNew("corpId", corpId)
//                ));
//
//                try {
//                    System.out.println(new Date() + "---start---" + this.getId() + ":" + isvService);
//                    CorpAuthToken corpAuthToken;
//                    corpAuthToken = isvService.getCorpToken(suiteToken, corpId, permanentCode);
//                    System.out.println(new Date() + "---end---" + this.getId() + ":" + JSON.toJSON(corpAuthToken));
//
//                } catch (Exception e) {
//                    bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
//                            "系统异常" + e.toString(),
//                            LogFormatter.KeyValue.getNew("suiteToken", suiteToken),
//                            LogFormatter.KeyValue.getNew("corpId", corpId)
//                    ), e);
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();

//        new Thread(){
//            public void run() {
//                try {
//                    System.out.println(new Date() + "==---start---" + this.getId() + ":");
//                    CorpAuthToken corpAuthToken;
//                    corpAuthToken = isvService.getCorpToken(suiteToken, corpId, permanentCode);
//                    System.out.println(new Date() + "==---end---" + this.getId() + ":" + JSON.toJSON(corpAuthToken));
//
//                } catch (Exception e) {
//                    bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
//                            "系统异常" + e.toString(),
//                            LogFormatter.KeyValue.getNew("suiteToken", suiteToken),
//                            LogFormatter.KeyValue.getNew("corpId", corpId)
//                    ), e);
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();

//        ServiceResult<CorpTokenVO> sr2 = corpManageService.getCorpToken(suiteKey,corpId);
//
//        System.out.println("sr2---" + JSON.toJSON(sr2));

    }

    @Test
    public void test_getCorpJSAPITicket() {
        Long startTime =System.currentTimeMillis();
        String corpId="ding4ed6d279061db5e7";
        String suiteKey="suiteytzpzchcpug3xpsm";
        ServiceResult<CorpJSAPITicketVO> sr = corpManageService.getCorpJSAPITicket(suiteKey, corpId);
        System.err.println(JSON.toJSONString(sr));
        System.err.println("rt:"+(System.currentTimeMillis()-startTime));
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
