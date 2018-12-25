package com.fighting.schoolo2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
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
import com.fighting.schoolo2o.util.PathUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
	@Autowired
	private ShopService shopService;

	@Autowired
	private ShopCategoryService shopCategoryService;

	@Autowired
	private AreaService areaService;
	
	@RequestMapping(value="/getshopmanagementinfo", method=RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if(shopId <= 0) {
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			if(currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/schoolo2o/shopadmin/shoplist");
			}else {
				Shop currentShop = (Shop)currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		}else {
			Shop currentShop = shopService.getByShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		user.setName("Fighting_Chen");
		request.getSession().setAttribute("user", user);
		user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			if(se.getState() == ShopStateEnum.INNER_ERROR.getState()) {
				modelMap.put("success", false);
				modelMap.put("errMsg", se.getStateInfo());
			}else {
				modelMap.put("success", true);
				modelMap.put("shopList", se.getShopList());
				modelMap.put("user", user);				
			}
		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("errMsg", ex.getMessage());
		}
		return modelMap;

	}

	@RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception ex) {
				modelMap.put("success", false);
				modelMap.put("errMsg", ex.getMessage());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "emptu shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getshopdetailbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopDetailById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				modelMap.put("shop", shop);
				modelMap.put("shopImagePath", PathUtil.getImgBasePath() + shop.getShopImg());
				modelMap.put("success", true);
			} catch (Exception ex) {
				modelMap.put("success", false);
				modelMap.put("errMsg", ex.getMessage());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}


	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入正确的验证码");
			return modelMap;
		}
		// 1、接受并转化相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
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
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			ShopExecution se;
			try {
				se = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					@SuppressWarnings("unchecked")
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if (shopList == null || shopList.size() == 0) {
						shopList = new ArrayList<Shop>();
					}
					shopList.add(se.getShop());
					request.getSession().setAttribute("shopList", shopList);
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

	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			resultMap.put("success", false);
			resultMap.put("errMsg", "请输入正确的验证码");
			return resultMap;
		}
		// 1、接受并转化相应的参数，包括店铺信息以及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		Shop shop = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("errMsg", e.getMessage());
			return resultMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			resultMap.put("success", false);
			resultMap.put("errMsg", "上传图片不能为空");
			return resultMap;
		}

		// 2、修改店铺信息
		if (shop != null && shop.getShopId() != null) {
			ShopExecution se;
			try {
				if (shopImg == null) {
					se = shopService.modifyShop(shop, null, null);
				} else {
					se = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
				}
				if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
					resultMap.put("success", true);
				} else {
					resultMap.put("success", false);
					resultMap.put("errMsg", se.getStateInfo());
				}
			} catch (ShopOperationException | IOException e) {
				resultMap.put("success", false);
				resultMap.put("errMsg", "请输入店铺信息");
			}
			return resultMap;
		} else {
			resultMap.put("success", false);
			resultMap.put("errMsg", "请输入店铺Id");
			return resultMap;
		}

	}

	@RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<ShopCategory> shopCategoryList = shopCategoryService.queryShopCategory(null);
			List<Area> areaList = areaService.getAreaList();
			resultMap.put("success", true);
			resultMap.put("shopCategoryList", shopCategoryList);
			resultMap.put("areaList", areaList);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("errMsg", e.getMessage());
		}
		return resultMap;
	}

	// private static void inputStreamToFile(InputStream ins, File file) {
	// FileOutputStream oStream = null;
	// try {
	// oStream = new FileOutputStream(file);
	// int bytesRead = 0;
	// byte[] buffer = new byte[1024];
	// while ((bytesRead = ins.read(buffer)) != -1) {
	// oStream.write(buffer, 0, bytesRead);
	// }
	// } catch (Exception e) {
	// throw new RuntimeException("调用inputStreamToFile产生异常：" + e.getMessage());
	// } finally {
	// try {
	// if (oStream != null) {
	// oStream.close();
	// }
	// if (ins != null) {
	// ins.close();
	// }
	// } catch (IOException e) {
	// throw new RuntimeException("inputStreamToFile关闭io产生异常：" + e.getMessage());
	// }
	// }
	//
	// }
}
