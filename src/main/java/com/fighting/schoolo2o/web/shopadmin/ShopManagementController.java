package com.fighting.schoolo2o.web.shopadmin;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.schoolo2o.dto.ShopExecution;
import com.fighting.schoolo2o.entity.Area;
import com.fighting.schoolo2o.entity.PersonInfo;
import com.fighting.schoolo2o.entity.Shop;
import com.fighting.schoolo2o.entity.ShopCategory;
import com.fighting.schoolo2o.enums.ShopStateEnum;
import com.fighting.schoolo2o.exceptions.ShopOperationException;
import com.fighting.schoolo2o.service.AreaService;
import com.fighting.schoolo2o.service.ShopCategoryService;
import com.fighting.schoolo2o.service.ShopService;
import com.fighting.schoolo2o.util.CodeUtil;
import com.fighting.schoolo2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShopCategoryService  shopCategoryService;
	
	@Autowired
	private AreaService areaService;

	@RequestMapping(value ="/registershop", method=RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {

		String test = request.getParameter("test");
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", test);
			return modelMap;
		}
			
		// 1、接受并转化相应的参数，包括店铺信息以及图片信息
		//String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		Shop shop = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}

		// 2、注册店铺
		if (shop != null && shopImg != null) {
			PersonInfo owner = new PersonInfo();
			owner.setUserId(1L);
			shop.setOwner(owner);
			ShopExecution se;
			try {
				se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException | IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入店铺信息");
			}			
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
	}
	
	@RequestMapping(value ="/getshopinitinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo(){
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		try{
			List<ShopCategory> shopCategoryList = shopCategoryService.queryShopCategory(null);
			List<Area> areaList = areaService.getAreaList();
			resultMap.put("success", true);
			resultMap.put("shopCategoryList", shopCategoryList);
			resultMap.put("areaList", areaList);
		}catch(Exception e){
			resultMap.put("success", false);
			resultMap.put("errMsg", e.getMessage());			
		}
		return resultMap;		
	}

//	private static void inputStreamToFile(InputStream ins, File file) {
//		FileOutputStream oStream = null;
//		try {
//			oStream = new FileOutputStream(file);
//			int bytesRead = 0;
//			byte[] buffer = new byte[1024];
//			while ((bytesRead = ins.read(buffer)) != -1) {
//				oStream.write(buffer, 0, bytesRead);
//			}
//		} catch (Exception e) {
//			throw new RuntimeException("调用inputStreamToFile产生异常：" + e.getMessage());
//		} finally {
//			try {
//				if (oStream != null) {
//					oStream.close();
//				}
//				if (ins != null) {
//					ins.close();
//				}
//			} catch (IOException e) {
//				throw new RuntimeException("inputStreamToFile关闭io产生异常：" + e.getMessage());
//			}
//		}
//
//	}
}
