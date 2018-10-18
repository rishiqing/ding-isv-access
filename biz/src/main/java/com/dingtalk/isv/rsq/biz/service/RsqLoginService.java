package com.dingtalk.isv.rsq.biz.service;

import com.rishiqing.qywx.service.common.crypto.CryptoUtil;
import com.rishiqing.qywx.service.model.corp.CorpStaffVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author Wallace Mao
 * Date: 2018-05-23 20:34
 */
public class RsqLoginService {
    @Autowired
    private CryptoUtil cryptoUtil;

    @Override
    public String generateLoginToken(CorpStaffVO corpStaffVO) {
        String loginStr = makeLoginString(corpStaffVO);
        return cryptoUtil.encrypt(loginStr);
    }

    private String makeLoginString(CorpStaffVO corpStaffVO){
        return String.valueOf(new Date().getTime()) +
                "--" +
                corpStaffVO.getCorpId() +
                "--" +
                corpStaffVO.getUserId();
    }
}
