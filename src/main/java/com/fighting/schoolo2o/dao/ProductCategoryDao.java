package com.fighting.schoolo2o.dao;

import java.util.List;

import com.fighting.schoolo2o.entity.ProductCategory;

public interface ProductCategoryDao {
	
	/**
	 * 通过shop id查询店铺商品类别
	 * @param shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> queryProductCategoryList(Long shopId);
}
