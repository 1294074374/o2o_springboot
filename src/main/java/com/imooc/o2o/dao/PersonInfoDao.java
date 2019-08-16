package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;

public interface PersonInfoDao {

	/**
	 * 通过personId查找用户
	 * 
	 * @param personId
	 * @return
	 */
	PersonInfo queryPersonInfoById(long userId);

	/**
	 * 添加用户信息
	 * 
	 * @param personInfo
	 * @return
	 */
	int insertPersonInfo(PersonInfo personInfo);
}
