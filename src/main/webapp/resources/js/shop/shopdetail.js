$(function() {
	// 从URL里获取shopId参数的值
	var shopId = getQueryString('shopId');
	// 编辑店铺前需要获取店铺信息，这里为获取当前店铺信息的URL
	var shopInfoUrl = "/schoolo2o/shopadmin/getshopdetailbyid?shopId=" + shopId;
	getShopInfo(shopId);
	// 通过店铺Id获取店铺信息
	function getShopInfo(shopId) {
		$.getJSON(shopInfoUrl, function(data) {
			if (data.success) {
				// 若访问成功，则依据后台传递过来的店铺信息为表单元素赋值
				var shop = data.shop;
				$('#shop-name').text(shop.shopName);
				$('#shop-category').text(shop.shopCategory.shopCategoryName);
				$('#shop-addr').text(shop.shopAddr);
				$('#shop-phone').text(shop.phone);
				$('#shop-desc').text(shop.shopDesc);
				$('#area').text(shop.area.areaName);
				//$('#shop-img').attr("src", '/schoolo2o/image/showimg?path=' +encodeURIComponent(data.shopImagePath));
				$('#shop-img').attr("src", data.shopImagePath);
			}
		});
	}

})