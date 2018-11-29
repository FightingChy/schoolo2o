package com.fighting.schoolo2o.service;

import java.io.File;

import com.fighting.schoolo2o.dto.ShopExecution;
import com.fighting.schoolo2o.entity.Shop;

public interface ShopService {
	ShopExecution addShop(Shop shop, File shopImg);
}
