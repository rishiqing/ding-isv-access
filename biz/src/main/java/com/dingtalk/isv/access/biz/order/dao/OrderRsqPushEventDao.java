package com.dingtalk.isv.access.biz.order.dao;

import com.dingtalk.isv.access.biz.order.model.OrderRsqPushEventDO;
import com.dingtalk.isv.access.biz.order.model.OrderStatusDO;
import org.springframework.stereotype.Repository;

@Repository("orderRsqPushEventDao")
public interface OrderRsqPushEventDao {

	/**
	 * 保存orderRsqPushEventDO
	 * @param orderRsqPushEventDO
	 */
	public void saveOrUpdateOrderRsqPushEvent(OrderRsqPushEventDO orderRsqPushEventDO);
}

