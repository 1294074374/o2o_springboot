package com.imooc.o2o.web.local;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exception.LocalAuthOperationException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "/local")
public class LocalAuthController {
	@Autowired
	private LocalAuthService localAuthService;

	/**
	 * 用户退出登陆
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loginout", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> loginout(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 将用户session置为空
		request.getSession().setAttribute("user", null);
		modelMap.put("success", true);
		return modelMap;
	}

	/**
	 * 用户登陆系统
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/logincheck", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> logincheck(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 错误三次就需要输入验证码
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if (needVerify && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		if (userName != null && password != null) {
			LocalAuth tempAuth = localAuthService.getLocalAuthByUserNameAndPwd(userName, password);
			if (tempAuth != null) {
				tempAuth.setUserId(tempAuth.getPersonInfo().getUserId());
				// 成功登陆获取对象
				modelMap.put("success", true);
				request.getSession().setAttribute("user", tempAuth.getPersonInfo());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户或密码错误");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户或密码不能为空");
		}
		return modelMap;
	}

	/**
	 * & 用户修改密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyLocalAuth(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
			return modelMap;
		}
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

		// 非空判断
		if (userName != null && password != null && newPassword != null
				|| user.getUserId() != null && !password.equals(newPassword)) {
			try {
				// 检查时候存在着用户 账号密码是否能
				LocalAuth tempAuth = localAuthService.getLocalAuthByUserNameAndPwd(userName, password);
				if (tempAuth == null || !tempAuth.getUserName().equals(userName)) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "输入的账号非本次登陆账号");
					return modelMap;
				}
				// 原密码账号已正确 修改平台账号的用户密码
				LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getUserId(), userName, password,
						newPassword, new Date());
				if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", le.getStateInfo());
				}
			} catch (LocalAuthOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入密码");
		}

		return modelMap;
	}

	/**
	 * 创建用户信息
	 * 
	 * @param request
	 * @return
	 * @throws LocalAuthOperationException
	 */
	@RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> bindLocalAuth(HttpServletRequest request) throws LocalAuthOperationException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "验证码错误");
		}
		String userName = HttpServletRequestUtil.getString(request, "userName");
		String password = HttpServletRequestUtil.getString(request, "password");
		// 从session中获取当前用户信息
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		// 非空判断
		if (userName != null && password != null && user != null && user.getUserId() != null) {
			// 创建localAuth对象并且赋值
			LocalAuth localAuth = new LocalAuth();
			localAuth.setUserName(userName);
			localAuth.setPersonInfo(user);
			localAuth.setPassword(password);
			localAuth.setUserId(user.getUserId());
			// 插入数据库
			LocalAuthExecution ie = localAuthService.bindLocalAuth(localAuth);
			if (ie.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ie.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名、密码不能为空");
		}
		return modelMap;
	}
}
