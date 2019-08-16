$(function() {
	// var shopId = 1;
	// 获取此店铺下的商品列表的URL
	// var listUrl = 'getproductlistbyshop?pageIndex=1&pageSize=9999&shopId='+
	// shopId;
	var listUrl = 'getproductlistbyshop?pageIndex=1&pageSize=999';
	// 商品下架URL
	var deleteUrl = 'modifyproduct';
	/*
	 * 获取此店铺下的商品列表
	 */
	function getList() {
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var productList = data.productList;
				var tempHtml = '';
				productList.map(function(item, index) {
					// 遍历每条商品信息，拼接成一行显示，列信息包括：
					// 商品名称，优先级。上架下架（含productId）编辑按钮（含productId）
					// 预览（含producti）
					var textOp = "下架";
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						// 若状态值为0，标明是已下架的商品，操作表示上架（即点击上架按钮上架相关商品）
						textOp = "上架";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					// 拼接每件商品的行信息
					tempHtml += '' + '<div class="row row-product">'
							+ '<div class="col-30">'
							+ item.productName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.point
							+ '</div>'
							+ '<div class="col-50">'
							+ '<a href="#" class="edit" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">编辑</a>'
							+ '<a href="#" class="status" data-id="'
							+ item.productId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '<a href="#" class="preview" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">预览</a>'
							+ '</div>'
							+ '</div>';
				});
				// 拼接好的信息绑定到前端
				$('.product-wrap').html(tempHtml);
			}
		});
	}

	getList();

	function changeItemStatus(id, enableStatus) {
		var product = {};
		product.productId = id;
		product.enableStatus = enableStatus;
		$.confirm('确定么?', function() {
			$.ajax({
				url : deleteUrl,
				type : 'POST',
				data : {
					productStr : JSON.stringify(product),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						getList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}

	// 将class为product-wrap里面的a标签绑定上点击事件
	$('.product-wrap').on(
			'click',
			'a',
			function(e) {
				var target = $(e.currentTarget);
				if (target.hasClass('edit')) {
					// 如果有class edit则点击就进入店铺信息编辑页面，并带有productId参数
					window.location.href = 'productoperation?productId='
							+ e.currentTarget.dataset.id;
				} else if (target.hasClass('status')) {
					// 如果有class status调用后台功能上下架相关商品，并带有productId参数
					changeItemStatus(e.currentTarget.dataset.id,
							e.currentTarget.dataset.status);
				} else if (target.hasClass('preview')) {
					// 如果class preview 则去前台展示系统展示该商品详情也预览商品详情
					window.location.href = 'productdetail?productId='
							+ e.currentTarget.dataset.id;
				}
			});

	$('#new').click(function() {
		window.location.href = 'productedit';
	});
});