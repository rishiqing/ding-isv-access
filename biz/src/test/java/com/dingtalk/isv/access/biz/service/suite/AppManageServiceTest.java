package com.dingtalk.isv.access.biz.service.suite;

import com.alibaba.fastjson.JSON;
import com.dingtalk.isv.access.api.model.suite.AppVO;
import com.dingtalk.isv.access.api.service.suite.AppManageService;
import com.dingtalk.isv.access.biz.base.BaseTestCase;
import com.dingtalk.isv.access.biz.suite.model.AppDO;
import com.dingtalk.isv.common.model.ServiceResult;
import org.junit.Test;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * Created by Wallace on 2016/11/18.
 */
public class AppManageServiceTest extends BaseTestCase {

    @Resource
    private AppManageService appManageService;

    @Test
    public void test_getAppByAppId() {
        Long appId = 2245L;
        ServiceResult<AppVO> sr = appManageService.getAppByAppId(appId);
        System.out.println(JSON.toJSON(sr));
        Assert.isTrue(null!=sr.getResult());
    }
}
