$(function(){
	var productId = getQueryString("productId");
	var detailUrl = '/schoolo2o/shopadmin/getproductbyid?productId=' + productId;
	var imgPath = 'D:/projectdev/images/';
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
				$('#product-img').attr("src", '/schoolo2o/image/showimg?path=' +encodeURIComponent(data.productImagePath));
				$('#product-desc').text(product.productDesc);
				var detailHtml = "";
				product.productImgList.map(function(item,index){
					detailHtml += '<img src="/schoolo2o/image/showimg?path=' + encodeURIComponent(imgPath + item.imgAddr)  + '"/>'
				});
				$('#detail-img').html(detailHtml);
			}			
		});
	};
	function getProductDetail(){
		
	}
})