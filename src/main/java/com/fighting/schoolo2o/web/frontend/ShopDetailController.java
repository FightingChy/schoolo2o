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

import com.fighting.schoolo2o.dto.ProductExecution;
import com.fighting.schoolo2o.entity.Product;
import com.fighting.schoolo2o.entity.ProductCategory;
import com.fighting.schoolo2o.entity.Shop;
import com.fighting.schoolo2o.service.ProductCategoryService;
import com.fighting.schoolo2o.service.ProductService;
import com.fighting.schoolo2o.service.ShopService;
import com.fighting.schoolo2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ProductService productService;
		
	@RequestMapping(value="/getshopdetail", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> shopDetail(HttpServletRequest request){
		Map<String, Object> resultModel = new HashMap<String, Object>();
		Shop shop = null;
		List<ProductCategory> productCategoryList = null;
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		shop = shopService.getByShopId(shopId);
		if(shopId > 0) {
			productCategoryList = productCategoryService.getProductCategoryList(shopId);
			resultModel.put("success", true);
			resultModel.put("shop", shop);
			resultModel.put("productCategoryList", productCategoryList);
		}else {
			resultModel.put("success", false);
			resultModel.put("errMsg", "empty shopId");
		}
		return resultModel;
	}
	
	@RequestMapping(value="/getproductlistbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductListByShop(HttpServletRequest request){			
		Map<String, Object> resultModel = new HashMap<String, Object>();
		ProductExecution pe = null;
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		long productCategory = HttpServletRequestUtil.getLong(request, "productCategoryId");
		Product productCondition = getProductCondition(shopId, productCategory);;		
		if(shopId > 0 && pageIndex > 0 && pageSize > 0) {
			pe = productService.getProductList(productCondition, pageIndex, pageSize);
			resultModel.put("success", true);
			resultModel.put("productList", pe.getProductList());
			resultModel.put("count", pe.getCount());
		}else {
			resultModel.put("success", false);
			resultModel.put("errMsg", "店铺Id等信息不能为空");
		}
		return resultModel;
	}
	
	private Product getProductCondition(long shopId, long productCategoryId) {
		Product product = new Product();
		if(shopId > 0) {
			Shop shop = new Shop();
			shop.setShopId(shopId);
			product.setShop(shop);
		}
		if(productCategoryId > 0) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			product.setProductCategory(productCategory);
		}
		return product;
	}
}
