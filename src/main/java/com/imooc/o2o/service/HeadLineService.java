package com.imooc.o2o.service;

import java.io.IOException;
import java.util.List;

import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.exception.HeadLineOperationException;

public interface HeadLineService {
	public static String HLLISTKEY = "headlinelist";

	/***
	 * 根据传入的条件返回指定的头条列表
	 * 
	 * @param headLineCondition
	 * @return
	 * @throws HeadLineOperationException
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException, HeadLineOperationException;
}
