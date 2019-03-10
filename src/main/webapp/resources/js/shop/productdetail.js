$(function(){
	var productId = getQueryString("productId");
	var detailUrl = '/schoolo2o/shopadmin/getproductbyid?productId=' + productId;
	getProductById();
	function getProductById(){
		$.getJSON(detailUrl, function(data){
			if(data.success){
				var product = data.product;
				$('#product-name').text(product.productName);
				$('#product-category').text(product.productCategory.productCategoryName);
				$('#priority').text(product.priority);
				$('#normal-price').text(product.normalPrice);
				$('#promotion-price').text(product.promotionPrice);
				$('#product-img').attr("src", product.imgAddr);
				$('#product-desc').text(product.productDesc);
				var detailHtml = "";
				product.productImgList.map(function(item,index){
					detailHtml += '<img src="' + item.imgAddr + '"/>'
				});
				$('#detail-img').html(detailHtml);
			}			
		});
	};
	function getProductDetail(){
		
	}
})