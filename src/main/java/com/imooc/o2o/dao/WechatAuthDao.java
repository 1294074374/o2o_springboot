package com.imooc.o2o.dao;

import com.imooc.o2o.entity.WechatAuth;

public interface WechatAuthDao {

	/***
	 * 通过OpenId查找对应本平台的微信账号信息
	 * 
	 * @param OpenId
	 * @return
	 */
	WechatAuth queryWechatAuthByOpenId(String openId);

	/**
	 * 添加对应本平台的微信账号信息
	 * 
	 * @param wechatAuth
	 * @return
	 */
	int insertWechatAuth(WechatAuth wechatAuth);
}
