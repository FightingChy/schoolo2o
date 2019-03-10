package com.fighting.schoolo2o.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fighting.schoolo2o.dao.PersonInfoDao;
import com.fighting.schoolo2o.entity.PersonInfo;
import com.fighting.schoolo2o.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

	@Autowired
	private PersonInfoDao personInfoDao;
	
	@Override
	public PersonInfo getPersonInfoById(Long userId) {
		return personInfoDao.queryPersonInfoById(userId);
	}

}
