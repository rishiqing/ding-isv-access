package com.dingtalk.isv.access.biz.order.dao;

import com.dingtalk.isv.access.biz.order.model.OrderSpecItemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("orderSpecItemDao")
public interface OrderSpecItemDao {

	/**
	 * 保存orderSpecItemDO
	 * @param orderSpecItemDO
	 */
	public void saveOrUpdateOrderSpecItemDO(OrderSpecItemDO orderSpecItemDO);

	public OrderSpecItemDO getOrderSpecItemBySuiteKeyAndGoodsCodeAndItemCode(
			@Param("suiteKey") String suiteKey,
			@Param("goodsCode") String goodsCode,
			@Param("itemCode") String itemCode
	);
}

