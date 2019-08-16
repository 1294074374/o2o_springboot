/**
 * 
 */
$(function(){
	//登陆验证的controller url
	var loginUrl = '/o2o/local/logincheck';
	//从地址栏的URL里获取usertype
	//usertype=1则为customer其余为shopowner
	var usertype=getQueryString("usertype");
	//登陆次数，累计超过三次失败之后自动弹出验证码要求输入
	var loginCount=0;
	
	$('#submit').click(function(){
		//获取输入的账号
		var userName=$('#username').val();
		//获取输入的密码
		var password=$('#psw').val();
		//获取验证码信息
		var verifyCodeActual = $('#j_captcha').val();
		//是否需要验证码验证，默认为false
		var needVerify = false;
		//如果登陆超过三次失败
		if(loginCount>=3){
			//需要验证码校验
			if(!verifyCodeActual){
				$.toast('请输入验证码!');
				return;
			}else{
				needVerify=true;
			}
		}
		//访问后台进行登陆验证
		$.ajax({
			url:loginUrl,
			async:false,
			cache:false,
			type:"post",
			dataType:'json',
			data:{
				userName:userName,
				password:password,
				verifyCodeActual:verifyCodeActual,
				//是否需要做验证码校验
				needVerify:needVerify
			},
			success:function(data){
				if(data.success){
					$.toast('登陆成功!');
					if(usertype == 1){
						//若用户在前端展示页面则自动连接到前端展示系统首页
						window.location.href='/o2o/frontend/index';
					}else{
						window.location.href='/o2o/shopadmin/shoplist';
					}
				}else{
					$.toast('登陆失败!'+data.errMsg);
					loginCount++;
					if(loginCount>=3){
						$('#verifyPart').show();
					}
				}
			}
		});
	});
});