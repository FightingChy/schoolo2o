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
					tempHtml += '<div class="row row-product-category now"><div class="col-33 product-category-name">'
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
	
	$('#submit').click(function(){
		var tempArr = $('.temp');
		var productCategoryList = [];
		tempArr.map(function(index, item){
			var productCategory = {};
			productCategory.productCategoryName = $(item).find('.category').val();
			productCategory.priority = $(item).find('.priority').val();
			if (productCategory.productCategoryName && productCategory.priority) {
				productCategoryList.push(productCategory);
			}
		});
		$.ajax({
			url : addUrl,
			type : 'POST',
			data : JSON.stringify(productCategoryList),
			contentType : 'application/json',
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					getList();
				} else {
					$.toast('提交失败！');
				}
			}
		});
		
	});
	
	$('#add').click(function(){
		$('.category-wrap').append('<div class="row row-product-category temp">'
				+ '<div class="col-33">'
				+ '<input type="text" class="category-input category" placeholder="商品类别"></input>'
				+ '</div>'
				+ '<div class="col-33">'
				+ '<input type="text" class="category-input priority" placeholder="优先级"></input>'
				+ '</div>'
				+ '<div class="col-33"><a href="#" class="button delete">删除</a></div>'
				+ '</div>')
	});
	
	$('.category-wrap').on('click', '.row-product-category.temp .delete',
			function(e) {
				console.log($(this).parent().parent());
				$(this).parent().parent().remove();

			});
	$('.category-wrap').on('click', '.row-product-category.now .delete',
			function(e) {
				var target = e.currentTarget;
				$.confirm('确定么?', function() {
					$.ajax({
						url : deleteUrl,
						type : 'POST',
						data : {
							productCategoryId : target.dataset.id
						},
						dataType : 'json',
						success : function(data) {
							if (data.success) {
								$.toast('删除成功！');
								getList();
							} else {
								$.toast('删除失败！');
							}
						}
					});
				});
			});
	
})