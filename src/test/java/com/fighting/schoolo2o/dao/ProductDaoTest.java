package com.fighting.schoolo2o.dao;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fighting.schoolo2o.BaseTest;
import com.fighting.schoolo2o.entity.Product;
import com.fighting.schoolo2o.entity.ProductCategory;
import com.fighting.schoolo2o.entity.Shop;

public class ProductDaoTest extends BaseTest {
	@Autowired
	public ProductDao productDao;
	
	@Test
	@Ignore
	public void testQueryProductListAndCount() {
		Product productCondition = new Product();
		Shop shop = new Shop();
		shop.setShopId(8L);
		productCondition.setProductName("柠檬");
		productCondition.setShop(shop);
		
		List<Product> productList = productDao.queryProductList(productCondition, 0, 100);
		int count = productDao.queryProductCount(productCondition);
		System.out.println("总数：" + productList.size());
		System.out.println("店铺总数：" + count);
		
	}
	
	@Test
	public void testUpdateProduct() {
		Product product = new Product();
		ProductCategory productCategory = new ProductCategory();
		Shop shop = new Shop();
		shop.setShopId(8L);
		product.setShop(shop);
		product.setProductId(1L);
		productCategory.setProductCategoryId(3L);
		product.setProductCategory(productCategory);
		product.setProductDesc("怀远最最好喝的柠檬水"); 
		product.setLastEditTime(new Date());
		product.setNormalPrice("13");
		product.setPromotionPrice("7");
		productDao.updateProduct(product);
	}
}
