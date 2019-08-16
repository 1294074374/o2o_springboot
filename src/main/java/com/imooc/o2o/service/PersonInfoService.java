package com.imooc.o2o.service;

import com.imooc.o2o.entity.PersonInfo;

public interface PersonInfoService {
	
	/**
	 * 通过ID 查找用户
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoById(Long userId);
	
	
}
