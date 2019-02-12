$(function() {
	var loading = false;
	// 分页允许返回的最大条数，超过此数则禁止访问后台
	var maxItems = 999;
	// 一页返回的最大条数
	var pageSize = 1;
	// 获取店铺列表的URL
	var shopDetailUrl = '/schoolo2o/frontend/getshopdetail';
	
	var productListUrl = '/schoolo2o/frontend/getproductlistbyshop';
	// 页码
	var pageNum = 1;
	var shopId = getQueryString('shopId');
	var productName = '';
	var productCategoryId = '';
	// 获取店铺详情及店铺类别信息
	getShopDetailAndProductCategory();
	// 预先加载10条店铺信息
	addItems(pageSize, pageNum);
	function getShopDetailAndProductCategory() {
		var url = shopDetailUrl + '?' + 'shopId=' + shopId;
		$.getJSON(url, function(data) {
			if (data.success) {
				var shop = data.shop;
				$('#shop-cover-pic').attr('src', shop.shopImg);
				$('#shop-update-time').html(
						new Date(shop.lastEditTime).Format("yyyy-MM-dd"));
				$('#shop-name').html(shop.shopName);
				$('#shop-desc').html(shop.shopDesc);
				$('#shop-addr').html(shop.shopAddr);
				$('#shop-phone').html(shop.phone);
				// 获取后台返回过来的店铺类别列表
				var productCategoryList = data.productCategoryList;
				var html = '<option value="">所有类别</option>';
				// 遍历店铺类别列表，拼接出a标签集
				productCategoryList.map(function(item, index) {
					html += '<option value="' + item.productCategoryId + '">'
							+ item.productCategoryName + '</option>';
				});
				// 将标签集添加进area列表里
				$('#product-category-search').html(html);
			}
		})
	}

	function addItems(pageSize, pageIndex) {
		// 生成新条目的HTML
		var html = '';
		// 设置flag
		loading = true;
		var url = productListUrl + "?shopId=" + shopId + "&pageSize=" + pageSize 
				+ '&pageIndex=' + pageIndex + '&productName=' + productName + '&productCategoryId=' + productCategoryId;
		$.getJSON(url, function(data) {
			if (data.success) {
				// 获取当前查询条件下店铺的总数
				maxItems = data.count;
				// 重置加载flag
				loading = false;
				var productList = data.productList;
				productList.map(function(item,index){
					html += '<div class="card"'
						+ 'data-product-id="' + item.productId +'">'
                        + '<div class="card-header">' + item.productName + '</div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">'
                        + '<ul>'     
                        + '<li class="item-content">'           
                        + '<div class="item-media">'              
                        + '<img src="' + item.imgAddr + '" width="44">'
                        + '</div>'
                        + '<div class="item-inner">'           
                        + '<div class="item-subtitle">' + item.productDesc +'</div>'                
                        + '</div></li></ul></div></div>'             
                        + '<div class="card-footer">'       
                        + '<span>' + new Date(item.lastEditTime).Format("yyyy-MM-dd") + '</span>'     
                        + '<span> 5 评论</span>'
                        + '</div></div>'
				});
							
				// 获取目前为止已显示的卡片总数，包含之前已经加载的
				var total = $('.list-div .card').length;
				// 若总数达到跟按照此查询条件列出来的总数一致，则停止后台的加载
				if (total >= maxItems) {
					// 隐藏提示符
					$('.infinite-scroll-preloader').hide();
				} else {
					$('.infinite-scroll-preloader').show();
				}
				// 否则页码加1，继续load出新的店铺
				pageNum += 1;
				// 容器发生改变,如果是js滚动，需要刷新滚动
				$.refreshScroller();
			}
			// 添加新条目
			$('.infinite-scroll-bottom .list-div').append(html);
		});
		
		

	}
	
	// 需要查询的店铺名字发生变化后，重置页码，清空原先的店铺列表，按照新的名字去查询
	$('#search').on('change', function(e) {
		productName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});
	
	$('.product-list').on('click','.card', function(e){
		productId = e.currentTarget.dataset.productId;
		var url = '/schoolo2o/frontend/productDetail?&productId=' + productId;
		window.location.href = url;
	});

	// 区域信息发生变化后，重置页码，清空原先的店铺列表，按照新的区域去查询
	$('#product-category-search').on('change', function() {
		productCategoryId = $('#product-category-search').val();
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	// 下滑屏幕自动进行分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});

	// 点击后打开右侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});

	// 初始化页面
	$.init();
});
