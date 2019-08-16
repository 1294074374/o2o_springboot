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

import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserAwardMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAwardMapDaoTest {
	@Autowired
	private UserAwardMapDao userAwardMapDao;

	@Test
	public void testAInsertUserAwardMap() throws Exception {
		UserAwardMap userAwardMap = new UserAwardMap();
		PersonInfo customer = new PersonInfo();
		customer.setUserId(1L);
		userAwardMap.setUser(customer);
		userAwardMap.setOperator(customer);
		Award award = new Award();
		award.setAwardId(1L);
		userAwardMap.setAward(award);
		Shop shop = new Shop();
		shop.setShopId(1L);
		userAwardMap.setShop(shop);
		userAwardMap.setCreateTime(new Date());
		userAwardMap.setUsedStatus(1);
		int effectedNum = userAwardMapDao.insertUserAwardMap(userAwardMap);
		assertEquals(1, effectedNum);
	}

	@Test
	@Ignore
	public void testBQueryUserAwardMapList() throws Exception {
		UserAwardMap userAwardMap = new UserAwardMap();

		List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
		assertEquals(2, userAwardMapList.size());
		int count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
		assertEquals(2, count);
		userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
		assertEquals(2, userAwardMapList.size());
		count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
		assertEquals(2, count);
		userAwardMapList = userAwardMapDao.queryUserAwardMapList(userAwardMap, 0, 3);
		assertEquals(1, userAwardMapList.size());
		count = userAwardMapDao.queryUserAwardMapCount(userAwardMap);
		assertEquals(1, count);
	}
}
