$(function(){
	var listUrl = '/schoolo2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
	var editUrl = '/schoolo2o/shopadmin/editproduct';
	var detailUrl = '/schoolo2o/shopadmin/removeproduct';
	var deleteUrl = '/schoolo2o/shopadmin/removeproduct';
	getProductList();
	function getProductList(){
		$.getJSON(listUrl, function(data){
			var productList = data.data;
			var productHtml = "";
			productList.map(function(item,index){
				productHtml += '<div class="row row-product"><div class="col-25">'
					+ item.productName + '</div><div class="col-25">'
					+ item.productCategory.productCategoryName + '</div><div class="col-25">'
					+ item.productDesc
					+ '</div><div class="col-25">'
					+ goProduct(item.productId) + '</div></div>';
			});
			$(".product-wrap").html(productHtml);			
		});
	};
	
	function goProduct(id){
		var html = '<a href="/schoolo2o/shopadmin/productoperation?productId='
				+ id + '">编辑</a>' 
				+ '<a href="/schoolo2o/shopadmin/productdetail?productId=' 
				+ id +'">预览</a>' 
				+ '<a href="#">删除</a>';
		return html;
	}
});