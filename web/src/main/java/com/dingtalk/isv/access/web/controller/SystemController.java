package com.dingtalk.isv.access.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.constant.AccessSystemConfig;
import com.dingtalk.isv.access.api.model.corp.CorpAppVO;
import com.dingtalk.isv.access.api.model.corp.CorpJSAPITicketVO;
import com.dingtalk.isv.access.api.model.corp.CorpTokenVO;
import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.api.model.event.CorpAuthSuiteEvent;
import com.dingtalk.isv.access.api.model.event.mq.SuiteCallBackMessage;
import com.dingtalk.isv.access.api.model.suite.CorpSuiteAuthVO;
import com.dingtalk.isv.access.api.model.suite.CorpSuiteCallBackVO;
import com.dingtalk.isv.access.api.service.corp.CorpManageService;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.api.service.message.SendMessageService;
import com.dingtalk.isv.access.api.service.suite.CorpSuiteAuthService;
import com.dingtalk.isv.access.api.service.suite.SuiteManageService;
import com.dingtalk.isv.access.biz.corp.dao.CorpJSAPITicketDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpStaffDao;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.helper.CorpJSAPITicketConverter;
import com.dingtalk.isv.access.biz.corp.model.helper.StaffConverter;
import com.dingtalk.isv.access.biz.dingutil.ConfOapiRequestHelper;
import com.dingtalk.isv.access.biz.suite.dao.SuiteDao;
import com.dingtalk.isv.access.biz.suite.model.SuiteDO;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.common.util.HttpUtils;
import com.dingtalk.isv.rsq.biz.event.mq.RsqSyncMessage;
import com.dingtalk.isv.rsq.biz.httputil.RsqAccountRequestHelper;
import com.dingtalk.isv.rsq.biz.model.RsqUser;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.MessageBody;
import com.dingtalk.open.client.api.service.corp.CorpUserService;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 这个controller功能如下
 * 1.健康检测
 * 2.数据订正
 * 3.临时测试
 */
@Controller
public class SystemController {
    @Autowired
    private EventBus corpAuthSuiteEventBus;
    @Autowired
    private AsyncEventBus asyncCorpAuthSuiteEventBus;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    @Qualifier("suiteCallBackQueue")
    private Queue suiteCallBackQueue;
    @Autowired
    @Qualifier("rsqSyncCallBackQueue")
    private Queue rsqSyncCallBackQueue;


    @RequestMapping("/test")
    @ResponseBody
    public String checkPreload() {
        asyncCorpAuthSuiteEventBus.post(new Integer(100));
        asyncCorpAuthSuiteEventBus.post(new Integer(200));
        asyncCorpAuthSuiteEventBus.post(new Integer(300));
        return "success......0000";
    }

    @RequestMapping("/mqtest")
    @ResponseBody
    public String mqTest() {
        System.out.println("--------------/mqtest------------");
        JSONObject jsonObject = JSON.parseObject("{\"hello\":\"Wallace\"}");

//        jmsTemplate.send(suiteCallBackQueue,new SuiteCallBackMessage(jsonObject, SuiteCallBackMessage.Tag.CHAT_ADD_MEMBER));
        return "success";
    }

    @Resource
    private CorpSuiteAuthService corpSuiteAuthService;
    @Resource
    private CorpManageService corpManageService;
    @Resource
    private CorpUserService corpUserService;
    @Autowired
    private StaffManageService staffManageService;
    @Resource
    SuiteManageService suiteManageService;
    @Resource
    AccessSystemConfig accessSystemConfig;
    @Resource
    CorpJSAPITicketDao corpJSAPITicketDao;
    @Resource
    ConfOapiRequestHelper confOapiRequestHelper;

    @Resource
    CorpStaffDao corpStaffDao;

    @Autowired
    private SuiteDao suiteDao;

    @Autowired
    private RsqAccountRequestHelper rsqAccountRequestHelper;

    @Resource
    SendMessageService sendMessageService;

    @RequestMapping("/test/sendCorpMessage/{suiteKey}")
    @ResponseBody
    public String testSendCorpMessage(
            @PathVariable("suiteKey") String suiteKey,
            @RequestParam(value = "corpId", required = true) String corpId
    ){
        try {
            List<String> staffList = new ArrayList<String>();
            staffList.add("manager5864");
            MessageBody.TextBody messageBody = new MessageBody.TextBody();
            messageBody.setContent("hello wallace!");
            Long appId = 2585L;

            jmsTemplate.send(rsqSyncCallBackQueue,new RsqSyncMessage(suiteKey, corpId));

//            ServiceResult sendSr = sendMessageService.sendMessageToUser(suiteKey, corpId, appId, "text", staffList, null, messageBody);
//            if(!sendSr.isSuccess()){
//                return "send failed:" + sendSr.getMessage();
//            }
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 更新指定corpId的企业员工的unionId
     * @param suiteKey
     * @param corpId
     * @return
     */
    @RequestMapping("/test/updateallunionid/{suiteKey}")
    @ResponseBody
    public String testUpdateAllUnionId(
            @PathVariable("suiteKey") String suiteKey,
            @RequestParam(value = "corpId", required = true) String corpId
    ){
        try {
            ServiceResult<List<StaffVO>> listSr = staffManageService.getStaffListByCorpId(corpId);
            if(!listSr.isSuccess()){
                return "fail get staff list=====" + listSr.getMessage();
            }
            List<StaffVO> list = listSr.getResult();

            Iterator it = list.iterator();
            while(it.hasNext()){
                StaffVO staffVO = (StaffVO)it.next();
                String staffId = staffVO.getStaffId();
                //  只处理不一致的情况
                if(staffId == null || !staffId.equals(staffVO.getUnionId())){
                    continue;
                }
                ServiceResult<StaffVO> sr = staffManageService.getStaff(staffVO.getStaffId(), corpId, suiteKey);
                if(!sr.isSuccess()){
                    return "fail get staff=====" + sr.getMessage();
                }
                StaffVO newStaffVO = sr.getResult();

                corpStaffDao.updateUnionId(StaffConverter.staffVO2StaffDO(newStaffVO));
            }
            return "successfully executed";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping("/test/postrsqupdateuser/{suiteKey}")
    @ResponseBody
    public String testPostRsqUpdateUser(
            @PathVariable("suiteKey") String suiteKey,
            @RequestParam(value = "corpId", required = true) String corpId
    ){
        try {
            StringBuffer result = new StringBuffer();
            //  suiteKey
            SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);

            ServiceResult<List<StaffVO>> listSr = staffManageService.getStaffListByCorpId(corpId);
            if(!listSr.isSuccess()){
                return "fail get staff list=====" + listSr.getMessage();
            }
            List<StaffVO> list = listSr.getResult();

            Map params = new HashMap<String, Object>();

            Iterator it = list.iterator();
            while(it.hasNext()){
                StaffVO staffVO = (StaffVO)it.next();

                ServiceResult<RsqUser> rsqUserSr = rsqAccountRequestHelper.updateUser(suiteDO, StaffConverter.staffVO2StaffDO(staffVO), params);

                if(!rsqUserSr.isSuccess()){
                    return "fail post staff=====" + rsqUserSr.getMessage();
                }
                if(list.size() < 10){
                    RsqUser rsqUser = rsqUserSr.getResult();
                    result.append("--start-- suiteDO is ");
                    result.append(suiteDO.getSuiteKey());
                    result.append(",");
                    result.append(rsqUser.getId()).append(",");
                    result.append(rsqUser.getUsername()).append(",");
                    result.append(rsqUser.getPassword()).append(",");
                    result.append(rsqUser.getOuterId()).append(",");
                    result.append(rsqUser.getFromClient()).append(",");
                    result.append(rsqUser.getRealName()).append(",");
                    result.append(rsqUser.getUnionId()).append(",");
                    result.append("--end--");
                }
            }
            result.append("successfully executed");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

    }

    @RequestMapping("/test/getcorpuser")
    @ResponseBody
    public String testGetCorpUser(){
        try {
            String suiteKey = "suitehu9dcew9gmutcbwd";
            String corpId = "dinga5892228289863f535c2f4657eb6378f";
            String staffId = "manager5864";

            ServiceResult<StaffVO> sr = staffManageService.getStaff(staffId, corpId, suiteKey);
            if(!sr.isSuccess()){
                return "fail=====" + sr.getMessage();
            }
            StaffVO staffVO = sr.getResult();
            ServiceResult<Void> saveSr = staffManageService.saveOrUpdateCorpStaff(staffVO);
            if(!saveSr.isSuccess()){
                return "fail in saveOrUpdateStaff:" + saveSr.getMessage();
            }
//            CorpUserDetail corpUserDetail = corpUserService.getCorpUser(corpToken, staffId);
//            return StaffConverter.corpUser2StaffVO(corpUserDetail, corpId).toString();
            return staffVO.toString() + "-----====aaabbcc";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping("/dbtest")
    @ResponseBody
    public String checkSomeTest(
            @RequestParam(value = "username", required = true) String username) {
        try {
            StaffVO staff = new StaffVO();
            staff.setCorpId("hellotest");
            staff.setStaffId(String.valueOf(new Date().getTime()));
            staff.setName(username);
            ServiceResult<Void> staffSaveSr = staffManageService.saveOrUpdateCorpStaff(staff);

            return "success";
        } catch (Exception e){
            e.printStackTrace();
            return "fail";
        }

    }

    @ResponseBody
    @RequestMapping(value = "/token/{suiteKey}", method = {RequestMethod.GET})
    public String genToken( HttpServletRequest request,@PathVariable("suiteKey") String suiteKey ) {
        String ip = HttpUtils.getRemortIP(request);
        if("127.0.0.1".equals(ip)){
            ServiceResult<Void> sr = suiteManageService.saveOrUpdateSuiteToken(suiteKey);
            if(sr.isSuccess()){
                return "success";
            }
            return sr.getCode()+","+sr.getMessage();
        }
        return "is not 127.0.0.1";
    }


    @RequestMapping(value = "/getCorpToken", method = {RequestMethod.GET})
    @ResponseBody
    public String getAccessToken(HttpServletRequest request,
                                  @RequestParam(value = "corpId", required = true) String corpId,
                                  @RequestParam(value = "suiteKey", required = true) String suiteKey) {
        String ip = HttpUtils.getRemortIP(request);
        if("127.0.0.1".equals(ip)){
            ServiceResult<CorpTokenVO> sr = corpManageService.getCorpToken(suiteKey, corpId);
            if(sr.isSuccess()){
                return "success";
            }
            return sr.getCode()+","+sr.getMessage();
        }
        return "is not 127.0.0.1";
    }


    @RequestMapping(value = "/getJsTicket", method = {RequestMethod.GET})
    @ResponseBody
    public String getJsTicket(HttpServletRequest request,
                                 @RequestParam(value = "corpId", required = true) String corpId,
                                 @RequestParam(value = "suiteKey", required = true) String suiteKey) {
        String ip = HttpUtils.getRemortIP(request);
        if("127.0.0.1".equals(ip)){
            ServiceResult<CorpTokenVO> corpTokenVoSr = corpManageService.getCorpToken(suiteKey, corpId);
            ServiceResult<CorpJSAPITicketVO> jsAPITicketSr = confOapiRequestHelper.getJSTicket(suiteKey, corpId, corpTokenVoSr.getResult().getCorpToken());
            corpJSAPITicketDao.saveOrUpdateCorpJSAPITicket(CorpJSAPITicketConverter.corpJSTicketVO2CorpJSTicketDO(jsAPITicketSr.getResult()));
            return "success";
        }
        return "is not 127.0.0.1";
    }


    @RequestMapping(value = "/getSuiteToken", method = {RequestMethod.GET})
    @ResponseBody
    public String getSuiteAccessToken(HttpServletRequest request,
                                      @RequestParam(value = "suiteKey", required = true) String suiteKey) {
        String ip = HttpUtils.getRemortIP(request);
        if("127.0.0.1".equals(ip)){
            ServiceResult<Void> sr = suiteManageService.saveOrUpdateSuiteToken(suiteKey);
            if(sr.isSuccess()){
                return "success";
            }
            return sr.getCode()+","+sr.getMessage();
        }
        return "is not 127.0.0.1";
    }



    /**
     * 更新企业回调地址
     * @param request
     * @param suiteKey
     * @return
     */
    @RequestMapping(value = "/updateCorpCallBack", method = {RequestMethod.GET})
    @ResponseBody
    public String updateCorpCallBack(HttpServletRequest request,
                                    @RequestParam(value = "suiteKey", required = true) String suiteKey,
                                    @RequestParam(value = "corpId", required = true) String corpId
                                    ) {
        String ip = HttpUtils.getRemortIP(request);
        if("127.0.0.1".equals(ip)){
            //订正全体
            if(StringUtils.isEmpty(corpId)){
                ServiceResult<List<CorpSuiteAuthVO>> sr = corpSuiteAuthService.getCorpSuiteAuthByPage(suiteKey, 0, Integer.MAX_VALUE);
                List<CorpSuiteAuthVO> corpSuiteAuthVOList = sr.getResult();
                for(CorpSuiteAuthVO corpSuiteAuthVO:corpSuiteAuthVOList){
                    ServiceResult<CorpSuiteCallBackVO> callBackSr =  corpSuiteAuthService.getCorpCallback(suiteKey, corpSuiteAuthVO.getCorpId());
                    if(callBackSr.isSuccess()){
                        corpSuiteAuthService.updateCorpCallback(suiteKey, corpSuiteAuthVO.getCorpId(), (accessSystemConfig.getCorpSuiteCallBackUrl() + suiteKey), SuiteCallBackMessage.Tag.getAllTag());
                        continue;
                    }
                    corpSuiteAuthService.saveCorpCallback(suiteKey, corpSuiteAuthVO.getCorpId(), (accessSystemConfig.getCorpSuiteCallBackUrl()+suiteKey), SuiteCallBackMessage.Tag.getAllTag());
                }
            }else{
                ServiceResult<CorpSuiteCallBackVO> callBackSr =  corpSuiteAuthService.getCorpCallback(suiteKey, corpId);
                if(callBackSr.isSuccess()){
                    corpSuiteAuthService.updateCorpCallback(suiteKey, corpId, (accessSystemConfig.getCorpSuiteCallBackUrl() + suiteKey), SuiteCallBackMessage.Tag.getAllTag());
                }
                corpSuiteAuthService.saveCorpCallback(suiteKey, corpId, (accessSystemConfig.getCorpSuiteCallBackUrl()+suiteKey), SuiteCallBackMessage.Tag.getAllTag());
            }
            return "success";
        }
        return "is not 127.0.0.1";
    }







}
