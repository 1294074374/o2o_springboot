package com.imooc.o2o.service;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.UserProductMap;

public interface UserProductMapService {
	/**
	 * 
	 * @param shopId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	UserProductMapExecution listUserProductMap(
			UserProductMap userProductCondition, Integer pageIndex,
			Integer pageSize);

	/**
	 * 
	 * @param userProductMap
	 * @return
	 * @throws RuntimeException
	 */
	UserProductMapExecution addUserProductMap(UserProductMap userProductMap)
			throws RuntimeException;

}
