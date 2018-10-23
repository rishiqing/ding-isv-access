package com.dingtalk.isv.rsq.biz.client;

import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.biz.corp.dao.CorpChargeStatusDao;
import com.dingtalk.isv.access.biz.corp.dao.CorpStaffDao;
import com.dingtalk.isv.access.biz.corp.dao.StaffPopupConfigDao;
import com.dingtalk.isv.access.biz.corp.dao.StaffPopupLogDao;
import com.dingtalk.isv.access.biz.corp.model.CorpChargeStatusDO;
import com.dingtalk.isv.access.biz.corp.model.StaffDO;
import com.dingtalk.isv.access.biz.corp.model.StaffPopupConfigDO;
import com.dingtalk.isv.access.biz.corp.model.StaffPopupLogDO;
import com.dingtalk.isv.access.biz.exception.ApplicationException;
import com.dingtalk.isv.access.biz.order.dao.OrderSpecItemDao;
import com.dingtalk.isv.access.biz.order.model.OrderSpecItemDO;
import com.dingtalk.isv.common.model.ServiceResult;
import com.dingtalk.isv.rsq.biz.model.PopupInfoVO;
import com.dingtalk.isv.rsq.biz.model.PopupType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dingtalk.isv.rsq.biz.model.PopupType.OLD_UPGRADE;

/**
 * @author Wallace Mao
 * Date: 2018-10-19 19:57
 */
public class PopupService {
    private static final Logger bizLogger = LoggerFactory.getLogger("STAFF_MANAGE_LOGGER");
    private static final Logger mainLogger = LoggerFactory.getLogger(PopupService.class);

    //  这里的long类型，需要在数字后面都加上L，否则java将会默认为int类型，会很容易overflow
    private static final long ONE_HUNDRED_YEAR_MILLS = 1000L * 60L * 60L * 24L * 365L * 100L;
    private static final long FOR_DAY_YEAR_MILLS = 1000L * 60L * 60L * 24L * 4L;

    @Autowired
    private StaffPopupConfigDao staffPopupConfigDao;
    @Autowired
    private StaffPopupLogDao staffPopupLogDao;
    @Autowired
    private CorpChargeStatusDao corpChargeStatusDao;
    @Autowired
    private CorpStaffDao corpStaffDao;
    @Autowired
    private OrderSpecItemDao orderSpecItemDao;

    /**
     * 获取弹窗信息
     * @param suiteKey
     * @param corpId
     * @param userId
     * @return
     */
    public PopupInfoVO getPopupInfo(String suiteKey, String corpId, String userId){
        PopupInfoVO popupInfo = new PopupInfoVO();
        //  设置默认的数据
        popupInfo.setCorpId(corpId);
        popupInfo.setServiceExpire(0L);
        popupInfo.setBuyNumber(0L);
        popupInfo.setTotalNumber(0L);
        popupInfo.setSpecKey("");
        //  读取当前用户信息
        StaffDO staffDO = corpStaffDao.getStaffByCorpIdAndUserId(corpId, userId);
        popupInfo.setAdmin(staffDO.getAdmin());

        //  读取企业信息
        CorpChargeStatusDO corpStatus = corpChargeStatusDao.getCorpChargeStatusBySuiteKeyAndCorpId(suiteKey, corpId);
        if(corpStatus != null){
            popupInfo.setServiceExpire(corpStatus.getCurrentServiceStopTime());
            popupInfo.setBuyNumber(corpStatus.getCurrentSubQuantity());
            popupInfo.setTotalNumber(corpStatus.getTotalQuantity());

            //  读取规格信息
            OrderSpecItemDO spec = orderSpecItemDao.getOrderSpecItemBySuiteKeyAndGoodsCodeAndItemCode(
                    suiteKey,
                    corpStatus.getCurrentGoodsCode(),
                    corpStatus.getCurrentItemCode());

            popupInfo.setSpecKey(spec.getInnerKey());
        }

        //  读取mute信息
        List<StaffPopupConfigDO> configList =
                staffPopupConfigDao.getStaffPopupConfigListBySuiteKeyAndIsDisabled(suiteKey, false);
        Map<String, StaffPopupConfigDO> popupConfigMap = new HashMap<String, StaffPopupConfigDO>();
        Map<String, StaffPopupLogDO> muteInfoMap = new HashMap<String, StaffPopupLogDO>();
        for(StaffPopupConfigDO configDO : configList){
            String type = configDO.getPopupType();
            popupConfigMap.put(type, configDO);
            StaffPopupLogDO logDO =
                    staffPopupLogDao.getStaffPopupLogBySuiteKeyAndCorpIdAndUserIdAndType(suiteKey, corpId, userId, type);
            muteInfoMap.put(type, logDO);
        }
        popupInfo.setPopupConfigMap(popupConfigMap);
        popupInfo.setMuteInfoMap(muteInfoMap);

        return popupInfo;
    }

    /**
     * 记录用户的弹窗信息，当用户点击关闭弹窗后，会根据实际情况有一定时间的静默期，分四种情况：
     * 1  老用户升级提醒：100年以后
     * 2  三日到期提醒：用户点击之后的第四天
     * 3  服务到期提醒：不做处理
     * 4  超员提醒：不做处理
     * @param suiteKey
     * @param corpId
     * @param userId
     * @param type
     */
    public void logStaffPopup(String suiteKey, String corpId, String userId, String type){
        PopupType popupType = PopupType.getPopupType(type);
        if(popupType == null){
            throw new ApplicationException("popupType not found: " + type);
        }
        long expireTime = 0L;
        long now = new Date().getTime();

        switch (popupType){
            case OLD_UPGRADE:
                expireTime = now + ONE_HUNDRED_YEAR_MILLS;
                break;
            case SOON_EXPIRE:
                expireTime = now + FOR_DAY_YEAR_MILLS;
                break;
            default:
                break;
        }
        if(expireTime != 0L){
            StaffPopupLogDO logDO = staffPopupLogDao.getStaffPopupLogBySuiteKeyAndCorpIdAndUserIdAndType(
                    suiteKey, corpId, userId, type
            );
            if(logDO == null){
                logDO = new StaffPopupLogDO();
                logDO.setPopupType(popupType.getKey());
                logDO.setSuiteKey(suiteKey);
                logDO.setCorpId(corpId);
                logDO.setUserId(userId);

            }
            logDO.setPopupMuteExpire(expireTime);
            staffPopupLogDao.saveOrUpdateStaffPopupLog(logDO);
        }
    }
}
