package com.fighting.schoolo2o.web.shopadmin;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 主要用来解析路由并转发到相应的html中
 * 
 * @author Fighting_Chen
 *
 */
@Controller
@RequestMapping(value = "/shopadmin", method = { RequestMethod.GET })
public class ShopAdminController {
	@RequestMapping(value = "/shopoperation")
	public String shopOperation() {
		// 转发至店铺注册/编辑页面
		return "shop/shopoperation";
	}
	
	@RequestMapping(value = "/shoplist")
	public String shopList() {
		// 转发至店铺注册/编辑页面
		return "shop/shoplist";
	}
	
	@RequestMapping(value = "/shopdetail")
	public String shopDetail() {
		// 转发至店铺详情页面
		return "shop/shopdetail";
	}
	
	@RequestMapping(value = "/shopmanagement")
	public String shopManagement(){
		// 转发至店铺注册/编辑页面
		return "shop/shopmanagement";
	}
	
	@RequestMapping(value = "/productcategorymanagement")
	public String productCategoryManage(){
		// 转发至店铺注册/编辑页面
		return "shop/productcategorymanagement";
	}

}
