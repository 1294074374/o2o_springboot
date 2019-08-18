package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;

@RunWith(SpringRunner.class)
@SpringBootTest

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductSellDailyDaoTest {
	@Autowired
	private ProductSellDailyDao productSellDailyDao;
	
	@Test
	public void testAInsertProductSellDaily() throws Exception{
		//创建商品日销量统计
		int effectedNum = productSellDailyDao.insertProductSellDaily();
		assertEquals(3,effectedNum);
	}
	
	@Test
	@Ignore
	public void testBQueryProductSellDaily() throws Exception{
		ProductSellDaily productSellDaily = new ProductSellDaily();
		Shop shop = new Shop();
		shop.setShopId(1L);
		productSellDaily.setShop(shop);
		List<ProductSellDaily> productSellDailyList = productSellDailyDao.queryProductSellDailyList(productSellDaily, null, null);
		assertEquals(2,productSellDailyList.size());
	}
}
