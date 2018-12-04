package com.fighting.schoolo2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fighting.schoolo2o.BaseTest;
import com.fighting.schoolo2o.dto.ShopExecution;
import com.fighting.schoolo2o.entity.Area;
import com.fighting.schoolo2o.entity.PersonInfo;
import com.fighting.schoolo2o.entity.Shop;
import com.fighting.schoolo2o.entity.ShopCategory;
import com.fighting.schoolo2o.enums.ShopStateEnum;

public class ShopServiceTest extends BaseTest {

	@Autowired
	private ShopService shopService;
	
	@Test
	public void testAddShop() throws FileNotFoundException {
		Shop shop = new Shop();
		shop.setShopName("香九里奶茶店");
		shop.setShopDesc("香九里奶茶店");
		shop.setPhone("18295268383");
		Area area = new Area();
		area.setAreaId(1);
		PersonInfo personInfo = new PersonInfo();
		personInfo.setUserId(1L);
		
		shop.setArea(area);
		shop.setOwner(personInfo);
		shop.setPriority(10);
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(1L);
		shop.setShopCategory(shopCategory);
		
		File shopImg = new File("D:/xiaohuangren.jpg");
		InputStream is = new FileInputStream(shopImg);
		ShopExecution se = shopService.addShop(shop, is, shopImg.getName());
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	} 
}
