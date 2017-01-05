package com.dingtalk.isv.access.biz.corp.dao;

import com.dingtalk.isv.access.biz.corp.model.CorpCallbackFailDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Wallace on 17-1-3.
 */
@Repository("corpCallbackFailDao")
public interface CorpCallbackFailDao {

    /**
     * 创建一个企业通讯录信息获取失败的对象
     * @param corpCallbackFailDO
     */
    public void addOrUpdateCorpCallbackFailDO(CorpCallbackFailDO corpCallbackFailDO);

    public List<CorpCallbackFailDO> getCorpCallbackFailList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    public void deleteById(@Param("id") Long id);
}
