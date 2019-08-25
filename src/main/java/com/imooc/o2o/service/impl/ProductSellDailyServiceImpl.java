package com.imooc.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.o2o.dao.ProductSellDailyDao;
import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.service.ProductSellDailyService;

@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {
	private static final Logger log = LoggerFactory.getLogger(ProductSellDailyDao.class);
	@Autowired
	private ProductSellDailyDao productSellDailyDao;

	@Override
	public void dailyCalculate() {
		log.info("Quartz Runnering!");
		productSellDailyDao.insertProductSellDaily();
		//System.out.println("Quartz运行了");
		productSellDailyDao.insertDefaultProductSellDaily();

	}

	@Override
	public List<ProductSellDaily> listProductSellDaily(ProductSellDaily productSellDailyCondition, Date beginTime,
			Date endTime) {
		return productSellDailyDao.queryProductSellDailyList(productSellDailyCondition, beginTime, endTime);
	}

}
