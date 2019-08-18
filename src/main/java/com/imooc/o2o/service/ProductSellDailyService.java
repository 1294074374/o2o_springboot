package com.imooc.o2o.service;

import java.util.Date;
import java.util.List;

import com.imooc.o2o.entity.ProductSellDaily;

public interface ProductSellDailyService {
	/**
	 * 每日定时所有的商铺的商品销量进行统计
	 */
	void dailyCalculate();

	/**
	 * 根据产需条件返回商品日销量的统计列表
	 * 
	 * @param productSellDailyCondition
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime,
			Date endTime);
}
