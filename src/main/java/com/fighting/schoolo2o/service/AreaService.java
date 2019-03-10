package com.fighting.schoolo2o.service;

import java.util.List;

import com.fighting.schoolo2o.entity.Area;

public interface AreaService {
	public static final String AREALISTKEY = "arealist";

	List<Area> getAreaList();
}
