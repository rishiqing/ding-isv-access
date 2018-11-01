package com.dingtalk.isv.access.web.controller.db;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.corp.StaffManageService;
import com.dingtalk.isv.access.api.service.message.SendMessageService;
import com.dingtalk.isv.access.biz.constant.SystemConstant;
import com.dingtalk.isv.access.biz.corp.dao.CorpChargeStatusDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpStaffDao;
import com.dingtalk.isv.access.biz.corp.model.CorpChargeStatusDO;
import com.dingtalk.isv.access.biz.corp.model.CorpDO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.order.dao.OrderRsqPushEventDao;
import com.dingtalk.isv.access.biz.order.dao.OrderSpecItemDao;
import com.dingtalk.isv.access.biz.order.model.OrderRsqPushEventDO;
import com.dingtalk.isv.access.biz.order.model.OrderSpecItemDO;
import com.dingtalk.isv.access.biz.order.model.helper.OrderModelConverter;
import com.dingtalk.isv.access.biz.suite.dao.SuiteDao;
import com.dingtalk.isv.access.biz.suite.model.SuiteDO;
import com.dingtalk.isv.access.biz.util.MessageUtil;
import com.dingtalk.isv.common.code.ServiceResultCode;
import com.dingtalk.isv.common.log.format.LogFormatter;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.event.mq.RsqSyncMessage;
import com.dingtalk.isv.rsq.biz.httputil.RsqAccountRequestHelper;
import com.dingtalk.isv.rsq.biz.service.RsqAccountService;
import com.dingtalk.open.client.api.model.corp.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jms.Queue;
import java.util.*;

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
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    @Qualifier("rsqSyncCallBackQueue")
    private Queue rsqSyncCallBackQueue;

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

    /**
     * 给指定范围内的用户推送消息：
     * 如果corpId存在，那么只给corpId的公司的管理员发送发送
     * 否则给staffNumberMin到staffNumberMax范围内的公司管理员发送
     * @param suiteKey
     * @param corpNumberMin
     * @param corpNumberMax
     * @param json
     * @return
     */
    @RequestMapping(value = "/admin/message/push", method = {RequestMethod.POST})
    @ResponseBody
    public String pushMessage(
            @RequestParam(value = "suiteKey") String suiteKey,
            @RequestParam(value = "corpNumberMin", required = false) Long corpNumberMin,
            @RequestParam(value = "corpNumberMax", required = false) Long corpNumberMax,
            @RequestBody JSONObject json
    ) {
        try{
            Long fromId = corpNumberMin == null ? 0 : corpNumberMin;
            Long toId = corpNumberMax == null ? 0 : corpNumberMax;
            for(long i = fromId; i < toId + 1; i++){
                manualPostMessage(suiteKey, i, json);
            }
            return "success";
        }catch (Exception e){
            bizLogger.error("error in message push", e);
            return "fail";
        }
    }

    @Autowired
    private CorpChargeStatusDao corpChargeStatusDao;
    @Autowired
    private OrderRsqPushEventDao orderRsqPushEventDao;
    @Autowired
    private OrderSpecItemDao orderSpecItemDao;
    @Autowired
    private SuiteDao suiteDao;
    @Autowired
    private CorpStaffDao corpStaffDao;
    @Autowired
    private RsqAccountRequestHelper rsqAccountRequestHelper;
    @Autowired
    private SendMessageService sendMessageService;
    @Resource(name="isvGlobal")
    private Map<String, String> isvGlobal;
    /**
     * 给所有老用户充值试用版，如果corpId存在，那么只给corpId发，否则给
     * @param suiteKey
     * @param corpNumberMin
     * @param corpNumberMax
     * @param expireDate
     * @return
     */
    @RequestMapping(value = "/admin/charge/trial", method = {RequestMethod.POST})
    @ResponseBody
    public String chargeTrial(
            @RequestParam(value = "suiteKey") String suiteKey,
            @RequestParam(value = "corpNumberMin", required = false) Long corpNumberMin,
            @RequestParam(value = "corpNumberMax", required = false) Long corpNumberMax,
            @RequestParam(value = "expireDate", required = false) Long expireDate
    ) {
        try{
            Long fromId = corpNumberMin == null ? 0 : corpNumberMin;
            Long toId = corpNumberMax == null ? 0 : corpNumberMax;
            expireDate = expireDate == null ? new Date().getTime() + 30L * 24L * 3600L * 1000L : expireDate;
            for(long i = fromId; i < toId + 1; i++){
                manualCharge(suiteKey, i, expireDate);
            }
            return "success";
        }catch (Exception e){
            bizLogger.error("error in charge trial", e);
            return "fail";
        }
    }

    private void manualPostMessage(String suiteKey, Long id, JSONObject json){
        CorpDO corp = corpDao.getCorpById(id);
        if(corp == null){
            return;
        }
        CorpChargeStatusDO status = corpChargeStatusDao.getCorpChargeStatusBySuiteKeyAndCorpId(suiteKey, corp.getCorpId());
        //  status不存在，那么不发送消息
        if(status == null){
            return;
        }
        // goodsCode码为空，不发送消息
        if(status.getCurrentGoodsCode() == null){
            return;
        }
        // maxOfPeople不为99999，那么不发送消息
        if(status.getCurrentMaxOfPeople() != 99999L){
            return;
        }
        String corpId = corp.getCorpId();
        //  找到企业管理员
        List<StaffDO> adminList = corpStaffDao.getStaffListByCorpIdAndIsAdmin(corpId, true);
        //  如果管理员人数超过20，那么只给前20个人发
        if(adminList.size() > 20){
            adminList = adminList.subList(0, 20);
        }
        String msgType = json.getString("msgtype");
        //Long appId = json.getLong("agent_id");

        List<String> userIdList = new ArrayList<String>();
        for (StaffDO staffDO : adminList){
            if(staffDO.getUserId() != null){
                userIdList.add(staffDO.getUserId());
            }
        }

        //  安全起见，toAllUser接口不开放
        Boolean toAllUser = false;
        Long appId = Long.valueOf(isvGlobal.get("appId"));


        MessageBody message = MessageUtil.parseMessage(json);
        sendMessageService.sendCorpMessageAsync(suiteKey, corpId, appId, msgType, toAllUser, userIdList, null, message);
    }

    private void manualCharge(String suiteKey, Long id, Long expireMills){
        CorpDO corp = corpDao.getCorpById(id);
        if(null == corp){
            return;
        }
        // 已经存在充值记录的不给充值
        if(null != corpChargeStatusDao.getCorpChargeStatusBySuiteKeyAndCorpId(suiteKey, corp.getCorpId())){
            return;
        }
        String corpId = corp.getCorpId();
        Long quantity = 99999L;
        String goodsCode = "FW_GOODS-1000330934";
        String itemCode = "0423c5392dbb385581b097b00df0da41";
        OrderRsqPushEventDO rsqPushEvent = new OrderRsqPushEventDO();
        rsqPushEvent.setSuiteKey(suiteKey);
        rsqPushEvent.setOrderId(id);
        rsqPushEvent.setCorpId(corpId);
        rsqPushEvent.setQuantity(quantity);
        rsqPushEvent.setServiceStopTime(expireMills);
        rsqPushEvent.setStatus(SystemConstant.ORDER_PUSH_STATUS_PENDING);
        rsqPushEvent.setRsqTeamId(Long.valueOf(corp.getRsqId()));
        orderRsqPushEventDao.saveOrUpdateOrderRsqPushEvent(rsqPushEvent);

        //发送后台接口进行充值
        SuiteDO suiteDO = suiteDao.getSuiteByKey(suiteKey);
        OrderSpecItemDO specItemDO = orderSpecItemDao.getOrderSpecItemBySuiteKeyAndGoodsCodeAndItemCode(
                suiteKey, goodsCode, itemCode);
        ServiceResult<Void> requestSr = rsqAccountRequestHelper.doCharge(suiteDO, specItemDO, rsqPushEvent);
        if(!requestSr.isSuccess()){
            bizLogger.error(LogFormatter.getKVLogData(LogFormatter.LogEvent.END,
                    "系统异常" ,
                    LogFormatter.KeyValue.getNew("suiteKey", suiteKey),
                    LogFormatter.KeyValue.getNew("id", id)
            ));
            return;
        }

        // 更新OrderRsqPushEventDO的状态为success
        rsqPushEvent.setStatus(SystemConstant.ORDER_PUSH_STATUS_SUCCESS);
        orderRsqPushEventDao.saveOrUpdateOrderRsqPushEvent(rsqPushEvent);

        // 保存CorpChargeStatusDO
        CorpChargeStatusDO corpChargeStatusDO = new CorpChargeStatusDO();
        corpChargeStatusDO.setSuiteKey(suiteKey);
        corpChargeStatusDO.setCorpId(corpId);
        corpChargeStatusDO.setCurrentMaxOfPeople(quantity);
        corpChargeStatusDO.setCurrentMinOfPeople(0L);
        corpChargeStatusDO.setCurrentServiceStopTime(expireMills);
        corpChargeStatusDO.setCurrentGoodsCode(goodsCode);
        corpChargeStatusDO.setCurrentItemCode(itemCode);
        corpChargeStatusDO.setStatus(SystemConstant.ORDER_CORP_CHARGE_STATUS_OK);
        // 计算当前人数，先暂时指定公司总人数为1，保证不超员，以后改为每天查人数来更新这个字段
        corpChargeStatusDO.setTotalQuantity(1L);
        corpChargeStatusDao.saveOrUpdateCorpChargeStatus(corpChargeStatusDO);
    }
}
