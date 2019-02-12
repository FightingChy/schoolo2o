package com.fighting.schoolo2o.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fighting.schoolo2o.entity.Product;
import com.fighting.schoolo2o.service.ProductService;
import com.fighting.schoolo2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/frontend", method = RequestMethod.GET)
public class ProductDetailController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/listproductdetailpageinfo", method = RequestMethod.GET )
	@ResponseBody
	private Map<String, Object> listProductDetailPageInfo(HttpServletRequest request){
		Map<String, Object> resultModel = new HashMap<String, Object>();
		long productId = HttpServletRequestUtil.getLong(request, "productId");
		Product product = null;
		if(productId != -1) {
			//根据productId获取商品信息，包含商品详情图列表
			product = productService.getProductById(productId);
			resultModel.put("product", product);
			resultModel.put("success", true);
		}else {
			resultModel.put("success", true);
			resultModel.put("errMsg", "empty productId");
		}			
		return resultModel;
	}
	
}
