package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.HeadLine;

public interface HeadLineDao {
	/**
	 * 根据传入的头条查询条件
	 * 
	 * @return
	 */
	List<HeadLine> queryHeadLine(@Param("headLineCondition") HeadLine headLineCondition);

	/**
	 * 根据头条id返回唯一头条信息
	 * 
	 * @param lineId
	 * @return
	 */
	HeadLine queryHeadLineById(long lineId);
}
