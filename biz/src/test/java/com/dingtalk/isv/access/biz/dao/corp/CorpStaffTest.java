package com.dingtalk.isv.access.biz.dao.corp;

import com.dingtalk.isv.access.biz.base.BaseTestCase;
import com.dingtalk.isv.access.biz.corp.dao.CorpStaffDao;
import com.dingtalk.isv.access.biz.corp.model.CorpDO;
import com.dingtalk.isv.access.biz.corp.model.StaffIdsDO;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 */
public class CorpStaffTest extends BaseTestCase {

    @Resource
    private CorpStaffDao corpStaffDao;

    @Test
    public void test_getUserIdFromRsqId() {
//        String[] arr = new String[]{"manager5864","0205536526115909"};
        String[] arr = new String[]{"1406","1410","7777"};
        List<StaffIdsDO> list = corpStaffDao.getUserIdFromRsqId("dinga5892228289863f535c2f4657eb6378f",arr);
        System.out.println(list);
    }
    @Test
    public void test_getRsqIdFromUserId() {
        String[] arr = new String[]{"manager5864","0205536526115909"};
//        String[] arr = new String[]{"1406","1410","7777"};
        List<StaffIdsDO> list = corpStaffDao.getRsqIdFromUserId("dinga5892228289863f535c2f4657eb6378f",arr);
        System.out.println(list);
    }

}
