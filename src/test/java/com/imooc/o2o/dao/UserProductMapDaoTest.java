package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserProductMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserProductMapDaoTest {
	@Autowired
	private UserProductMapDao userProductMapDao;

	@Test
	@Ignore
	public void testAInsertUserProductMap() throws Exception {
		UserProductMap userProductMap = new UserProductMap();
		PersonInfo customer = new PersonInfo();
		customer.setUserId(1L);
		userProductMap.setUser(customer);
		userProductMap.setOperator(customer);
		Product product = new Product();
		product.setProductId(1L);
		Shop shop = new Shop();
		shop.setShopId(20L);
		userProductMap.setCreateTime(new Date());
		int effectedNum = userProductMapDao.insertUserProductMap(userProductMap);
		assertEquals(1, effectedNum);
		UserProductMap userProductMap2 = new UserProductMap();
		PersonInfo customer2 = new PersonInfo();
		customer2.setUserId(1L);
		userProductMap2.setUser(customer2);
		userProductMap2.setOperator(customer2);
		Product product2 = new Product();
		product2.setProductId(1L);
		Shop shop2 = new Shop();
		shop2.setShopId(20L);
		userProductMap2.setCreateTime(new Date());
		int effectedNum2 = userProductMapDao.insertUserProductMap(userProductMap2);
		assertEquals(1, effectedNum2);
	}

	@Test
	@Ignore
	public void testBQueryUserProductMapList() throws Exception {
		UserProductMap userProductMap = new UserProductMap();

		List<UserProductMap> userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 3);
		assertEquals(2, userProductMapList.size());
		int count = userProductMapDao.queryUserProductMapCount(userProductMap);
		assertEquals(2, count);
		userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 3);
		assertEquals(2, userProductMapList.size());
		count = userProductMapDao.queryUserProductMapCount(userProductMap);
		assertEquals(2, count);
		userProductMapList = userProductMapDao.queryUserProductMapList(userProductMap, 0, 3);
		assertEquals(1, userProductMapList.size());
		count = userProductMapDao.queryUserProductMapCount(userProductMap);
		assertEquals(1, count);
	}
}
