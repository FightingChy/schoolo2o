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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fighting.schoolo2o.dto.ImageHolder;
import com.fighting.schoolo2o.dto.ProductExecution;
import com.fighting.schoolo2o.dto.Result;
import com.fighting.schoolo2o.entity.Product;
import com.fighting.schoolo2o.entity.ProductCategory;
import com.fighting.schoolo2o.entity.Shop;
import com.fighting.schoolo2o.enums.ProductStateEnum;
import com.fighting.schoolo2o.exceptions.ProductOperationException;
import com.fighting.schoolo2o.service.ProductCategoryService;
import com.fighting.schoolo2o.service.ProductService;
import com.fighting.schoolo2o.util.CodeUtil;
import com.fighting.schoolo2o.util.HttpServletRequestUtil;
import com.fighting.schoolo2o.util.PathUtil;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductCategoryService productCategoryService;

	// 支持上传商品详情图的最大数量
	private static final int IMAGEMAXCOUNT = 6;

	@RequestMapping(value = "/getproductlistbyshop", method = RequestMethod.GET)
	@ResponseBody
	private Result<List<Product>> getProductList(HttpServletRequest request) {
		Result<List<Product>> result = null;
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		Product productCondition = new Product();
		productCondition.setShop(currentShop);
		ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
		if(pe.getProductList().size() > 0) {
			result = new Result<List<Product>>(true,pe.getProductList());
		}else {
			result = new Result<List<Product>>(false, new ArrayList<Product>());
		}	
		return result;
	}

	@RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductById(Long productId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productId == null || productId <= 0) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "商品ID为空");
			return modelMap;
		}
		Product product = productService.getProductById(productId);
		if (product != null && product.getProductId() > 0) {
			// 获取该店铺下的商品类别列表
			List<ProductCategory> productCategoryList = productCategoryService
					.getProductCategoryList(product.getShop().getShopId());
			modelMap.put("success", true);
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "没有找到商品");
		}
		return modelMap;
	}

	@RequestMapping(value = "/addproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 1.接收并转化相应的参数，包括店铺信息以及图片信息
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		try {
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImgList);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 若Product信息，缩略图以及详情图列表为非空，则开始进行商品添加操作
		if (product != null && thumbnail != null && productImgList.size() > 0) {
			try {
				// 从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				// 执行添加操作
				ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
				if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}

		return modelMap;
	}
	
	@RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入了错误的验证码");
			return modelMap;
		}
		// 1.接收并转化相应的参数，包括店铺信息以及图片信息
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		try {
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
		try {
			if (multipartResolver.isMultipart(request)) {
				thumbnail = handleImage(request, thumbnail, productImgList);
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 若Product信息，缩略图以及详情图列表为非空，则开始进行商品添加操作
		if (product != null && thumbnail != null && productImgList.size() > 0) {
			try {		
				// 从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				product.setShop(currentShop);
				// 执行添加操作
				ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
				if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}

		return modelMap;
	}
	
	private ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws IOException {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multiRequest.getFile("thumbnail");
		if (thumbnailFile != null) {
			thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
		}
//		List<MultipartFile> detailImgList = (List<MultipartFile>) multiRequest.getFiles("detailImgList");
//
//		for (MultipartFile file : detailImgList) {
//			ImageHolder productImg = new ImageHolder(file.getOriginalFilename(), file.getInputStream());
//			productImgList.add(productImg);
//		}
		// 取出详情图列表并构建List<ImageHolder>列表对象，最多支持六张图片上传
		for (int i = 0; i < IMAGEMAXCOUNT; i++) {
			CommonsMultipartFile productImgFile = (CommonsMultipartFile) multiRequest.getFile("productImg" + i);
			if (productImgFile != null) {
				// 若取出的第i个详情图片文件流不为空，则将其加入详情图列表
				ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
						productImgFile.getInputStream());
				productImgList.add(productImg);
			} else {
				// 若取出的第i个详情图片文件流为空，则终止循环
				break;
			}
		}

		return thumbnail;
	}
}
