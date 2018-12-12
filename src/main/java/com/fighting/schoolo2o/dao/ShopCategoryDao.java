package com.fighting.schoolo2o.dao;

import java.util.List;

import com.fighting.schoolo2o.entity.ShopCategory;

public interface ShopCategoryDao {

	/**
	 * 根据传入的查询条件返回店铺类别列表
	 * @return
	 */
	public List<ShopCategory> queryShopCategory(ShopCategory shopCategoryCondition);
}
