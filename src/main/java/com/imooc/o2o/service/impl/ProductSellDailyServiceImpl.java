package com.imooc.o2o.service.impl;

import org.springframework.stereotype.Service;

import com.imooc.o2o.service.ProductSellDailyService;

@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {

	@Override
	public void dailyCalculate() {
		System.out.println("Quartz运行了");

	}

}
