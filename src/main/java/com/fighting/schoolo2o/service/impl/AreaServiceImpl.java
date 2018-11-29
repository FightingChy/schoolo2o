package com.fighting.schoolo2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fighting.schoolo2o.dao.AreaDao;
import com.fighting.schoolo2o.entity.Area;
import com.fighting.schoolo2o.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService {

	@Autowired
	public AreaDao areaDao;
	@Override
	public List<Area> getAreaList() {
		// TODO Auto-generated method stub
		return areaDao.queryArea();
	}

}
