package com.dingtalk.isv.access.biz.order.dao;

import com.dingtalk.isv.access.biz.order.model.OrderEventDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("orderEventDao")
public interface OrderEventDao {

	/**
	 * 保存orderEvent
	 * @param orderEventDO
	 */
	public void saveOrUpdateOrderEvent(OrderEventDO orderEventDO);


	/**
	 * 根据id查询event
	 * @param id
	 * @return
	 */
	public OrderEventDO getOrderEventById(@Param("id") Long id);
}

