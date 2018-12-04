package com.fighting.schoolo2o.service;

import java.io.InputStream;

import com.fighting.schoolo2o.dto.ShopExecution;
import com.fighting.schoolo2o.entity.Shop;
import com.fighting.schoolo2o.exceptions.ShopOperationException;

public interface ShopService {
	ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationException;
}
