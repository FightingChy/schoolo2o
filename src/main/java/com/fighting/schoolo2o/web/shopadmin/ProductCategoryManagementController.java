package com.fighting.schoolo2o.web.shopadmin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fighting.schoolo2o.dto.Result;
import com.fighting.schoolo2o.entity.ProductCategory;
import com.fighting.schoolo2o.entity.Shop;
import com.fighting.schoolo2o.enums.ProductCategoryStateEnum;
import com.fighting.schoolo2o.service.ProductCategoryService;

@Controller
@RequestMapping(value="/shopadmin")
public class ProductCategoryManagementController {
	
	@Autowired
	private ProductCategoryService productCategory;
	
	@RequestMapping(value="/getproductcategorylist", method=RequestMethod.GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){		
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		List<ProductCategory> list = null;
		if(currentShop != null && currentShop.getShopId() > 0) {
			list = productCategory.getProductCategoryList(currentShop.getShopId());
			return new Result<List<ProductCategory>>(true, list);
		}else {
			ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
			return new Result<List<ProductCategory>>(false, ps.getState(), ps.getStateInfo());
		}
	}
	
}
