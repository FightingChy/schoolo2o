$(function() {

	// 从URL里获取productId参数的值
	var productId = getQueryString('productId');
	// 通过productId获取商品信息的URL
	var infoUrl = '/schoolo2o/shopadmin/getproductbyid?productId=' + productId;
	// 获取当前店铺设定的商品类别列表的URL
	var categoryUrl = '/schoolo2o/shopadmin/getproductcategorylist';
	// 更新商品信息的URL
	var productPostUrl = '/schoolo2o/shopadmin/modifyproduct';
	var isEdit = false;
	if (productId) {
		getInfo(productId);
		isEdit = false;
	} else {
		getCategory();
		productPostUrl = '/schoolo2o/shopadmin/addproduct';
	}

	function getInfo(id) {
		$.getJSON(infoUrl,
						function(data) {
							if (data.success) {
								var product = data.product;
								$('#product-name').val(product.productName);
								$('#priority').val(product.priority);
								$('#product-desc').val(product.productDesc);
								$('#normal-price').val(product.normalPrice);
								$('#promotion-price').val(
										product.promotionPrice);
								// 获取原本的商品类别以及该店铺的所有商品类别列表
								var optionHtml = '';
								var optionSelected = product.productCategory.productCategoryId;
								var productCategoryList = data.productCategoryList;
								productCategoryList
										.map(function(item, index) {
											var isSelect = optionSelected === item.productCategoryId ? 'selected'
													: '';
											optionHtml += '<option data-value="'
													+ item.productCategoryId
													+ '"'
													+ isSelect
													+ '>'
													+ item.productCategoryName
													+ '</option>';
										});
								$('#category').html(optionHtml);
							}
						})
	};

	function getCategory() {
		$.getJSON(categoryUrl, function(data){
			if(data.success){
				var productCategoryList = data.data;
				var optionHtml = '';
				productCategoryList
				.map(function(item, index) {
					optionHtml += '<option data-value="'
							+ item.productCategoryId
							+ '"'
							+ '>'
							+ item.productCategoryName
							+ '</option>';
				});
				$('#category').html(optionHtml);
			}
		});

	};
	//针对商品详情图控件组，若该控件组的最后一个元素发生变化（即上传了图片）
	//且控件总数未达到6个，则生成新的一个文件上传控件
	$('.detail-img-div').on('change', '.detail-img:last-child', function() {
		if ($('.detail-img').length < 6) {
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});

	$('#submit').click(function() {
		var product = {};
		product.productName = $('#product-name').val();
		product.productDesc = $('#product-desc').val();
		product.normalPrice = $('#normal-price').val();
		product.promotionPrice = $('#promotion-price').val();
		product.priority = $('#priority').val();
		// 选择选定好的店铺类别
		product.productCategory = {
				productCategoryId : $('#category').find('option').not(function() {
				return !this.selected;
			}).data('value')
		};
		product.productId = productId;
		// 获取缩略图文件流
		var thumbnail = $('#small-img')[0].files[0];
		// 生成表单对象，用于接收参数并传递给后台
		var formData = new FormData();
		formData.append('thumbnail', thumbnail);
		// 遍历商品详情图控件，获取里面的文件流
		$('.detail-img').map(
				function(index, item) {
					// 判断该控件是否已选择了文件
					if ($('.detail-img')[index].files.length > 0) {
						// 将第i个文件流赋值给key为productImgi的表单键值对里
						formData.append('productImg' + index,
								$('.detail-img')[index].files[0]);
					}
				});
//		var detailImg = $('.detail-img')[0].files;
//		formData.append('detailImgList', detailImg);
		// 将product json对象转成字符流保存至表单对象key为productStr的的键值对里
		formData.append('productStr', JSON.stringify(product));
		// 获取表单里输入的验证码
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		// 将数据提交至后台处理相关操作
		$.ajax({
			url : productPostUrl,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					$('#captcha_img').click();
				} else {
					$.toast(data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
		

	});

})