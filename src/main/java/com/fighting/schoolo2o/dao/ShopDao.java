package com.fighting.schoolo2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fighting.schoolo2o.entity.Shop;

public interface ShopDao {
	
	/**
	 * 
	 * @param shopCondition
	 * @param rowIndex 从第几行开始取数据
	 * @param pageSize 返回的条数
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,  
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	/**
	 * 返回queryList总数
	 * @param shopCondition
	 * @return
	 */
	int queryShopCount(@Param("shopCondition")Shop shopCondition);
	
	/**
	 * 通过shopId查询Shop
	 * @param shopId
	 * @return Shop
	 */
	Shop queryByShopId(long shopId);
	
	/**
	 * 新增店铺
	 * @param shop
	 * @return
	 */
	int insertShop(Shop shop);
	
	/**
	 * 更新店铺信息
	 * @param shop
	 * @return
	 */
	int updateShop(Shop shop);
	
}
