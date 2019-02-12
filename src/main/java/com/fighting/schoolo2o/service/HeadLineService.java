package com.fighting.schoolo2o.service;

import java.io.IOException;
import java.util.List;

import com.fighting.schoolo2o.entity.HeadLine;

public interface HeadLineService {
	
	public static final String HLLISTKEY = "headlinelist";
	
	/**
	 * 根据传入的条件返回指定的头条列表
	 * 
	 * @param headLineCondition
	 * @return
	 * @throws HeadLineOperationException
	 */
	List<HeadLine> getHeadLineList(HeadLine headLineCondition); 
}
