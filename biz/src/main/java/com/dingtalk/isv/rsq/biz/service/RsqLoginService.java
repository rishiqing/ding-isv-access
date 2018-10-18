package com.dingtalk.isv.rsq.biz.service;

import com.dingtalk.isv.access.api.model.corp.StaffVO;
import com.dingtalk.isv.access.biz.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-05-23 20:34
 */
public class RsqLoginService {
    @Autowired
    private CryptoUtil cryptoUtil;

    public String generateLoginToken(StaffVO corpStaffVO) {
        String loginStr = makeLoginString(corpStaffVO);
        return cryptoUtil.encrypt(loginStr);
    }

    private String makeLoginString(StaffVO corpStaffVO){
        return String.valueOf(new Date().getTime()) +
                "--" +
                corpStaffVO.getCorpId() +
                "--" +
                corpStaffVO.getStaffId();
    }
}
