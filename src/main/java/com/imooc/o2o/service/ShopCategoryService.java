package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.exception.ShopCategoryOperationException;

public interface ShopCategoryService {
	public static String SCLISTKEY = "shopcategorylist";

	/***
	 * 根据查询条件获取商品类别列表
	 * 
	 * @param shopCategoryCondition
	 * @return
	 * @throws ShopCategoryOperationException
	 */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition) throws ShopCategoryOperationException;
}
