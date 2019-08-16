/**
 * 
 */
$(function(){
	//修改平台妈妈的controller url
	var url='/o2o/local/changelocalpwd';
	//从地址栏的url中获取uertype
	//usertype=1 则为customer，其余为shopowner
	var usertype=getQueryString("usertype");
	$('#submit').click(function(){
		//获取账号
		var userName = $('#username').val();
		//获取密码
		var password = $('#password').val();
		var newPassword=$('#newPassword').val();
		var confirmPassword=$('#confirmPassword').val();
		if(newPassword != confirmPassword){
			//两次密码不正确
			$.toast('两次输入的新密码不一致！');
			return;
		}
		//添加表单数据
		var formData =  new FormData();
		formData.append('userName',userName);
		formData.append('password',password);
		formData.append('newPassword',newPassword);
		//获取验证码
		var verifyCodeActual = $('#j_captcha').val();
		if(!verifyCodeActual){
			$.toast('请输入验证码!');
			return;
		}
		formData.append("verifyCodeActual",verifyCodeActual);
		//请求后台
		$.ajax({
			url:url,
			type:'POST',
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			success:function(data){
				if(data.success){
					$.toast('提交成功!');
					if(usertype==1){
						//若用户在前端展示系统页面则自动回到前端展示系统首页
						window.location.href='/o2o/frontend/index';
					}else{
						//若用户在店家系统页面则自动回到店铺列表首页
						window.location.href='/o2o/shopadmin/shoplist';
					}
				}else{
					$.toast('提交失败!'+data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});
	$('#back').click(function(){
		window.location.href='/o2o/shopadmin/shoplist';
	});
});