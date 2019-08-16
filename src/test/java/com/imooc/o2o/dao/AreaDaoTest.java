package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.entity.Area;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AreaDaoTest {
	@Autowired
	private AreaDao areaDao;

	@Test
	public void testQueryArea() {
		List<Area> areaList = areaDao.queryArea();
		// 判断列表中的长度
		assertEquals(2, areaList.size());
	}
}
