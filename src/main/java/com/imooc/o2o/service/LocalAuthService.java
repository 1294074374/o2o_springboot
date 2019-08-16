package com.imooc.o2o.service;

import java.util.Date;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.exception.LocalAuthOperationException;

public interface LocalAuthService {

	/**
	 * 根据账号密码返回登陆对象
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth getLocalAuthByUserNameAndPwd(String userName, String password);

	/**
	 * 根据userId 查找账号用户
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 * 绑定微信  添加平台账号
	 * @param localAuth
	 * @return
	 * @throws RuntimeException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

	/***
	 * 修改密码
	 * @param userId
	 * @param userName
	 * @param password
	 * @param newPassword
	 * @return
	 * @throws LocalAuthOperationException 
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String userName, String password, String newPassword,Date lastEditTime) throws LocalAuthOperationException;
}
