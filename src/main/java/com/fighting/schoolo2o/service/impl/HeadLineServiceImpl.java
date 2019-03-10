package com.fighting.schoolo2o.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.schoolo2o.cache.JedisUtil;
import com.fighting.schoolo2o.dao.HeadLineDao;
import com.fighting.schoolo2o.entity.Area;
import com.fighting.schoolo2o.entity.HeadLine;
import com.fighting.schoolo2o.exceptions.AreaOperationException;
import com.fighting.schoolo2o.exceptions.HeadLineOperationException;
import com.fighting.schoolo2o.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService {

	@Autowired
	private HeadLineDao headLineDao;
	
	@Autowired
	private JedisUtil.Keys jedisKeys;
	
	@Autowired
	private JedisUtil.Strings jedisStrings;
	
	private static String HLLISTKEY = "headlinelist";
	
	private static Logger logger = LoggerFactory.getLogger(HeadLineServiceImpl.class);
	
	@Override
	@Transactional
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) {
		//定义redis的key前缀
		String key = HLLISTKEY;
		//定义接收对象
		List<HeadLine> headLineList = null;
		//定义jackson数据转换操作类
		ObjectMapper mapper = new ObjectMapper();
		//拼接出redis的key
		if(headLineCondition != null && headLineCondition.getEnableStatus() != null) {
			key = key + "_" + headLineCondition.getEnableStatus();
		}
		if(!jedisKeys.exists(key)) {
			headLineList = headLineDao.queryHeadLine(headLineCondition);
			String jsonString = null;
			try {
				jsonString = mapper.writeValueAsString(headLineList);				
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
			jedisStrings.set(key, jsonString);	
		}else {
			String jsonString = jedisStrings.get(key);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
			try {
				headLineList = mapper.readValue(jsonString, javaType);
			} catch (JsonParseException e) {
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			} catch (JsonMappingException e) {
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
				throw new HeadLineOperationException(e.getMessage());
			}
		}
		return headLineList;
	}

}
