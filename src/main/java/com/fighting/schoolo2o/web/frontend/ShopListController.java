package com.fighting.schoolo2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fighting.schoolo2o.dto.ShopExecution;
import com.fighting.schoolo2o.entity.Area;
import com.fighting.schoolo2o.entity.Shop;
import com.fighting.schoolo2o.entity.ShopCategory;
import com.fighting.schoolo2o.service.AreaService;
import com.fighting.schoolo2o.service.ShopCategoryService;
import com.fighting.schoolo2o.service.ShopService;
import com.fighting.schoolo2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/frontend")
public class ShopListController {

	@Autowired
	private AreaService areaService;

	@Autowired
	private ShopCategoryService shopCategoryService;

	@Autowired
	private ShopService shopService;

	@RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
		Map<String, Object> modelResult = new HashMap<String, Object>();
		Long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		if (parentId != -1) {
			// 如果parentId存在，则取出该一级ShopCategory下的二级ShopCategory列表
			try {
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.queryShopCategory(shopCategoryCondition);
			} catch (Exception e) {
				modelResult.put("success", false);
				modelResult.put("errMsg", e.getMessage());
				return modelResult;
			}
		} else {
			try {
				// 如果parentId不存在，则取出所有一级ShopCategory(用户在首页选择的全部商品)
				shopCategoryList = shopCategoryService.queryShopCategory(null);
			} catch (Exception e) {
				modelResult.put("success", false);
				modelResult.put("errMsg", e.getMessage());
				return modelResult;
			}
		}
		modelResult.put("shopCategoryList", shopCategoryList);
		List<Area> areaList = null;
		try {
			areaList = areaService.getAreaList();
			modelResult.put("areaList", areaList);
			modelResult.put("success", true);
			return modelResult;
		} catch (Exception e) {
			modelResult.put("success", false);
			modelResult.put("errMsg", e.getMessage());
		}
		return modelResult;
	}

	/**
	 * 获取指定查询条件下的店铺列表
	 * 
	 * @param request
	 * @return
	 */

	@RequestMapping(value = "/listshops", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelResult = new HashMap<String, Object>();
		// 获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		// 获取一页需要显示的数据条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		if (pageIndex > -1 && pageSize > -1) {
			// 试着获取一级类别Id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			// 试着获取二级类别Id
			long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
			// 试着获取区域Id
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			// 试着获取模糊查询的名字
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			Shop shopCondition = GetSearchShopCondition(parentId, shopCategoryId, areaId, shopName);
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelResult.put("shopList", se.getShopList());
			modelResult.put("count", se.getCount());
			modelResult.put("success", true);		
		} else {
			modelResult.put("success", false);
			modelResult.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelResult;
	}

	/**
	 * 根据查询条件构造查询条件
	 * @param parentId
	 * @param shopCategoryId
	 * @param areaId
	 * @param shopName
	 * @return
	 */
	private Shop GetSearchShopCondition(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		if(parentId != -1L) {
			ShopCategory childCategory = new ShopCategory();
			ShopCategory parentCategory = new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);
		}
		if(shopCategoryId != -1L) {
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		if(areaId != -1) {
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		if(shopName != null) {
			shopCondition.setShopName(shopName);
		}
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}

}
