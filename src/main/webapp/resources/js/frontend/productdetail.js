$(function(){
	var productId = getQueryString("productId");
	var productDetailUrl = '/schoolo2o/frontend/listproductdetailpageinfo?&productId=' + productId;
	$.getJSON(productDetailUrl, function(data){
		if(data.success){
			var product = data.product;
			//商品缩略图
			$('#product-img').attr("src",product.imgAddr);
			$('#product-time').text(
					new Date(product.lastEditTime).Format("yyyy-MM-dd"));
			//商品名称
			$('#product-name').text(product.productName);
			$('#product-desc').text(product.productDesc);
			if(product.normalPrice != undefined 
					&& product.promotionPrice != undefined){
				//如果现价和原价都不为空则都展示，并且给原价加个删除符号
				$('#price').show();
				$('#normalPrice').html(
						'<del>' + '¥' + product.normalPrice + '</del>');
				$('#promotionPrice').text('¥' + product.promotionPrice);
			}else if(product.normalPrice != undefined 
					&& product.promotionPrice == undefined){
				$('#price').show();
				$('#normalPrice').text('¥' + product.normalPrice);
			}else if(product.normalPrice == undefined 
					&& product.promotionPrice != undefined){
				$('#promotionPrice').text('¥' + product.promotionPrice);
			}
			
			var html = '';
			product.productImgList.map(function(item,index){
				html += '<div><img src="' + item.imgAddr + '" width="100%"></img></div>'
			})
			$('#imgList').html(html);
		}
	});

	// 点击后打开右侧栏
	$('#me').click(function() {
		$.openPanel('#panel-right-demo');
	});
	$.init();
})