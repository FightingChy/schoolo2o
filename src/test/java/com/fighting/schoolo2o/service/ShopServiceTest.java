package com.fighting.schoolo2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fighting.schoolo2o.BaseTest;
import com.fighting.schoolo2o.dto.ImageHolder;
import com.fighting.schoolo2o.dto.ShopExecution;
import com.fighting.schoolo2o.entity.Area;
import com.fighting.schoolo2o.entity.PersonInfo;
import com.fighting.schoolo2o.entity.Shop;
import com.fighting.schoolo2o.entity.ShopCategory;
import com.fighting.schoolo2o.enums.ShopStateEnum;
import com.fighting.schoolo2o.exceptions.ShopOperationException;

public class ShopServiceTest extends BaseTest {

	@Autowired
	private ShopService shopService;
	
	@Test
	public void testGetShopList() {
		Shop shopCondition = new Shop();
		ShopCategory shopCategory = new ShopCategory();
		shopCategory.setShopCategoryId(1L);
		
		Area area = new Area();
		area.setAreaId(1);
		shopCondition.setArea(area);
		ShopExecution se =  shopService.getShopList(shopCondition, 2, 2);
		for(Shop shop : se.getShopList()) {
			System.out.println(shop.getShopId());
		}
		
	}
	
	@Test
	@Ignore
	public void testModifyShop() throws ShopOperationException, FileNotFoundException {
		Shop oldShop = shopService.getByShopId(1L);
		oldShop.setShopDesc("香八里奶茶店的描述");
		File shopImg = new File("D:/timg.jpg");
		InputStream is = new FileInputStream(shopImg);
		ShopExecution se = shopService.modifyShop(oldShop, new ImageHolder(shopImg.getName(), is));
		assertEquals(ShopStateEnum.SUCCESS.getState(), se.getState());
	}
	
	@Test
	@Ignore
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
		
		ShopExecution se = shopService.addShop(shop, new ImageHolder(shopImg.getName(),is));
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	} 
}
