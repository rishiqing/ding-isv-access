package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.CorpOrgFetchFailDO;
import com.dingtalk.isv.access.biz.suite.model.CorpSuiteAuthFaileDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wallace on 17-1-3.
 */
@Repository("corpOrgFetchFailDao")
public interface CorpOrgFetchFailDao {

    /**
     * 创建一个企业组织机构信息获取失败的对象
     * @param corpOrgFetchFailDO
     */
    public void addOrUpdateCorpOrgFetchFailDO(CorpOrgFetchFailDO corpOrgFetchFailDO);



    public List<CorpOrgFetchFailDO> getCorpOrgFetchFailList(@Param("offset") Integer offset, @Param("limit") Integer limit);


    public void deleteById(@Param("id") Long id);
}
