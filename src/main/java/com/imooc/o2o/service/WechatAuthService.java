package com.imooc.o2o.service;

import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.exception.WechatAuthOperationException;

public interface WechatAuthService {

	/**
	 * 通过openId查找微信用户
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);

	/**
	 * 根据微信用户创造用户
	 * 
	 * @param wechatAuth
	 * @return
	 * @throws WechatAuthOperationException
	 */
	WechatAuthExecution regiest(WechatAuth wechatAuth) throws WechatAuthOperationException;

}
