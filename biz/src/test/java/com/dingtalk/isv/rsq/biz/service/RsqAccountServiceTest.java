package com.dingtalk.isv.rsq.biz.service;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.model.corp.CorpVO;
import com.dingtalk.isv.access.biz.base.BaseTestCase;
import com.dingtalk.isv.common.model.ServiceResult;
import org.eclipse.core.runtime.Assert;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by Wallace on 2016/11/29.
 */
public class RsqAccountServiceTest extends BaseTestCase {
    @Resource
    RsqAccountService rsqAccountService;

    @Test
    public void test_createRsqTeam(){
        String suiteKey = "suitee7uslxed43cjwsm7";
        String corpid = "dinga5892228289863f535c2f4657eb6378f";
        ServiceResult<CorpVO> sr = rsqAccountService.createRsqTeam(suiteKey, corpid);
        System.out.println(JSON.toJSON(sr));
    }

    @Test
    public void test_createRsqTeamStaff(){

    }
}
