package com.fighting.schoolo2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/frontend", method = { RequestMethod.GET })
public class FrontendController {
	
	@RequestMapping("/index")
	private String index() {
		return "frontend/index";
	}
	
	@RequestMapping("/shoplist")
	private String shopList() {
		return "frontend/shoplist";
	}
	
	@RequestMapping("/shopdetail")
	private String shopDetail() {
		return "frontend/shopdetail";
	}
	
	@RequestMapping("/productDetail")
	private String productDetail() {
		return "frontend/productdetail";
	}
	
}
