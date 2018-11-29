package com.fighting.schoolo2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fighting.schoolo2o.BaseTest;
import com.fighting.schoolo2o.entity.Area;
import com.fighting.schoolo2o.entity.PersonInfo;
import com.fighting.schoolo2o.entity.Shop;
import com.fighting.schoolo2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest {
	
	@Autowired
	public ShopDao shopDao;
	
	@Test
	@Ignore
	public void testInsertShopDao()
	{
		Shop shop = new Shop();
		shop.setShopName("香飘飘奶茶店");
		shop.setShopDesc("香飘飘奶茶店");
		shop.setPhone("18295268283");
		Area area = new Area();
		area.setAreaId(1);
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		
		shop.setArea(area);
		shop.setOwner(personInfo);
		shop.setPriority(5);
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(1L);
		shop.setShopCategory(shopCategory);
		
		int result = shopDao.insertShop(shop);
		assertEquals(1,result);
	}
	@Test
	public void testUpdateShopDao()
	{
		Shop shop = new Shop();
		shop.setShopId(1L);
		shop.setShopName("香八里奶茶店");
		shop.setShopDesc("香八里奶茶店");
		shop.setPhone("18295268286");
		int result = shopDao.updateShop(shop);
		assertEquals(1,result);
	}
}
