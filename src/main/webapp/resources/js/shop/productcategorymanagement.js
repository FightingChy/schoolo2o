$(function(){
	var listUrl = '/schoolo2o/shopadmin/getproductcategorylist';
	var addUrl = '/schoolo2o/shopadmin/addproductcategorys';
	var deleteUrl = '/schoolo2o/shopadmin/removeproductcategory';
	getList();
	function getList(){
		$.getJSON(listUrl, function(data){
			if(data.success){
				var datalist = data.data;
				$('.category-wrap').html("");
				var tempHtml = '';
				datalist.map(function(item,index){
					tempHtml += '<div class="row row-product-category"><div class="col-33">'
						+ item.productCategoryName + '</div><div class="col-33">'
						+ item.priority
						+ '</div><div class="col-33"><a href="#" class="button delete" data-id="'
						+ item.productCategoryId + '">删除</a></div>'
						+ '</div>';
				});
				$('.category-wrap').html(tempHtml);
				
			}else{
				$.toast()
			}
			
		});
	}
	
})