package com.fighting.schoolo2o.service;

import java.util.List;

import com.fighting.schoolo2o.entity.ShopCategory;

public interface ShopCategoryService {
	
	List<ShopCategory> queryShopCategory( ShopCategory shopCategoryCondition);
}
