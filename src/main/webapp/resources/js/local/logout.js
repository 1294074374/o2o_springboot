/**
 * 
 */
$(function(){
	$('#log-out').click(function(){
		//清楚session
		$.ajax({
			url:"/o2o/local/loginout",
			type:"post",
			async:false,
			cache:false,
			dataType:'json',
			success:function(data){
				if(data.success){
					var usertype=$("#log-out").attr("usertype");
					//清楚成功后退出到登陆界面
					window.location.href="/o2o/local/login?usertype="+usertype;
					return false;
				}
			},
			error:function(data,error){
				alert(error);
			}
		});
	});
});