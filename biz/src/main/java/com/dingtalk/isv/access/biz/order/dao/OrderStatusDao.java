package com.dingtalk.isv.access.biz.order.dao;

import com.dingtalk.isv.access.biz.order.model.OrderStatusDO;
import org.springframework.stereotype.Repository;

@Repository("orderStatusDao")
public interface OrderStatusDao {

	/**
	 * 保存orderStatusDO
	 * @param orderStatusDO
	 */
	public void saveOrUpdateOrderStatus(OrderStatusDO orderStatusDO);
}

