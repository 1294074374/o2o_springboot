package com.imooc.o2o.service;

import java.util.List;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.exception.ProductCategoryOperationException;

public interface ProductCategoryService {
	/***
	 * 通过shop id 查询店铺商品类别
	 * 
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(long shopId);

	/***
	 * 批量添加商品类别
	 * 
	 * @param productCategoryList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException;

	/***
	 * 将此类别下的商品里的类别ID置为空，再删除该商品类别
	 * 置空是由于 该商品类别下可能有商品
	 * 
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException;
}
