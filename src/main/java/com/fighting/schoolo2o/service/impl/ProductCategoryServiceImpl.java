package com.fighting.schoolo2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fighting.schoolo2o.dao.ProductCategoryDao;
import com.fighting.schoolo2o.dao.ProductDao;
import com.fighting.schoolo2o.dto.ProductCategoryExecution;
import com.fighting.schoolo2o.entity.ProductCategory;
import com.fighting.schoolo2o.enums.ProductCategoryStateEnum;
import com.fighting.schoolo2o.exceptions.ProductCategoryOperationException;
import com.fighting.schoolo2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Autowired
	private ProductDao productDao;

	@Override
	public List<ProductCategory> getProductCategoryList(long shopId) {
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// 将改ProductCategoryId下面对应的商品的ProductCategoryId置为null
		try {
		int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
		if(effectedNum < 0) {
			throw new ProductCategoryOperationException("商品类别更新失败");
		}
		}catch(Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:" + e.getMessage());
		}
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectedNum <= 0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategory error:" + e.getMessage());
		}
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		try {
			int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
			if (effectedNum <= 0) {
				throw new ProductCategoryOperationException("商品类别新增失败");
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("batchAddProductCategory error:" + e.getMessage());
		}
	}

}
