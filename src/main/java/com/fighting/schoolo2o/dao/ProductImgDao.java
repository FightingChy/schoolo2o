package com.fighting.schoolo2o.dao;

import java.util.List;

import com.fighting.schoolo2o.entity.ProductImg;

public interface ProductImgDao {

	/**
	 * 根据商品Id查询商品的详情图片
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long productId);
	
	/**
	 * 批量增加商品图片
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);
	
	/**
	 * 删除商品详情图片
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);
	
}
