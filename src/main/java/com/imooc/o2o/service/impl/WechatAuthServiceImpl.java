package com.imooc.o2o.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dao.WechatAuthDao;
import com.imooc.o2o.dto.WechatAuthExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.exception.WechatAuthOperationException;
import com.imooc.o2o.service.WechatAuthService;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
	private static Logger log = LoggerFactory.getLogger(WechatAuthServiceImpl.class);
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public WechatAuth getWechatAuthByOpenId(String openId) {
		return wechatAuthDao.queryWechatAuthByOpenId(openId);
	}

	@Override
	@Transactional
	public WechatAuthExecution regiest(WechatAuth wechatAuth) throws WechatAuthOperationException {
		// 空值判断
		if (wechatAuth == null || wechatAuth.getOpenId() == null) {
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		}
		// 如果微信账号中的夹着用户信息并且用户Id为空，则认为该用户是第一次使用平台(且通过微信登陆)
		// 则自动创建用户
		if (wechatAuth.getPersionInfo() != null && wechatAuth.getPersionInfo().getUserId() == null) {
			try {
				wechatAuth.getPersionInfo().setCreateTime(new Date());
				wechatAuth.getPersionInfo().setEnableStatus(1);
				PersonInfo personInfo = wechatAuth.getPersionInfo();
				int effectedNum = personInfoDao.insertPersonInfo(personInfo);
				wechatAuth.setPersionInfo(personInfo);
				if (effectedNum <= 0) {
					throw new WechatAuthOperationException("添加用户信息失败");
				}

			} catch (Exception e) {
				log.error("insertPersonInfo error:" + e.toString());
				throw new WechatAuthOperationException("insertPersonInfo error:" + e.getMessage());
			}
		}
		// 创建本平台的微信用户
		try {
			wechatAuth.setCreateTime(new Date());
			int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
			if (effectedNum <= 0) {
				throw new WechatAuthOperationException("添加用户信息失败");
			} else {
				return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			log.error("insertWechatAuth error:" + e.toString());
			throw new WechatAuthOperationException("insertPersonInfo error:" + e.getMessage());
		}
	}

}
