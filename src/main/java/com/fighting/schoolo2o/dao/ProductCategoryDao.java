package com.fighting.schoolo2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fighting.schoolo2o.entity.ProductCategory;

public interface ProductCategoryDao {
	
	/**
	 * 通过shop id查询店铺商品类别
	 * @param shopId
	 * @return List<ProductCategory>
	 */
	List<ProductCategory> queryProductCategoryList(Long shopId);
	
	/**
	 * 通过productCategoryId和shopId删除商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return 影响的行数
	 */
	
	int deleteProductCategory(@Param("productCategoryId")long productCategoryId, @Param("shopId") long shopId);
	
	/**
	 * 批量增加商品类别
	 * @param productCategoryList
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
}
