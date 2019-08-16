package com.imooc.o2o.service;

public interface CacheService {

	/**
	 * 依据key浅醉删除匹配该模式下的多有key_value，如传入：shopcategory；则shopcategory打头的key_value都会被清空
	 * 
	 * @param keyPrefix
	 */
	void removeFromCache(String keyPrefix);
}
