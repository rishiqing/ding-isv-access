package com.dingtalk.isv.access.bizex.dingpush.dao;

import com.dingtalk.isv.access.bizex.dingpush.model.OpenSyncBizData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("openSyncBizDataDao")
public interface OpenSyncBizDataDao {

	public void updateStatus(OpenSyncBizData data);

	public OpenSyncBizData getOpenSyncBizDataById(@Param("id") Long id);

	public List<OpenSyncBizData> getOpenSyncBizDataListByStatus(@Param("status") Long status);
}

