package com.fighting.schoolo2o.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fighting.schoolo2o.BaseTest;
import com.fighting.schoolo2o.entity.ProductCategory;


public class ProductCategoryDaoTest extends BaseTest {

	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	public void testQueryByShopId() throws Exception{
		long shopId = 1L;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(8L);
		System.out.println("该店铺自定义类别数为：" + productCategoryList.size());
		
	}
}
