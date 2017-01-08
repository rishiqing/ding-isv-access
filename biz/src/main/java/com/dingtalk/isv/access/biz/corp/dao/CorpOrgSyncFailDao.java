package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.CorpOrgSyncFailDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wallace on 17-1-3.
 */
@Repository("corpOrgSyncFailDao")
public interface CorpOrgSyncFailDao {

    /**
     * 创建一个企业组织机构信息获取失败的对象
     * @param corpOrgSyncFailDO
     */
    public void addOrUpdateCorpOrgSyncFailDO(CorpOrgSyncFailDO corpOrgSyncFailDO);



    public List<CorpOrgSyncFailDO> getCorpOrgSyncFailList(@Param("offset") Integer offset, @Param("limit") Integer limit);


    public void deleteById(@Param("id") Long id);
}
