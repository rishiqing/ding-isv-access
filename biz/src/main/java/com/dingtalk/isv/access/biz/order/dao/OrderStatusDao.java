package com.dingtalk.isv.access.biz.order.dao;

import com.dingtalk.isv.access.biz.order.model.OrderStatusDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("orderStatusDao")
public interface OrderStatusDao {

	/**
	 * 保存orderStatusDO
	 * @param orderStatusDO
	 */
	public void saveOrUpdateOrderStatus(OrderStatusDO orderStatusDO);

	public OrderStatusDO getOrderStatusByOrderId(@Param("orderId") Long orderId);
}

