package com.imooc.o2o.interceptor.shopadmin;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.imooc.o2o.entity.PersonInfo;

public class ShopLoginInterceptor extends HandlerInterceptorAdapter {
	/*
	 * 主要做事前拦截，即用户操作发生前，改写preHandle里的逻辑，进行拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从session中取出用户信息
		Object userObj = request.getSession().getAttribute("user");
		if (userObj != null) {
			// 若用户信息不为空则将session里的用户信息转换成PersonInfo实体类对象
			PersonInfo user = (PersonInfo) userObj;
			// 做空值判断，确保userId不为空并且该账号的可用状态为1，并且用户类型为1
			if (user != null && user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1) {
				// 若通过验证则返回true,拦截器返回true之后，用户接下来的操作得以正确
				return true;
			}
		}
		// 若不满足登陆验证，则直接跳转到账号登陆页面
		PrintWriter out = response.getWriter();
		out.print("<html>");
		out.print("    <script>");
		out.print("        window.open('" + request.getContextPath() + "/local/login?usertype=2','_self')");
		out.print("    </script>");
		out.print("</html>");
		return false;
	}
}
