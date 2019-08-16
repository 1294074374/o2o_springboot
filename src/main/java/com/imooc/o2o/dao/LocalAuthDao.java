package com.imooc.o2o.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.LocalAuth;

public interface LocalAuthDao {

	/***
	 * 用户登陆时通过账号和密码比对后台数据库
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	LocalAuth queryLocalAuthByUserNameAndPwd(@Param("userName") String userName, @Param("password") String password);
	
	/***
	 * 通过id查找localAuth	
	 * @param userId
	 * @return
	 */
	LocalAuth queryLocalAuthById(@Param("userId") long userId);
	
	/***
	 * 添加平台账户
	 * @param localAuth
	 * @return
	 */
	int insertLocalAuth(LocalAuth localAuth);
	
	/**
	 * 通过 userId  userName  password 修改密码
	 * @param localAuth
	 * @return
	 */
	int updateLocalAuth(@Param("userId") Long userId,
			@Param("userName") String userName,
			@Param("password") String password,
			@Param("newPassword") String newPassword,
			@Param("lastEditTime") Date lastEditTime);
	
	
}
