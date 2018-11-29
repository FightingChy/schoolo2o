package com.fighting.schoolo2o.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fighting.schoolo2o.BaseTest;
import com.fighting.schoolo2o.entity.Area;
import java.util.*;

public class AreaDaoTest extends BaseTest {

	@Autowired
	public AreaDao areaDao;
	
	@Test
	public void testQueryArea() {
		
		List<Area> areaList = areaDao.queryArea();
		
		assertEquals(2, areaList.size());
	}
}
