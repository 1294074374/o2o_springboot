package com.imooc.o2o.service;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthServiceTest {
	@Autowired
	private WechatAuthService wechatAuthService;

	@Test
	@Ignore
	public void testGetWechatAuthByOpenId() {
		String openId = "ss";
		WechatAuth wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
		System.out.println(wechatAuth.getOpenId().toString());
	}

	@Test
	public void testRegiest() {
		PersonInfo personInfo = new PersonInfo();
		WechatAuth wechatAuth = new WechatAuth();
		String openId="dafahizhfdhaih";
		// personInfo.setUserId(1L);
		personInfo.setCreateTime(new Date());
		personInfo.setName("测试一下");
		personInfo.setUserType(1);
		wechatAuth.setPersionInfo(personInfo);
		wechatAuth.setOpenId("测试一下");
		wechatAuth.setCreateTime(new Date());
		WechatAuthExecution wechatAuthExecution = wechatAuthService.regiest(wechatAuth);
		wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
		System.out.println(wechatAuthExecution.getState());
		System.out.println(wechatAuthExecution.getStateInfo());

	}

}
